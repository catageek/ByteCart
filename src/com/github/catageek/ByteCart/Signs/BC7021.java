package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;

public final class BC7021 extends BC7020 implements Triggable {

	public BC7021(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC7021";
		this.FriendlyName = "Train head";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = this.Permission + this.Name;
	}
	
	protected void actionWagon() {
		this.getOutput(0).setAmount(0);	// deactivate levers
	}


}
