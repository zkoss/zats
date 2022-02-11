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
import java.util.Random;
import java.util.logging.Logger;

import org.zkoss.Version;
import org.zkoss.zats.ZatsException;

/**
 * Utilities for implementation.
 * @author pao
 */
public class Util {
	private static Logger logger = Logger.getLogger(Util.class.getName());
	private static BigInteger zkVersion; // current zk version

	static {
		// load current zk version
		try {
			String version = Version.class.getField("UID").get(null).toString();
			zkVersion = Util.parseVersion(version);
			if (zkVersion == null) {
				throw new Exception("failed to parse ZK version string: " + version);
			}
		} catch (Throwable e) {
			throw new ZatsException("cannot load zk", e);
		}
	}

	public static BigInteger getZKVersion() {
		return zkVersion;
	}

	public static boolean isZKVersion(int primaryVer) {
		byte[] ver = zkVersion.toByteArray();
		if (ver != null && ver.length >= 3) {
			return ver[0] == primaryVer;
		}
		return false;
	}

	public static boolean hasClass(String className) {
		try {
			try {
				Util.class.getClassLoader().loadClass(className);
				return true;
			} catch (ClassNotFoundException x) {
				return false;
			}
		} catch (Throwable x) {
			logger.warning(x.getMessage());
		}
		return false;
	}

	/**
	 * check the given version range is match current ZK version or not.
	 * it accepts wildcard "*". e.g * or 5.0.* or 6.*.* 
	 */
	public static boolean checkVersion(String startVersion, String endVersion) {
		// check version
		// If current isn't between start and end version, ignore this register.
		BigInteger start = "*".equals(startVersion.trim()) ? BigInteger.ZERO
				: Util.parseVersion(startVersion.replaceAll("[*]", "0"));
		BigInteger end = "*".equals(endVersion.trim()) ? BigInteger.valueOf(Long.MAX_VALUE)
				: Util.parseVersion(endVersion.replaceAll("[*]", String.valueOf(Byte.MAX_VALUE)));
		if (start == null || end == null)
			throw new IllegalArgumentException("wrong version format");
		if (zkVersion.compareTo(start) < 0 || zkVersion.compareTo(end) > 0)
			return false;
		return true;
	}

	/**
	 * close resource without any exception.
	 * @param r resource. If null, do nothing.
	 */
	public static void close(Closeable r) {
		try {
			r.close();
		} catch (Throwable e) {
		}
	}

	/**
	 * parse ZK version to uniform number for compare.
	 * the method design for normal version string, but it will still parse as long as possible.
	 * @param version ZK version string.
	 * @return uniform number or null if failed to parse.
	 */
	public static BigInteger parseVersion(final String version) {
		if (version == null || version.length() <= 0)
			return null;

		// check version and show some warning 
		if (!version.matches("^\\s*\\d+(?:\\.\\d+)*\\s*$")) {
			logger.warning("not a normal ZK version: <" + version + ">");
		}

		// parse string into byte array
		byte[] raw = new byte[4]; // 
		String[] tokens = version.trim().split("[^\\d\\w]+"); // it remain combined digital and char. terms
		int length = Math.min(raw.length, tokens.length);
		for (int i = 0; i < length; ++i) {
			try {
				raw[i] = Byte.parseByte(tokens[i]);
			} catch (NumberFormatException e) {
				logger.warning("unrecognized part when parsing version: <" + tokens[i] + ">");
			}
		}

		// major version term must be existed
		if (raw[0] <= 0) {
			return null;
		}

		// create extended BigInteger
		// Because raw data is big-endian, major number is most significant.
		return new BigInteger(raw) {
			private static final long serialVersionUID = 2712388085709130462L;

			public String toString() {
				return version;
			}
		};
	}

	public static String generateRandomString() {
		return "zats-" + Integer.toHexString(System.identityHashCode(Util.class))
				+ new BigInteger(64, new Random()).abs().toString(36);
	}
}
