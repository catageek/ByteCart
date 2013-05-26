package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import com.github.catageek.ByteCart.HAL.IC;

public interface Powerable extends IC {
	public void power() throws ClassNotFoundException, IOException;

}
