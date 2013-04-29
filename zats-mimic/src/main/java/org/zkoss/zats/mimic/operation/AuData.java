/* AuEvent.java

	Purpose:
		
	Description:
		
	History:
		Apr 22, 2013 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zats.common.json.JSONObject;
import org.zkoss.zats.mimic.AgentException;

/**
 * An AU request data class to define custom event and data.
 * @author pao
 * @since 1.1.0
 */
public class AuData {

	private String eventName;
	private Map<String, Object> data = new HashMap<String, Object>();

	public AuData(String eventName) {
		if (eventName == null || eventName.length() <= 0) {
			throw new AgentException("event name can't be null or empty");
		}
		this.eventName = eventName;
	}

	/**
	 * Setting data for event. If key is existed, new value will replace old one. 
	 * @param key can't be null.
	 * @param value a value and type should be one of following: 
	 * {@link org.zkoss.zats.common.json.JSONObject},
	 * 	java.lang.String,
	 * 	java.lang.Number,
	 * 	java.lang.Boolean,
	 * 	null.
	 * @return The AU event itself.
	 */
	public AuData setData(String key, Object value) {
		if (key == null) {
			throw new AgentException("key can't be null.");
		}
		checkType(value);
		data.put(key, value);
		return this;
	}

	/**
	 * Setting data for event. If key is existed, new value will replace old one. 
	 * @param key can't be null.
	 * @param values a value array and element type should be one of following: 
	 * {@link org.zkoss.zats.common.json.JSONObject},
	 * 	java.lang.String,
	 * 	java.lang.Number,
	 * 	java.lang.Boolean,
	 * 	null.
	 * @return The AU event itself.
	 */
	public AuData setData(String key, Object[] values) {
		if (key == null) {
			throw new AgentException("key can't be null.");
		}

		// JSON parser doesn't accept Java array type
		List<Object> list = new ArrayList<Object>();
		for (Object value : values) {
			checkType(value);
			list.add(value);
		}
		data.put(key, list);
		return this;
	}

	private void checkType(Object object) {
		if (!(object instanceof JSONObject || object instanceof String || object instanceof Number
				|| object instanceof Boolean || object == null)) {
			throw new AgentException(
					"object type should be org.zkoss.zats.common.json.JSONObject, java.lang.String, java.lang.Number, java.lang.Boolean or null.");
		}
	}

	public String getName() {
		return eventName;
	}

	public Map<String, Object> getData() {
		return data;
	}
}