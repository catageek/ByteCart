package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.Location;
import com.github.catageek.ByteCart.HAL.IC;
import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.HAL.RegistryOutput;

public interface Triggable extends IC {
	public RegistryInput getInput(int index);
	public RegistryOutput getOutput(int index);
	public void addInputRegistry(RegistryInput reg);
	public void addOutputRegistry(RegistryOutput reg);
	public void trigger() throws ClassNotFoundException, IOException;
	public boolean isTrain();
	public boolean wasTrain(Location loc);
	
	/**
	 * Tell if the lever is negated
	 * @return true if it is negated
	 */
	public boolean isLeverReversed();
}
