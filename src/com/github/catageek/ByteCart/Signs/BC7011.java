package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;


public final class BC7011 extends BC7010 implements TriggeredSign {

	public BC7011(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);

		this.Name = "BC7011";
		this.FriendlyName = "Storage Goto";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = this.Permission + this.Name;


		this.StorageCartAllowed = true;
	}

	

}
