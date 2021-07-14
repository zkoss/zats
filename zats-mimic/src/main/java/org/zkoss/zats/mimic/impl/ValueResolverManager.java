/* ValueResolverManager.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zats.mimic.Agent;
import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zk.ui.event.Event;

/**
 * This class maintains a list of {@link ValueResolver}. When it resolves a Agent, it calls each resolver in the list to resolve it.
 * @author dennis
 */
public class ValueResolverManager {
	private static ValueResolverManager instance;
	
	public static synchronized ValueResolverManager getInstance(){
		if(instance==null){
			instance = new ValueResolverManager(); 
		}
		return instance;
	}
	private Map<String, ValueResolver> resolvers = new HashMap<String, ValueResolver>();

	public ValueResolverManager() {
	
		//ComponentAgent resolver
		registerResolver("9.6.0","*", "agent", new ValueResolver(){
			@SuppressWarnings("unchecked")
			public <T> T resolve(Agent agent, Class<T> clazz) {
				if (OperationAgent.class.isAssignableFrom(clazz)) {
					Class<OperationAgent> opc = (Class<OperationAgent>) clazz;
					OperationAgentBuilder<Agent, OperationAgent> builder = OperationAgentManager.getInstance().getBuilder(
							agent.getDelegatee(), opc);
					if (builder != null)
						return (T) builder.getOperation(agent);
				}
				return null;
			}
		});
		//ZK native component resolver
		registerResolver("9.6.0","*", "component", new ValueResolver(){
			@SuppressWarnings("unchecked")
			public <T> T resolve(Agent agent, Class<T> clazz) {
				if (clazz.isInstance(agent.getDelegatee())) {
					return (T) agent.getDelegatee();
				}
				return null;
			}
		});
	}
	
	@SuppressWarnings({ "rawtypes"})
	public void registerResolver(String startVersion, String endVersion, String key, String resolverClazz) {
		if (startVersion == null || endVersion == null || resolverClazz == null)
			throw new IllegalArgumentException();
		
		if(!Util.checkVersion(startVersion,endVersion)) return;
		ValueResolver resolver = null;
		try{
			Class buildClz = Class.forName(resolverClazz);
			resolver = (ValueResolver)buildClz.newInstance();
		}catch(Exception x){
			throw new IllegalArgumentException(x.getMessage(),x);
		}
		
		registerResolver(startVersion,endVersion, key, resolver);
	}

	
	public <T extends Event> 
		void registerResolver(String startVersion, String endVersion, String key, ValueResolver resolver) {
		
		if (startVersion == null || endVersion == null || key == null || resolver==null)
			throw new IllegalArgumentException();

		if(!Util.checkVersion(startVersion,endVersion)) return;
		// ZATS-11: note that, the key can be used for replacing previous one and prevent duplicate handlers
		resolvers.put(key, resolver);
	}
	
	/**
	 * resolve the component agent to a object with registered value resolver
	 */
	public <T> T resolve(Agent agent, Class<T> clazz){
		for (ValueResolver r : resolvers.values()) {
			T obj = r.resolve(agent, clazz);
			if(obj!=null) return obj;
		}
		return null;
	}
}
