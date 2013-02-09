package com.github.catageek.ByteCart.Signs;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;


import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder;
import com.github.catageek.ByteCart.CollisionManagement.Router;
import com.github.catageek.ByteCart.CollisionManagement.RouterCollisionAvoiderBuilder;
import com.github.catageek.ByteCart.Event.SignPostRouteEvent;
import com.github.catageek.ByteCart.Event.UpdaterPassRouterEvent;
import com.github.catageek.ByteCart.Event.SignPreRouteEvent;
import com.github.catageek.ByteCart.Routing.DefaultRouterWanderer;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Routing.RoutingTable;
import com.github.catageek.ByteCart.Routing.RoutingTableFactory;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Routing.UpdaterFactory;
import com.github.catageek.ByteCart.Storage.UpdaterManager;
import com.github.catageek.ByteCart.Util.DirectionRegistry;
import com.github.catageek.ByteCart.Util.MathUtil;



public class BC8010 extends AbstractTriggeredSign implements BCRouter, Triggable, HasRoutingTable {
	
	private final BlockFace From;
	private final Address Sign;
	private final RoutingTable RoutingTable;
	private final AddressRouted destination;
	private final Block center;
	protected boolean IsTrackNumberProvider;

	public BC8010(Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.IsTrackNumberProvider = true;
		From = this.getCardinal().getOppositeFace();
		// reading destination address of the cart
		destination = AddressFactory.getAddress(this.getInventory());
		// reading address written on BC8010 sign
		Sign = AddressFactory.getAddress(this.getBlock(),3);
		// Centre de l'aiguillage
		center = this.getBlock().getRelative(this.getCardinal(), 6).getRelative(MathUtil.clockwise(this.getCardinal()));
		
		BlockState blockstate;
		
		if ((blockstate = center.getRelative(BlockFace.UP, 5).getState()) instanceof InventoryHolder) {
			// Loading inventory of chest above router
			Inventory ChestInventory = ((InventoryHolder) blockstate).getInventory();

			// Converting inventory in routing table
			RoutingTable = RoutingTableFactory.getRoutingTable(ChestInventory);
		}
		else {
			RoutingTable = null;
		}
	}

	@Override
	public void trigger() {

		CollisionAvoiderBuilder builder = new RouterCollisionAvoiderBuilder(this, center.getLocation());

		try {

			BlockFace direction, to;
			org.bukkit.entity.Vehicle vehicle = this.getVehicle();
			Router router = ByteCart.myPlugin.getCollisionAvoiderManager().<Router>getCollisionAvoider(builder);

			// Here begins the triggered action

			// is this an updater who needs special routing ? no then routing normally
			if(selectUpdater(vehicle.getEntityId())) {
				UpdaterManager um = ByteCart.myPlugin.getUm();

				// non updater carts case
				if (! um.isUpdater(vehicle.getEntityId())) {
					// if this is a cart in a train
					if (this.wasTrain(this.getLocation())) {

						// leave a message to next cart that it is a train
						ByteCart.myPlugin.getIsTrainManager().getMap().reset(getLocation());
						// tell to router not to change position
						ByteCart.myPlugin.getCollisionAvoiderManager().<Router>getCollisionAvoider(builder).Book(this.isTrain());
						return;
					}

					// Time-to-live management

					//loading TTl of cart
					int ttl = destination.getTTL();

					// if ttl did not reach end of life ( = 0)
					if (ttl != 0) {

						destination.updateTTL(ttl-1);
					}

					if(ByteCart.debug)
						ByteCart.log.info("ByteCart : TTL is " + destination.getTTL());


					// if this is the first car of a train
					// we keep it during 2 s
					if (this.isTrain()) {
						this.setWasTrain(this.getLocation(), true);
					}


					direction = this.SelectRoute(destination, Sign, RoutingTable);
				}
				else {
					// is an updater (this code is executed only by BC8020)
					int region = ByteCart.myPlugin.getWm().getRegion(vehicle.getEntityId());
					if(ByteCart.debug)
						ByteCart.log.info("ByteCart : region " + region);
					try {
						direction = RoutingTable.getDirection(region).getBlockFace();
					} catch (NullPointerException e) {
						// this region does not exist
						direction = From;
						// remove the cart as updater
						um.getMapRoutes().remove(vehicle.getEntityId());
					}
				}

				// trigger event
				BlockFace bdest = router.WishToGo(From, direction, isTrain());
				int ring = this.getRoutingTable().getDirectlyConnected(new DirectionRegistry(bdest));
				SignPostRouteEvent event = new SignPostRouteEvent(this, ring);
				Bukkit.getServer().getPluginManager().callEvent(event);
				
				return;
			}

			// it's an updater, so let it choosing direction
			Updater updater = getUpdater();

			// routing normally
			to = router.WishToGo(From, updater.giveRouterDirection(), isTrain());
			
			int nextring = this.getRoutingTable().getDirectlyConnected(new DirectionRegistry(to));
			UpdaterPassRouterEvent event = new UpdaterPassRouterEvent(updater, to, nextring);
			Bukkit.getServer().getPluginManager().callEvent(event);

			// here we perform routes update
			updater.doAction(to);

		}
		catch (ClassCastException e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : " + e.toString());
			e.printStackTrace();

			// Not the good blocks to build the signs
			return;
		}
		catch (NullPointerException e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : "+ e.toString());

			e.printStackTrace();

			// there was no inventory in the cart
			return;
		}




	}

	/**
	 * Tells if this cart needs normal routing
	 * @param id: id of the cart
	 * @return: true if the cart needs normal routing
	 */
	protected boolean selectUpdater(int id) {
		// everything that is not an updater must be routed
		return ! ByteCart.myPlugin.getUm().isUpdater(id);
	}

	protected BlockFace SelectRoute(AddressRouted IPaddress, Address sign, RoutingTable RoutingTable) {

		// same region : lookup destination track
		try {
			if (IPaddress.getRegion().getAmount() == sign.getRegion().getAmount() && IPaddress.getTTL() != 0) {
				int destination = this.destination.getTrack().getAmount();
				DirectionRegistry out = RoutingTable.getDirection(destination);
				// trigger event
				SignPreRouteEvent event = new SignPreRouteEvent(this, this.getRoutingTable().getDirectlyConnected(out));
				Bukkit.getServer().getPluginManager().callEvent(event);
				return out.getBlockFace();
			}
		} catch (NullPointerException e) {
		}

		// If not in same region, or if TTL is 1 or 0, then we lookup track 0
		try {
			return RoutingTable.getDirection(0).getBlockFace();
		} catch (NullPointerException e) {
		}

		return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getCardinal().getOppositeFace());


	}

	protected final Updater getUpdater() {
		return UpdaterFactory.getUpdater(this);
	}


	public Updater.Level getLevel() {
		return Updater.Level.REGION;
	}

	public final BlockFace getFrom() {
		return From;
	}

	public final Address getSignAddress() {
		return Sign;
	}

	public final RoutingTable getRoutingTable() {
		return RoutingTable;
	}
	
	public final boolean isTrackNumberProvider() {
		return IsTrackNumberProvider;
	}
	
	public final String getDestinationIP() {
		return destination.toString();
	}
	
	public final int getOriginTrack() {
		return Sign.getTrack().getAmount();
	}

	public final Block getCenter() {
		return center;
	}
	
	@Override
	public String getName() {
		return "BC8010";
	}

	@Override
	public String getFriendlyName() {
		return "L1 Router";
	}
}
