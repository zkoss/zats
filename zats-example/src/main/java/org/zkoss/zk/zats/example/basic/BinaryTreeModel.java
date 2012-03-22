/* BinaryTreeModel.java

	Purpose:
		
	Description:
		
	History:
		2012/3/22 Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zk.zats.example.basic;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.AbstractTreeModel;

/**
 * @author dennis
 *
 */
public class BinaryTreeModel extends AbstractTreeModel{
private ArrayList _tree =null;
	
	/**
	 * Constructor
	 * @param tree the list is contained all data of nodes.
	 */
	public BinaryTreeModel(List tree){
		super(tree.get(0));
		_tree = (ArrayList)tree;
	}
	
	//-- TreeModel --//
	public Object getChild(Object parent, int index) {
		int i = _tree.indexOf(parent) *2 +1 + index;
		if( i>= _tree.size())
			return null;
		else
			return _tree.get(i);
	}
	
	//-- TreeModel --//
	public int getChildCount(Object parent) {
		int count = 0;
		if(getChild(parent,0) != null)
			count++;
		if(getChild(parent,1) != null)
			count++;
		return count;
	}
	
	//-- TreeModel --//
	public boolean isLeaf(Object node) {
		return (getChildCount(node) == 0);
	}

	public int getIndexOfChild(Object parent, Object child) {
		// TODO Auto-generated method stub
		return 0;
	}
}
