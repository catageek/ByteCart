package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public interface TriggeredIC extends IC {
	public RegistryInput getInput(int index);
	public RegistryOutput getOutput(int index);
	public void addInputRegistry(RegistryInput reg);
	public void addOutputRegistry(RegistryOutput reg);
	public void trigger();
	public boolean isTrain();
	public boolean wasTrain(Block block);
}
