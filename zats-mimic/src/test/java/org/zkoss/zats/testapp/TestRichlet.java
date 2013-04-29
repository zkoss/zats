/* TestRichlet.java

	Purpose:
		
	Description:
		
	History:
		Jun 11, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testapp;

import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

/**
 * @author pao
 */
public class TestRichlet extends GenericRichlet {

	public void service(Page page) throws Exception {
		page.setTitle("Test ZATS Mimic on Richlet");

		Vbox layout = new Vbox();
		layout.setPage(page);

		final Label msg = new Label("Hello world!");
		msg.setId("msg");
		msg.setParent(layout);

		Button btn = new Button("go");
		btn.setId("btn");
		btn.setParent(layout);
		btn.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				msg.setValue("Hello ZK!");
			}
		});
	}
}
