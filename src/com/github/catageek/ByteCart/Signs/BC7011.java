package com.github.catageek.ByteCart.Signs;



/**
 * A universal ticket spawner
 */
public class BC7011 extends BC7010 implements Triggable {

	public BC7011(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);

		this.StorageCartAllowed = true;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7010#getName()
	 */
	@Override
	public String getName() {
		return "BC7011";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7010#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return "Storage Goto";
	}
}
