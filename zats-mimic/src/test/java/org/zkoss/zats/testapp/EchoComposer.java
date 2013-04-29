/* EchoComposer.java

	Purpose:
		
	Description:
		
	History:
		Apr 26, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.testapp;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;

/**
 * @author pao
 */
public class EchoComposer extends GenericForwardComposer {
	private static final long serialVersionUID = 1L;
	private Component comp;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.comp = comp;
		Events.echoEvent("onFoo", comp, "Bar");
		Events.echoEvent("onFoo2", comp, "Bar2");
	}
	
	public void onFoo(Event event) {
		Label label = new Label(event.getData().toString());
		label.setId("lblX");
		comp.appendChild(label);
	}
	public void onFoo2(Event event) {
		Label label = new Label(event.getData().toString());
		label.setId("lblY");
		comp.appendChild(label);
	}
}
