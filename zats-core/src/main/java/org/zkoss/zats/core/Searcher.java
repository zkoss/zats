package org.zkoss.zats.core;

import java.util.List;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.Node;

/**
 * A tool for searching components.
 * @author pao
 */
public class Searcher
{
	/**
	 * find components matched specify selector.
	 * @param root the reference node for selector (it could be Desktop or Page).
	 * @param selector the selector string.
	 * @return a list contained matched components.
	 */
	public static List<ComponentNode> findAll(Node root, String selector)
	{
		return null;
	}

	/**
	 * find first component matched specify selector.
	 * @param root the reference node for selector (it could be Desktop or Page).
	 * @param selector the selector string.
	 * @return matched component or null if not found.
	 */
	public static ComponentNode find(Node root, String selector)
	{
		return null;
	}
}
