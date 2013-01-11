package com.github.catageek.ByteCart.HAL;

import java.util.EmptyStackException;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.IO.InventorySlot;

public final class StackInventory {

	private final Inventory Inventory;
	private final int Top;
	private final int Size;

	public StackInventory(org.bukkit.inventory.Inventory inventory, int top, int size) {
		super();
		Inventory = inventory;
		Top = top;
		Size = size;
	}

	private Inventory getInventory() {
		return Inventory;
	}

	private int getTop() {
		return Top;
	}
	
	private InventorySlot getSlot(int n) {
		return new InventorySlot(this.getInventory(), n);
	}
	
	public boolean empty() {
		return this.getSlot(getTop()).isEmpty();
	}
	
	public void clear() {
		this.getSlot(getTop()).empty();
	}
	
	public int peek() {
		if (empty())
			throw new EmptyStackException();
		return this.getSlot(getTop()).getAmount();
	}
	
	public int pop() {
		if (empty())
			throw new EmptyStackException();
		int i, ret = this.peek();
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : stack pop : popping " + ret);
		for (i = this.getTop(); i < this.bottom(); i++) {
			if (this.getSlot(i+1).isEmpty())
				break;
			this.getSlot(i).setAmount(this.getSlot(i+1).getAmount());
		}
		this.getSlot(i).empty();
		return ret;
	}
	
	public int push(int d) {
		int bottom = this.bottom();
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : stack push : pushing " + d + " on stack");
		if (bottom >= this.getTop() + this.getSize() - 1)
			throw new IndexOutOfBoundsException();
		this.getSlot(bottom+1).empty();
		for (int i = bottom; i > this.getTop();i--) {
			this.getSlot(i).setAmount(this.getSlot(i-1).getAmount());
		}
		this.getSlot(getTop()).setAmount(d);
		return d;
	}
	
	private int bottom() {
		int ret = this.getTop();
		while(! this.getSlot(ret).isEmpty())
			ret++;
		return ret;
	}

	public int getSize() {
		return Size;
	}
	
	

}
