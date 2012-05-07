/* BookmarkEventDataBuilder.java

	Purpose:
		
	Description:
		
	History:
		2012/5/7 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.mimic.impl.au;

import java.util.Map;

import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zk.ui.event.Event;

/**
 * @author dennis
 *
 */
public class BookmarkEventDataBuilder implements EventDataBuilder {

	public Map<String, Object> build(Event event, Map<String, Object> data) {
		BookmarkEvent evt = (BookmarkEvent) event;
		AuUtility.setEssential(data, "", evt.getBookmark());
		return data;
	}

}
