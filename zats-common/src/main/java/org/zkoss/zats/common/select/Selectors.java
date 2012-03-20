/* Selectors.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
	// ported from zk 6.0.0 
// original package: org.zkoss.zk.ui.select
package org.zkoss.zats.common.select;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.zkoss.zats.common.select.impl.ComponentIterator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;

/**
 * A collection of selector related utilities.
 * This was porting from ZK 6 in order to provide selector on ZK 5.
 * It don't recommend user to use this class directly.
 * @since 6.0.0
 * @author simonpai
 */
public class Selectors
{

	/**
	 * Returns an Iterable that iterates through all Components matched by the
	 * selector.
	 * @param page the reference page for selector
	 * @param selector the selector string
	 * @return an Iterable of Component
	 */
	public static Iterable<Component> iterable(final Page page, final String selector)
	{
		return new Iterable<Component>()
		{
			public Iterator<Component> iterator()
			{
				return new ComponentIterator(page, selector);
			}
		};
	}

	/**
	 * Returns an Iterable that iterates through all Components matched by the
	 * selector.
	 * @param root the reference component for selector
	 * @param selector the selector string
	 * @return an Iterable of Component
	 */
	public static Iterable<Component> iterable(final Component root, final String selector)
	{
		return new Iterable<Component>()
		{
			public Iterator<Component> iterator()
			{
				return new ComponentIterator(root, selector);
			}
		};
	}

	/**
	 * Returns a list of Components that match the selector.
	 * @param page the reference page for selector
	 * @param selector the selector string
	 * @return a List of Component
	 */
	public static List<Component> find(Page page, String selector)
	{
		return toList(iterable(page, selector));
	}

	/**
	 * Returns a list of Components that match the selector.
	 * @param root the reference component for selector
	 * @param selector the selector string
	 * @return a List of Component
	 */
	public static List<Component> find(Component root, String selector)
	{
		return toList(iterable(root, selector));
	}

	private static <T> List<T> toList(Iterable<T> iterable)
	{
		List<T> result = new ArrayList<T>();
		for(T t : iterable)
			result.add(t);
		return result;
	}

}
