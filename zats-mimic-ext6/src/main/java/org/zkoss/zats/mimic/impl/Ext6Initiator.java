/* Initial.java

	Purpose:
		
	Description:
		
	History:
		2012/3/23 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl;

import org.zkoss.bind.Binder;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.operation.DateTypeAgentBuilderZK6;
import org.zkoss.zats.mimic.impl.operation.GenericCheckAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.OperationAgentManager;
import org.zkoss.zats.mimic.impl.operation.TimeTypeAgentBuilderZK6;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Toolbarbutton;

/**
 * @author dennis
 *
 */
public class Ext6Initiator implements WebAppInit{

	public void init(WebApp wapp) throws Exception {
		
		//testcases and mimic server is in the same vm. 
		//so it is ok to register builder by webapp init
		
		// operation
		OperationAgentManager.registerBuilder("6.0.0", "*", Toolbarbutton.class, CheckAgent.class,
				new GenericCheckAgentBuilder());
		OperationAgentManager.registerBuilder("6.0.0", "*", Datebox.class, TypeAgent.class,
				new DateTypeAgentBuilderZK6());
		OperationAgentManager.registerBuilder("6.0.0", "*", Timebox.class, TypeAgent.class,
				new TimeTypeAgentBuilderZK6());
		
		//event data
//		EventDataManager.registerBuilder("6.0.0","*", RenderEvent.class, new EventDataBuilder(){
//			public Map<String, Object> build(Event event,Map<String,Object> data) {
//				RenderEvent evt = (RenderEvent)event;
//				setEssential(data,"items",evt.getItems());//id set of items
//				return data;
//			}});
		
		
		// resolvers
		//resolve view model
		ValueResolverManager.registerResolver("6.0.0","*",new ValueResolver(){
			@SuppressWarnings("unchecked")
			public <T> T resolve(ComponentAgent agent, Class<T> clazz) {
				Object binder = agent.getAttribute(BinderImpl.BINDER);
				if(binder != null && binder instanceof Binder){
					Object vm = ((Binder)binder).getViewModel();
					if (vm!=null && clazz.isInstance(vm)) {
						return (T)vm;
					}
				}
				return null;
			}
		});
	}
//	
//	@SuppressWarnings("unchecked")
//	static private Object toSafeJsonObject(Object obj){
//		if(obj instanceof Set){
//			//exception if data is Set
//			//>>Unexpected character (n) at position 10.
//			//>>	at org.zkoss.json.parser.Yylex.yylex(Yylex.java:610)
//			//>>	at org.zkoss.json.parser.JSONParser.nextToken(JSONParser.java:270)
//			return new ArrayList((Set)obj);
//		}
//		return obj;
//	}
//	
//	static private void setEssential(Map<String,Object> data,String key, Object obj){
//		if(obj==null) throw new ConversationException("data of "+key+" is null");
//		data.put(key, toSafeJsonObject(obj));
//	}
//	
//	static private void setOptional(Map<String,Object> data,String key, Object obj){
//		if(obj==null) return;
//		data.put(key, toSafeJsonObject(obj));
//	}
//
//	static private void setReference(Map<String,Object> data,Component comp){
//		if(comp==null) return;
//		data.put("reference", comp.getUuid());
//	}

}
