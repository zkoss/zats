/* BindValueResolver.java

	Purpose:
		
	Description:
		
	History:
		2012/5/10 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import org.zkoss.bind.Binder;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.zats.mimic.Agent;
import org.zkoss.zats.mimic.ComponentAgent;

/**
 * @author dennis
 *
 */
public class BindValueResolver implements ValueResolver{
	@SuppressWarnings("unchecked")
	public <T> T resolve(Agent agent, Class<T> clazz) {
		if(agent instanceof ComponentAgent){
			Object binder = ((ComponentAgent)agent).getAttribute(BinderImpl.BINDER);
			if(binder != null && binder instanceof Binder){
				Object vm = ((Binder)binder).getViewModel();
				if (vm!=null && clazz.isInstance(vm)) {
					return (T)vm;
				}
			}
		}
		return null;
	}
}
