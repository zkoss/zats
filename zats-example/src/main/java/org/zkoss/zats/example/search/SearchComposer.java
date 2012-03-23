/* SearchVM.java

	Purpose:
		
	Description:
		
	History:
		2011/10/25 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.example.search;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
/**
 * An implementation in MVP (Model - View - Presenter)
 * manipulate UI components' presentation by accessing them directly.
 * Comparing to MVVM:
 * Pros:
 * no need getter & setter.
 * 
 * Cons:
 * declare variable for each UI component to manipulate.
 * view formating logic (ex: converter) is hard to reuse.
 * Not do unit test
 * 
 * @author Hawk
 */

@SuppressWarnings("serial")
public class SearchComposer extends GenericForwardComposer{
	//the search result
	private ListModelList items;

	//the selected item
	private Item selected;
	//UI component
	private Textbox filterBox;
	private Button searchButton;
	private Listbox itemListbox;
	private Groupbox detailBox;
	private Caption detailCaption;
	private Label descriptionLabel;
	private Label priceLabel;
	private Label quantityLabel;
	private Label totalPriceLabel;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		search();
		itemListbox.setModel(items);
		itemListbox.setItemRenderer(new ItemRenderer());
	}
	
	protected SearchService getSearchService(){
		return new SearchService();
	}
	
	public void onClick$searchButton(){
		search();
	}
	
	private void search(){
		items = new ListModelList();
		items.addAll(getSearchService().search(filterBox.getValue()));
		itemListbox.setModel(items);
		detailBox.setVisible(false);
	}
	
	public void onChange$filterBox(){
		searchButton.setDisabled(filterBox.getValue().length()==0);
	}
	public void onSelect$itemListbox(){
		selected = (Item)items.get(itemListbox.getSelectedIndex());
		//display item detail
		detailBox.setVisible(true);
		detailCaption.setLabel(selected.getName());
		descriptionLabel.setValue(selected.getDescription());
		priceLabel.setValue(ItemRenderer.priceFormatter.format(selected.getPrice()));
		quantityLabel.setValue(Integer.toString(selected.getQuantity()));
		quantityLabel.setSclass(selected.getQuantity()<3?"red":"");
		totalPriceLabel.setValue(ItemRenderer.priceFormatter.format(selected.getTotalPrice()));
	}
	
	public void onSelect$cbox(){
		System.out.println("select");
	}
}
