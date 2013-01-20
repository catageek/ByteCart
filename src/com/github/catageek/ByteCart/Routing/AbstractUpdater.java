package com.github.catageek.ByteCart.Routing;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Vehicle;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.HAL.CounterInventory;
import com.github.catageek.ByteCart.IO.InventoryHalfStack;
import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Util.DirectionRegistry;
import com.github.catageek.ByteCart.Util.MathUtil;

public abstract class AbstractUpdater {



	/**
	 * @param to 
	 * @param routingtable: the router's routing table
	 * @param exchange: the routes received from the BCUpdater cart
	 * @param ringAddress: the address written on the BC80XX sign under the cart
	 * @param from: from where the cart arrives
	 */
	protected AbstractUpdater(RoutingTable routingtable,
			Vehicle vehicle, Address signaddress, BlockFace from, boolean isTrackNumberProvider, Level level) {
		super();
		RoutingTable = routingtable;
		Vehicle = vehicle;
		ByteCart.myPlugin.getUm().getMapRoutes().get(vehicle.getEntityId());
		SignAddress = signaddress;
		From = new DirectionRegistry(from);
		IsTrackNumberProvider = isTrackNumberProvider;
		Level = level;
		counter = new CounterInventory(new InventoryHalfStack(((StorageMinecart) this.Vehicle).getInventory()));
		Routes = ByteCart.myPlugin.getUm().getMapRoutes().get(Vehicle.getEntityId());
	}

	private final RoutingTable RoutingTable;
	private final Vehicle Vehicle;
	private final Address SignAddress;
	private final DirectionRegistry From;
	private final boolean IsTrackNumberProvider;
	private final Level Level;
	private final CounterInventory counter;
	private RoutingTableExchange Routes;

	abstract protected String getAddress(int var);
	abstract protected int setSign(int current);
	abstract protected int getCurrent(int current);

	public void Update(Side To) {
		return;
	}

	public void reset() {
		// case of reset
			// erase address on sign
			SignAddress.remove();
			// clear routes except route to ring 0
			RoutingTable.clear();
	}
	
	protected boolean isResetCart() {
		return Routes != null && Routes.getLevel().number == this.getLevel().number + 8;
	}
	
	protected void routeUpdates(BlockFace To, int current) {
		if(isRouteConsumer()) {
		List<Integer> connected = RoutingTable.getDirectlyConnected(getFrom());

		// if the track we come from is not recorded
		// or others track are wrongly recorded, we correct this
		if (current >=0 && ( ! connected.contains(current) || connected.size() != 1)) {

			Iterator<Integer> it = connected.iterator();
			while (it.hasNext()) {
				RoutingTable.removeEntry(it.next());
			}

			// Storing the route from where we arrive
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : Updater : storing ring " + current + " direction " + From.ToString());

			RoutingTable.setEntry(current, MathUtil.binlog(From.getAmount()) << 4, 0);

		}

		// loading received routes in router
		RoutingTable.Update(Routes, From);


		// preparing the routes to send
		Routes = new RoutingTableExchange(RoutingTable, new DirectionRegistry(To), this.getLevel(), Routes.getRegion());


		// storing the route in the map
		ByteCart.myPlugin.getUm().getMapRoutes().put(Vehicle.getEntityId(), Routes, false);
		}
		

		// If we are turning back, keep current track otherwise discard
		if (isSameTrack(To))
			Routes.setCurrent(current);
		else {
			Routes.setCurrent(-1);
		}

	}


	public Side giveSimpleDirection() {
		return Side.LEFT;
	}



	protected boolean isSameTrack(BlockFace To) {
		return From.getBlockFace().equals(To);
	}

	protected final int getTrackNumber() {
		switch(Routes.getLevel()) {
		case BACKBONE:
		case RESET_BACKBONE:
			return SignAddress.getRegion().getAmount();
		case REGION:
		case RESET_REGION:
			return SignAddress.getTrack().getAmount();
		case LOCAL:
		case RESET_LOCAL:
			break;
		}
		return -1;
	}

	public final BlockFace giveRouterDirection() {
		return this.selectDirection();
	}

	public final static BlockFace getRandomBlockFace(RoutingTable routingtable, BlockFace from) {

		// selecting a random destination avoiding ring 0 or where we come from
		boolean zerodistance = routingtable.getDistance(0) == 0;
		int zerodirection = (routingtable.getDirection(0) != null ? routingtable.getDirection(0).getAmount() : -1);

		DirectionRegistry direction = new DirectionRegistry(1 << (new Random()).nextInt(4));

		while (direction.getBlockFace() == from || (zerodistance && (zerodirection == direction.getAmount()))) {
			direction.setAmount(1 << (new Random()).nextInt(4));
		}

		return direction.getBlockFace();
	}

	protected BlockFace selectDirection() {
		if (Routes != null) {

			// case of reset
			if (Routes.getLevel().number == this.getLevel().number + 8) {
				return AbstractUpdater.getRandomBlockFace(RoutingTable, From.getBlockFace());
			}

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : Updater : cart level " + Routes.getLevel().number + ", router level " + this.getLevel().number);

			if ((Routes.getLevel().number & 7) != this.getLevel().number)
			{
				// if we are at the border of the region
				// going back

				return From.getBlockFace();
			}
		}
		
		return null;


	}


	protected boolean isRouteConsumer() {
		return Routes.getLevel().number == this.getLevel().number;
	}


	protected final RoutingTable getRoutingTable() {
		return RoutingTable;
	}
	protected final Address getSignAddress() {
		return SignAddress;
	}
	protected final DirectionRegistry getFrom() {
		return From;
	}
	protected CounterInventory getCounter() {
		return counter;
	}
	protected final RoutingTableExchange getRoutes() {
		return Routes;
	}
	protected boolean isTrackNumberProvider() {
		return IsTrackNumberProvider;
	}
	protected final Level getLevel() {
		return Level;
	}
}
