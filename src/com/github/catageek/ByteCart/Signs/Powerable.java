package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import com.github.catageek.ByteCartAPI.HAL.IC;

/**
 * An IC that can be powered should implement this
 */
public interface Powerable extends IC {
	/**
	 * Method called when the IC is powered
	 *
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void power();

}
