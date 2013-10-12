package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.AddressLayer.ReturnAddressFactory;
import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder;
import com.github.catageek.ByteCart.CollisionManagement.Router;
import com.github.catageek.ByteCart.CollisionManagement.RouterCollisionAvoiderBuilder;
import com.github.catageek.ByteCart.Routing.RoutingTableWritable;
import com.github.catageek.ByteCart.Routing.RoutingTableFactory;
import com.github.catageek.ByteCart.Updaters.DefaultRouterWanderer;
import com.github.catageek.ByteCart.Wanderer.WandererContentFactory;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.Event.SignPostRouteEvent;
import com.github.catageek.ByteCartAPI.Event.SignPreRouteEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterPassRouterEvent;
import com.github.catageek.ByteCartAPI.Signs.BCRouter;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Util.MathUtil;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;



/**
 * An IC at the entry of a L1 router
 */
public class BC8010 extends AbstractTriggeredSign implements BCRouter, Triggable, HasRoutingTable {

	private final BlockFace From;
	private final Address Sign;
	private final RoutingTableWritable RoutingTable;
	private AddressRouted destination;
	private final Block center;
	protected boolean IsTrackNumberProvider;

	BC8010(Block block, org.bukkit.entity.Vehicle vehicle) throws ClassNotFoundException, IOException {
		super(block, vehicle);
		this.IsTrackNumberProvider = true;
		From = this.getCardinal().getOppositeFace();
		// reading destination address of the cart
		destination = AddressFactory.getAddress(this.getInventory());
		// reading address written on BC8010 sign
		Sign = AddressFactory.getAddress(this.getBlock(),3);
		// Center of the router, at sign level
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

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.Triggable#trigger()
	 */
	@Override
	public void trigger() throws ClassNotFoundException, IOException {

		CollisionAvoiderBuilder builder = new RouterCollisionAvoiderBuilder(this, center.getLocation());

		try {

			BlockFace direction, to;
			Router router = ByteCart.myPlugin.getCollisionAvoiderManager().<Router>getCollisionAvoider(builder);
			boolean isTrain = AbstractTriggeredSign.isTrain(destination);

			// Here begins the triggered action

			// is this an updater who needs special routing ? no then routing normally
			if(selectUpdater()) {
				//				UpdaterManager um = ByteCart.myPlugin.getUm();

				// non updater carts case
				//				if (! um.isUpdater(vehicle.getEntityId())) {
				//				if (! RoutingTableExchangeFactory.isRoutingTableExchange(getInventory(), Sign.getRegion().getAmount(), this.getLevel())) {

				// if this is a cart in a train
				if (this.wasTrain(this.getLocation())) {

					// leave a message to next cart that it is a train
					ByteCart.myPlugin.getIsTrainManager().getMap().reset(getLocation());
					// tell to router not to change position
					ByteCart.myPlugin.getCollisionAvoiderManager().<Router>getCollisionAvoider(builder).Book(isTrain);
					return;
				}

				if (destination != null) {
					// Time-to-live management

					//loading TTl of cart
					int ttl = destination.getTTL();

					// if ttl did not reach end of life ( = 0)
					if (ttl != 0) {

						destination.updateTTL(ttl-1);
					}

					// if ttl was 1 (now 0), we try to return the cart to source station

					if (ttl == 1 && tryReturnCart())
						destination = AddressFactory.getAddress(this.getInventory());

					if(ByteCart.debug)
						ByteCart.log.info("ByteCart : TTL is " + destination.getTTL());


					// if this is the first car of a train
					// we keep it during 2 s
					if (isTrain) {
						this.setWasTrain(this.getLocation(), true);
					}

					destination.finalizeAddress();
				}

				direction = this.SelectRoute(destination, Sign, RoutingTable);
				/*			}
							else {
					// is an updater (this code is executed only by BC8020)
					int region = RoutingTableExchangeFactory.getRoutingTableExchange(getInventory(), Sign.getRegion().getAmount(), this.getLevel()).;
					if(ByteCart.debug)
						ByteCart.log.info("ByteCart : region " + region);
					try {
						direction = RoutingTableWritable.getDirection(region).getBlockFace();
					} catch (NullPointerException e) {
						// this region does not exist
						direction = From;
						// remove the cart as updater
						//um.getMapRoutes().remove(vehicle.getEntityId());
						RoutingTableExchangeFactory.deleteRoutingTableExchange(getInventory());
					}
				}
				 */
				// trigger event
				BlockFace bdest = router.WishToGo(From, direction, isTrain);
				int ring = this.getRoutingTable().getDirectlyConnected(new DirectionRegistry(bdest));
				SignPostRouteEvent event = new SignPostRouteEvent(this, ring);
				Bukkit.getServer().getPluginManager().callEvent(event);

				return;
			}

			// it's an updater, so let it choosing direction
			Wanderer updater = getUpdater();

			// routing normally
			to = router.WishToGo(From, updater.giveRouterDirection(), isTrain);

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
	 * @return true if the cart needs normal routing
	 */
	protected boolean selectUpdater() {
		// everything that is not an updater must be routed
		//return ! ByteCart.myPlugin.getUm().isUpdater(id);
		return ! WandererContentFactory.isWanderer(getInventory());
	}

	/**
	 * Compute the direction to take
	 *
	 * @param IPaddress the destination address
	 * @param sign the BC sign
	 * @param RoutingTableWritable the routing table contained in the chest
	 * @return the direction to destination, or to ring 0. If ring 0 does not exist, random direction
	 */
	protected BlockFace SelectRoute(AddressRouted IPaddress, Address sign, RoutingTableWritable RoutingTable) {

		DirectionRegistry face;
		// same region : lookup destination track
		if (IPaddress != null && IPaddress.getRegion().getAmount() == sign.getRegion().getAmount() && IPaddress.getTTL() != 0) {
			int destination = this.destination.getTrack().getAmount();
			DirectionRegistry out = RoutingTable.getDirection(destination);
			if (out != null) {
				// trigger event
				SignPreRouteEvent event = new SignPreRouteEvent(this, this.getRoutingTable().getDirectlyConnected(out));
				Bukkit.getServer().getPluginManager().callEvent(event);
				return RoutingTable.getDirection(event.getTargetTrack()).getBlockFace();
			}
		}

		// If not in same region, or if TTL is 0, or the ring does not exist then we lookup track 0
		if ((face = RoutingTable.getDirection(0)) != null)
			return face.getBlockFace();

		// If everything has failed, then we randomize output direction
		return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getCardinal().getOppositeFace());
	}

	/**
	 * Try to send the cart to its return address
	 *
	 * @return true if success
	 */
	private boolean tryReturnCart() {
		Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());
		if (returnAddress != null && returnAddress.isReturnable()) {
			(new BC7017(this.getBlock(), this.getVehicle())).trigger();
			return true;
		}
		return false;
	}

	/**
	 * Get the updater object
	 *
	 * @return the updater
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	protected final Wanderer getUpdater() throws ClassNotFoundException, IOException {
		return ByteCart.myPlugin.getWandererManager().getFactory(this.getInventory()).getWanderer(this, this.getInventory());
	}


	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BCSign#getLevel()
	 */
	@Override
	public Wanderer.Level getLevel() {
		return Wanderer.Level.REGION;
	}

	/**
	 * Return the direction from where the cart is coming
	 *
	 * @return the direction
	 */
	public final BlockFace getFrom() {
		return From;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BCSign#getSignAddress()
	 */
	@Override
	public final Address getSignAddress() {
		return Sign;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.HasRoutingTable#getRoutingTable()
	 */
	@Override
	public final RoutingTableWritable getRoutingTable() {
		return RoutingTable;
	}

	/**
	 * Tell if this IC will provide track numbers during configuration
	 *
	 * @return true if the IC provides track number
	 */
	public final boolean isTrackNumberProvider() {
		return IsTrackNumberProvider;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BCSign#getDestinationIP()
	 */
	@Override
	public final String getDestinationIP() {
		return destination.toString();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BCRouter#getOriginTrack()
	 */
	@Override
	public final int getOriginTrack() {
		return Sign.getTrack().getAmount();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BCSign#getCenter()
	 */
	@Override
	public final Block getCenter() {
		return center;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getName()
	 */
	@Override
	public String getName() {
		return "BC8010";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return "L1 Router";
	}
}
