/* EventDataManager.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.au.EventDataBuilder;
import org.zkoss.zats.mimic.impl.operation.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.OperationAgentManager;
import org.zkoss.zats.mimic.operation.OperationAgent;
import org.zkoss.zk.ui.event.Event;

/**
 * @author dennis
 *
 */
public class ValueResolverManager {

	private static List<ValueResolver> resolvers = new ArrayList<ValueResolver>();

	static {
		//resolve agent
		registerResolver("5.0.0","*",new ValueResolver(){
			@SuppressWarnings("unchecked")
			public <T> T resolve(ComponentAgent agent, Class<T> clazz) {
				if (OperationAgent.class.isAssignableFrom(clazz)) {
					Class<OperationAgent> opc = (Class<OperationAgent>) clazz;
					OperationAgentBuilder<OperationAgent> builder = OperationAgentManager.getBuilder(
							agent.getComponent(), opc);
					if (builder != null)
						return (T) builder.getOperation(agent);
				}
				return null;
			}
		});
		//resolver comp
		registerResolver("5.0.0","*",new ValueResolver(){
			@SuppressWarnings("unchecked")
			public <T> T resolve(ComponentAgent agent, Class<T> clazz) {
				if (clazz.isInstance(agent.getComponent())) {
					return (T) agent.getComponent();
				}
				return null;
			}
		});
	}
	
	@SuppressWarnings({ "rawtypes"})
	public static  
		void registerResolver(String startVersion, String endVersion, String resolverClazz) {
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
		
		registerResolver(startVersion,endVersion,resolver);
	}

	
	public static <T extends Event> 
		void registerResolver(String startVersion, String endVersion, ValueResolver resolver) {
		
		if (startVersion == null || endVersion == null || resolver==null)
			throw new IllegalArgumentException();

		if(!Util.checkVersion(startVersion,endVersion)) return;
		resolvers.add(resolver);
	}
	
	/**
	 * resolve the component agent to a object by registered value resolver
	 */
	public static <T> T resolve(ComponentAgent agent, Class<T> clazz){
		for(ValueResolver r:resolvers){
			T obj = r.resolve(agent, clazz);
			if(obj!=null) return obj;
		}
		return null;
	}
}
