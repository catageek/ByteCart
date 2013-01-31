package Event;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VehiclePreRouteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    private final Vehicle vehicle;
    private final int ring_in, ring_out;

	public VehiclePreRouteEvent(Vehicle vehicle, int ring_out, int ring_in) {
		super();
		this.vehicle = vehicle;
		this.ring_in = ring_in;
		this.ring_out = ring_out;
	}

	public HandlerList getHandlers() {
        return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public final Vehicle getVehicle() {
		return vehicle;
	}
	
	public final int fromRing() {
		return ring_out;
	}
	
	public final int toRing() {
		return ring_in;
	}

}
