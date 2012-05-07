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

/**
 * @author dennis
 *
 */
public class BookmarkEventDataBuilder implements EventDataBuilder<BookmarkEvent> {

	public Map<String, Object> build(BookmarkEvent evt, Map<String, Object> data) {
		AuUtility.setEssential(data, "", evt.getBookmark());
		return data;
	}
	
	public Class<BookmarkEvent> getEventClass(){
		return BookmarkEvent.class;
	}

}
