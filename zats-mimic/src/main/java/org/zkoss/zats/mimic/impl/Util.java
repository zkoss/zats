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

import org.zkoss.Version;
import org.zkoss.zats.ZatsException;

/**
 * Utilities for implementation.
 * 
 * @author pao
 */
public class Util {
	
	private static BigInteger zkVersion; // current zk version
	
	static {
		// load current zk version
		try {
			zkVersion = Util.parseVersion(Version.class.getField("UID").get(null).toString());
		} catch (Throwable e) {
			throw new ZatsException("cannot load zk", e);
		}
	}
	
	public static BigInteger getZKVersion() {
		return zkVersion;
	}
	
	public static boolean isZKVersion(int primaryVer){
		byte[] ver = zkVersion.toByteArray();
		if(ver!=null && ver.length>=3){
			return ver[0]==primaryVer;
		}
		return false;
	}
	
	
	public static boolean checkVersion(String startVersion, String endVersion){
		// check version
		// If current isn't between start and end version, ignore this register.
		BigInteger start = "*".equals(startVersion.trim()) ? BigInteger.ZERO
				: Util.parseVersion(startVersion);
		BigInteger end = "*".equals(endVersion.trim()) ? BigInteger
				.valueOf(Long.MAX_VALUE) : Util.parseVersion(endVersion);
		if (start == null || end == null)
			throw new IllegalArgumentException("wrong version format");
		if (zkVersion.compareTo(start) < 0 || zkVersion.compareTo(end) > 0)
			return false;
		return true;
	}
	
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
