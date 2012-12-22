package com.github.catageek.ByteCart.EventManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.HAL.IC;
import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.HAL.RegistryOutput;

public interface TriggeredIC extends IC {
	public RegistryInput getInput(int index);
	public RegistryOutput getOutput(int index);
	public void addInputRegistry(RegistryInput reg);
	public void addOutputRegistry(RegistryOutput reg);
	public void trigger();
	public boolean isTrain();
	public boolean wasTrain(Location loc);
}
