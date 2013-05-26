package com.github.catageek.ByteCart.Signs;



public class BC7011 extends BC7010 implements Triggable {

	public BC7011(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);

		this.StorageCartAllowed = true;
	}

	@Override
	public String getName() {
		return "BC7011";
	}

	@Override
	public String getFriendlyName() {
		return "Storage Goto";
	}
}
