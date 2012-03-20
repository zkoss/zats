/* Util.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl;

import java.io.Closeable;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for implementation.
 * 
 * @author pao
 */
public class Util {
	/**
	 * close resource without any exception.
	 * 
	 * @param r
	 *            resource. If null, do nothing.
	 */
	public static void close(Closeable r) {
		try {
			r.close();
		} catch (Throwable e) {
		}
	}

	/**
	 * parse zk version to uniform number for compare. This method doesn't
	 * recognize beta or release candidate version.
	 * 
	 * @param version
	 *            zk version string.
	 * @return uniform number or null if argument is illegal.
	 */
	public static BigInteger parseVersion(final String version) {
		if (version == null || version.length() <= 0)
			return null;
		// parse string into byte array
		Matcher m = Pattern.compile(
				"\\s*(\\d+)\\.(\\d+)\\.(\\d+)\\.?(\\d*)\\s*").matcher(version);
		if (!m.matches())
			return null;
		byte[] raw = new byte[4];
		for (int i = 0; i < m.groupCount(); ++i)
			raw[i] = m.group(i + 1).length() > 0 ? Byte.parseByte(m
					.group(i + 1)) : 0;
		// create extended BigInteger
		// Because raw data is big-endian, major number is most significant.
		return new BigInteger(raw) {
			private static final long serialVersionUID = 2712388085709130462L;

			public String toString() {
				return version;
			}
		};
	}
}
