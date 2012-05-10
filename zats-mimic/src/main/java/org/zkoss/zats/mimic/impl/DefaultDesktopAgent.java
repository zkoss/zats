/* DefaultDesktopAgent.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.PageAgent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
/**
 * 
 * @author dennis
 *
 */
public class DefaultDesktopAgent implements DesktopAgent {
	private Client client;
	private Desktop desktop;

	public DefaultDesktopAgent(Client client, Desktop desktop) {
		this.client = client;
		this.desktop = desktop;
	}

	public String getId() {
		return desktop.getId();
	}

	public String getType() {
		return "desktop";
	}

	public Object getAttribute(String name) {
		return desktop.getAttribute(name);
	}

	public Client getClient() {
		return client;
	}

	public List<PageAgent> getPages() {
		List<PageAgent> agents = new ArrayList<PageAgent>();
		Iterator<?> iter = desktop.getPages().iterator();
		while (iter.hasNext())
			agents.add(new DefaultPageAgent(this, (Page) iter.next()));
		return agents;
	}

	public Desktop getDesktop() {
		return desktop;
	}

	@Override
	public int hashCode() {
		return desktop.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return desktop.equals(obj);
	}
	public Object getDelegatee() {
		return desktop;
	}
	
	public String toString(){
		return new StringBuilder().append(getClass().getSimpleName())
			.append("@").append(Integer.toHexString(System.identityHashCode(this)))
			.append("[").append(desktop.toString()).append("]")
			.toString();
	}

	public ComponentAgent query(String selector) {
		return Searcher.find(this, selector);
	}

	public List<ComponentAgent> queryAll(String selector) {
		return Searcher.findAll(this, selector);
	}
	
	public void destroy(){
		((ClientCtrl)getClient()).destroy(this);
	}
	
	public <T> T as(Class<T> clazz) {
		T obj = ValueResolverManager.resolve(this, clazz);
		if(obj!=null) return obj;
		throw new AgentException("cannot resolve " + clazz.getName() +" for "+ getType());
	}

	public <T> boolean is(Class<T> clazz) {
		T obj = ValueResolverManager.resolve(this, clazz);
		return obj!=null;
	}
	
}
