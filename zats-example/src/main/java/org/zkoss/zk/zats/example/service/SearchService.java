/* FakeSearchService.java

	Purpose:
		
	Description:
		
	History:
		2011/10/25 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zk.zats.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 */
public class SearchService {

	List<Item> allItems = new ArrayList<Item>();

	Random r = new Random(System.currentTimeMillis());
	
	public SearchService() {
		allItems.add(new Item("P001-A", "part 001, type A", nextPrice(), nextQuantity()));
		allItems.add(new Item("P001-B", "part 001, type B", nextPrice(), nextQuantity()));
		allItems.add(new Item("P001-C", "part 001, type C", nextPrice(), nextQuantity()));
		allItems.add(new Item("P001-D", "part 001, type D", nextPrice(), nextQuantity()));
		allItems.add(new Item("P001-E", "part 001, type E", nextPrice(), nextQuantity()));

		allItems.add(new Item("P002-A", "part 001, type A", nextPrice(), nextQuantity()));
		allItems.add(new Item("P002-B", "part 001, type B", nextPrice(), nextQuantity()));
		allItems.add(new Item("P002-C", "part 001, type C", nextPrice(), nextQuantity()));
		allItems.add(new Item("P002-D", "part 001, type D", nextPrice(), nextQuantity()));
		allItems.add(new Item("P002-E", "part 001, type E", nextPrice(), nextQuantity()));

		allItems.add(new Item("P003-A", "part 001, type A", nextPrice(), nextQuantity()));
		allItems.add(new Item("P003-B", "part 001, type B", nextPrice(), nextQuantity()));
		allItems.add(new Item("P003-C", "part 001, type C", nextPrice(), nextQuantity()));
		allItems.add(new Item("P003-D", "part 001, type D", nextPrice(), nextQuantity()));
		allItems.add(new Item("P003-E", "part 001, type E", nextPrice(), nextQuantity()));

		allItems.add(new Item("P004-A", "part 001, type A", nextPrice(), nextQuantity()));
		allItems.add(new Item("P004-B", "part 001, type B", nextPrice(), nextQuantity()));
		allItems.add(new Item("P004-C", "part 001, type C", nextPrice(), nextQuantity()));
		allItems.add(new Item("P004-D", "part 001, type D", nextPrice(), nextQuantity()));
		allItems.add(new Item("P004-E", "part 001, type E", nextPrice(), nextQuantity()));
	}

	double nextPrice() {
		return r.nextDouble()*300;
	}
	
	int nextQuantity() {
		return r.nextInt(10);
	}

	public List<Item> search(String fitler) {
		List<Item> items = new ArrayList<Item>();
		for (Item item : allItems) {
			if ((fitler == null || "*".equals(fitler))
					|| item.getName().indexOf(fitler) >= 0) {
				items.add(item);
			}
		}
		return items;
	}

}
