/* Node.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.node;

import java.util.Map;
import org.zkoss.zats.mimic.Conversation;

/**
 * A interface represented a tree node from ZUML structure.
 * 
 * @author pao
 */
public interface Node {
	/**
	 * get ID. of this node.
	 * 
	 * @return ID or null if it hasn't.
	 */
	String getId();

	/**
	 * get type name of this node.
	 * 
	 * @return type name
	 */
	String getType();

	/**
	 * get attribute by specify name.
	 * 
	 * @param name
	 *            attribute name.
	 * @return attribute value or null if not found or otherwise.
	 */
	Object getAttribute(String name);

	/**
	 * get all attributes of this node.
	 * 
	 * @return a map of attributes.
	 */
	Map<String, Object> getAttributes();

	/**
	 * get conversation this node belonged to.
	 * 
	 * @return conversation
	 */
	Conversation getConversation();
}
