/* AuUtility.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.zkoss.zats.ZatsException;
import org.zkoss.zats.common.json.JSONArray;
import org.zkoss.zats.common.json.JSONValue;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zk.au.AuResponse;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zsoup.Zsoup;
import org.zkoss.zsoup.nodes.Document;
import org.zkoss.zsoup.nodes.Element;
import org.zkoss.zsoup.select.Elements;

/**
 * A utility for AU.
 * 
 * @author dennis
 * @author jumperchen
 */
public class AuUtility {

	/**
	 * lookup the event target of a component, it look up the component and its
	 * ancient. use this for search the actual target what will receive a event
	 * for a action on a component-agent
	 * <p/>
	 * Currently, i get it by server side directly
	 */
	public static ComponentAgent lookupEventTarget(ComponentAgent c, String evtname) {
		if (c == null)
			return null;
		Component comp = (Component)c.getDelegatee();
		if (Events.isListened(comp, evtname, true)) {
			return c;
		}
		return lookupEventTarget(c.getParent(), evtname);

	}

	static void setEssential(Map<String,Object> data,String key, Object obj){
		setEssential(data,key,obj,false);
	}
	static void setEssential(Map<String,Object> data,String key, Object obj, boolean nullable){
		if(obj==null&&!nullable) throw new AgentException("data of "+key+" is null");
		data.put(key, toSafeJsonObject(obj));
	}

	static void setOptional(Map<String,Object> data,String key, Object obj){
		if(obj==null) return;
		data.put(key, toSafeJsonObject(obj));
	}

	static void setReference(Map<String,Object> data,Component comp){
		if(comp==null) return;
		data.put("reference", comp.getUuid());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static private Object toSafeJsonObject(Object obj){
		if(obj instanceof Set){
			//exception if data is Set
			//>>Unexpected character (n) at position 10.
			//>>	at org.zkoss.json.parser.Yylex.yylex(Yylex.java:610)
			//>>	at org.zkoss.json.parser.JSONParser.nextToken(JSONParser.java:270)
			return new ArrayList((Set)obj);
		}
		return obj;
	}
	
	/**
	 * convert JSON object of AU. response to AuResponse list. 
	 * @param jsonObject AU. response. If null, throw null point exception.
	 * @return list of AuResponse if the format of object is valid, or null if otherwise.
	 */
	public static List<AuResponse> convertToResponses(Map<String, Object> jsonObject) {
		// check argument
		if (jsonObject == null)
			throw new NullPointerException("input object can't be null");
		Object responses = jsonObject.get("rs");
		if (!(responses instanceof List))
			return null;

		// fetch all response
		ArrayList<AuResponse> list = new ArrayList<AuResponse>();
		for (Object response : (List<?>) responses) {
			if (response instanceof List) {
				List<?> resp = (List<?>) response;
				if (resp.size() == 2) {
					// create response
					String cmd = resp.get(0).toString();
					Object data = resp.get(1);
					if (data instanceof List)
						list.add(new AuResponse(cmd, ((List<?>) data).toArray()));
					else
						list.add(new AuResponse(cmd, data));
					continue;
				}
			}
			// format is invalid
			return null;
		}
		return list;
	}
	
	public static Map<String, Object> parseAuResponseFromLayout(String raw) {
		try {
			String zkmxArgs = null;

			// Bug fixed for ZATS-44
			Document doc = Zsoup.parse(new ByteArrayInputStream(raw.getBytes("utf-8")), "UTF-8",
					"", org.zkoss.zsoup.parser.Parser.xhtmlParser());
			Elements scripts = doc.getElementsByTag("script");
			for (Element script : scripts) {
				// fetch arguments of zkmx()
				String text = script.html().replaceAll("[\\n\\r]", "");
				// ZATS-34: layout response might have other client-side scripts
				// using JS parser to fetch argument of zkmx()
				if (text.contains("zkmx(")) {
					zkmxArgs = fetchJavascriptFuntionArguments("zkmx", text);
					if(zkmxArgs != null) {
						break;	// find first
					}
				}
			}
			if (zkmxArgs == null) {
				return null;
			}

			// ZATS-25: filter non-JSON part (i.e. real JS code)
			String json = filterNonJSON(zkmxArgs);
			
			// parse to JSON and wrap to map
			JSONArray layoutCmds = (JSONArray) JSONValue.parseWithException(json);
			if (layoutCmds.size() < 3) {
				return null; // maybe no AU cmd
			}
			JSONArray rawAuCmds = (JSONArray) layoutCmds.get(2); // the 3rd argument are AuCmd -> mount.js -> zkmx()
			JSONArray auCmds = new JSONArray();
			// group up (two items > one AU cmd)
			for (int i = 0; i < rawAuCmds.size(); i += 2) {
				Object cmd = rawAuCmds.get(i);
				Object data = rawAuCmds.get(i + 1);
				// check data type 
				if (!(data instanceof JSONArray)) {
					data = JSONValue.parseWithException(data.toString());
				}
				JSONArray a = new JSONArray();
				a.add(cmd);
				a.add(data);
				auCmds.add(a);
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("rs", auCmds); // compatible with AU response 
			return map;

		} catch (Exception e) {
			throw new ZatsException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * @return an json array text contained specified function's arguments, or null otherwise.
	 */
	public static String fetchJavascriptFuntionArguments(final String funcName, String code) {
		if (funcName == null || code == null) {
			return null;
		}

		// parse js
		Parser parser = new Parser();
		AstRoot root = parser.parse(code, null, 0);

		// find specified function
		final AtomicReference<FunctionCall> target = new AtomicReference<FunctionCall>();
		root.visit(new NodeVisitor() {
			public boolean visit(AstNode node) {
				if (node instanceof FunctionCall) {
					FunctionCall call = (FunctionCall) node;
					node = call.getTarget();
					if (node instanceof Name) {
						Name name = (Name) node;
						if (funcName.equals(name.toSource())) {
							target.set(call);
							return false;
						}
					}
				}
				return true;
			}
		});
		FunctionCall call = target.get();
		if (call == null) {
			return null;
		}

		// fetch arguments and merge to an array text
		int lp = call.getLp();
		int rp = call.getRp();
		if (lp < 0 || rp < 0) {
			return null;
		}
		int start = lp + call.getAbsolutePosition() + 1;
		int end = rp + call.getAbsolutePosition();
		return "[" + code.substring(start, end) + "]"; 
	}
	
	public static String filterNonJSON(String json) {
		// prefix for valid js code
		String prefix = "var tmp = ";
		StringBuilder src = new StringBuilder(prefix).append(json);
		
		// parse js
		Parser parser = new Parser();
		AstRoot root = parser.parse(src.toString(), null, 0);

		// collect functions
		final List<FunctionNode> functions = new ArrayList<FunctionNode>();
		root.visit(new NodeVisitor() {
			public boolean visit(AstNode node) {
				if(node instanceof FunctionNode) {
					functions.add((FunctionNode)node);
				}
				return true;
			}
		});

		// sort and make sure ordered (last to first)
		Collections.sort(functions);
		
		// filter function declaration by replacing functions
		for(int i = functions.size() - 1 ; i >= 0 ; --i) {
			FunctionNode func = functions.get(i);
			int p = func.getAbsolutePosition();
			int len = func.getLength();
			src.replace(p, p + len, "''"); // replace by empty js string literal
		}

		// remove prefix and return result
		return src.substring(prefix.length());
	}
}
