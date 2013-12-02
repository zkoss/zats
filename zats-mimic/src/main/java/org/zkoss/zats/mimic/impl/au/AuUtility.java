/* AuUtility.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.au;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.NodeVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.zkoss.zats.ZatsException;
import org.zkoss.zats.common.json.JSONArray;
import org.zkoss.zats.common.json.JSONValue;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zk.au.AuResponse;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;

/**
 * A utility for AU.
 * 
 * @author dennis
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
			String json = null;

			// parse <scrpit> from XHTML by SAX
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver() {

				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					return new InputSource(new StringReader(""));
				}
			});
			Document doc = builder.parse(new ByteArrayInputStream(raw.getBytes("utf-8")));
			NodeList scripts = doc.getElementsByTagName("script");
			for (int i = 0; i < scripts.getLength(); ++i) {
				Element script = (Element) scripts.item(i);
				// fetch arguments of zkmx()
				String text = script.getTextContent().replaceAll("[\\n\\r]", "");
				int start = text.indexOf("zkmx(");
				if (start >= 0) {
					int end = text.lastIndexOf(");");
					if (end >= 0) {
						String parameters = text.substring(start + 5, end);
						json = "[" + parameters + "]"; // convert to JSON array
					}
				}
			}
			if (json == null) {
				return null;
			}

			// ZATS-25: filter non-JSON part (i.e. real JS code)
			json = filterNonJSON(json);
			
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
