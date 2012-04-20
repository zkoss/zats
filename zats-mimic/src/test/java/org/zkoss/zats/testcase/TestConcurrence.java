/* TestConcurrent.java

	Purpose:
		
	Description:
		
	History:
		Apr 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testcase;

import org.junit.Assert;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zul.Label;

/**
 * @author pao
 *
 */
public class TestConcurrence {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable{
		int count = args.length < 1 ? 200 : Integer.parseInt(args[0]);
		String url = args.length < 2 ? "/~./basic/type1.zul" : args[1];
		System.out.println("count: " + count);
		if (count == 0)
			return;
		else if (count > 0) {
			for (int i = 0; i < count; ++i) {
				System.out.println("iteration: " + i);
				run(i, url);
			}
		} else {
			int maxSec = Math.abs(count);
			long start = System.currentTimeMillis();
			long iter = 0;
			while (true) {
				System.out.println("iteration: " + iter);
				run(iter, url);
				long runSec = (System.currentTimeMillis() - start) / 1000L;
				if (runSec >= maxSec)
					break;
				else
					++iter;
			}
		}
	}

	public static void run(long id, String url) throws Throwable{
		String text = id + ": " + System.currentTimeMillis();

		try {
			Zats.init(".");

			Client client = Zats.newClient();
			DesktopAgent desktopAgent = client.connect(url);
			ComponentAgent label = desktopAgent.query("#l1");
			ComponentAgent input = desktopAgent.query("#inp1");
			Assert.assertEquals("", label.as(Label.class).getValue());
			input.type(text);
			Assert.assertEquals(text, label.as(Label.class).getValue());

			Zats.cleanup();
		} catch (Throwable e) {
			throw e;
		} finally {
			Zats.end();
		}
	}
}
