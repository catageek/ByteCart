package com.github.catageek.ByteCart;

public class BC7000 extends BC7001 implements TriggeredIC, PoweredIC {

	public BC7000(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC7000";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = this.Permission + this.Name;
	}

	@Override
	public void trigger() {
		// if this is a cart in a train
		if (this.getVehicle() != null ) {
			if (!this.isTrain() && this.wasTrain(this.getLocation())) {
				ByteCart.myPlugin.getIsTrainManager().getMap().ping(this.getLocation());

				return;
			}

			if (this.isTrain()) {
				this.setWasTrain(this.getLocation(), true);
			}
		}

		super.trigger();
	}

}
