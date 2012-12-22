package com.github.catageek.ByteCart.Signs;

import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder;
import com.github.catageek.ByteCart.CollisionManagement.Router;
import com.github.catageek.ByteCart.CollisionManagement.RouterCollisionAvoiderBuilder;
import com.github.catageek.ByteCart.EventManagement.AbstractTriggeredIC;
import com.github.catageek.ByteCart.EventManagement.TriggeredIC;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Routing.RoutingTable;
import com.github.catageek.ByteCart.Routing.RoutingTableExchange;
import com.github.catageek.ByteCart.Routing.RoutingTableFactory;
import com.github.catageek.ByteCart.Util.DirectionRegistry;
import com.github.catageek.ByteCart.Util.MathUtil;



public class BC8010 extends AbstractTriggeredIC implements TriggeredIC {


	public BC8010(Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC8010";
		this.FriendlyName = "L1 router";
		this.Triggertax = ByteCart.myPlugin.getConfig().getInt("usetax." + this.Name);
		this.Permission = this.Permission + this.Name;

	}

	@Override
	public void trigger() {

		// Centre de l'aiguillage
		Block center = this.getBlock().getRelative(this.getCardinal(), 6).getRelative(MathUtil.clockwise(this.getCardinal()));

		CollisionAvoiderBuilder builder = new RouterCollisionAvoiderBuilder(this, center.getLocation());

		try {

			// reading destination address of the cart
			AddressRouted IPaddress = AddressFactory.getAddress(this.getInventory());

			// reading address written on BC8010 sign
			Address sign = AddressFactory.getAddress(this.getBlock(),3);

			// Loading inventory of chest above router
			Inventory ChestInventory = ((InventoryHolder) center.getRelative(BlockFace.UP, 5).getState()).getInventory();

			// Converting inventory in routing table
			RoutingTable RoutingTable = RoutingTableFactory.getRoutingTable(ChestInventory);

			// Here begins the triggered action
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
			int ttl = IPaddress.getTTL();

			// if ttl did not reach end of life ( = 0)
			if (ttl != 0) {

				IPaddress.updateTTL(ttl-1);
			}

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : TTL is " + IPaddress.getTTL());


			// if this is the first car of a train
			// we keep it during 2 s
			if (this.isTrain()) {
				this.setWasTrain(this.getLocation(), true);
			}

			BlockFace direction, to, from = this.getCardinal().getOppositeFace();
			int vid = this.getVehicle().getEntityId();
			Router router = ByteCart.myPlugin.getCollisionAvoiderManager().<Router>getCollisionAvoider(builder);
			int track = sign.getTrack().getAmount();
			boolean isUpdater = ByteCart.myPlugin.getUm().isUpdater(vid);

			if(isUpdater)
				direction = getRandomBlockFace(RoutingTable, from);
			else
				direction = this.SelectRoute(IPaddress, sign, RoutingTable);

			synchronized(router) {
				to = router.WishToGo(from, direction, isTrain());
			}

			if(isUpdater)
				Updater(RoutingTable, new DirectionRegistry(from), new DirectionRegistry(to), track, vid);

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

	protected BlockFace SelectRoute(AddressRouted IPaddress, Address sign, RoutingTable RoutingTable) {
		// If not in same region, or if TTL is 1 or 0, then we lookup track 0
		if (IPaddress.getRegion().getAmount() != sign.getRegion().getAmount() || IPaddress.getTTL() == 0) {
			return RoutingTable.getDirection(0).getBlockFace();
		} else
		{	// same region : lookup destination track
			return RoutingTable.getDirection(IPaddress.getTrack().getAmount()).getBlockFace();
		}

	}

	protected void Updater(RoutingTable RoutingTable, DirectionRegistry from, DirectionRegistry to, int fromring, int vehicleId) {

		// Storing the route from where we arrive

		RoutingTable.setEntry(fromring, MathUtil.binlog(from.getAmount()) << 4, 0);

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Updater : storing ring " + fromring + " direction " + from.ToString());

		// loading received routes in router
		RoutingTableExchange routes = ByteCart.myPlugin.getUm().getMap().get(vehicleId);
		if (routes != null)
			RoutingTable.Update(routes, from);


		// preparing the routes to send
		routes = new RoutingTableExchange(RoutingTable, to);

		// storing the route in the map
		ByteCart.myPlugin.getUm().getMap().put(vehicleId, routes);

	}


	private final static BlockFace getRandomBlockFace(RoutingTable RoutingTable, BlockFace from) {

		// selecting a random destination avoiding ring 0 or where we come from
		boolean zerodistance = RoutingTable.getDistance(0) == 0;
		int zerodirection = RoutingTable.getDirection(0).getAmount();

		DirectionRegistry direction = new DirectionRegistry(1 << (new Random()).nextInt(4));

		while (direction.getBlockFace() == from || (zerodistance && (zerodirection == direction.getAmount()))) {
			direction.setAmount(1 << (new Random()).nextInt(4));
		}

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Updater : direction selected " + direction.ToString());

		return direction.getBlockFace();
	}


}
