package org.zkoss.zats.example.search;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
/**
 * @author Hawk
 */

@SuppressWarnings("serial")
public class SearchComposer extends SelectorComposer{
	//the search result
	private ListModelList items;

	//the selected item
	private Item selected;
	//UI component
	@Wire
	private Textbox filterBox;
	@Wire
	private Button searchButton;
	@Wire
	private Listbox itemListbox;
	@Wire
	private Groupbox detailBox;
	@Wire
	private Caption detailCaption;
	@Wire
	private Label descriptionLabel;
	@Wire
	private Label priceLabel;
	@Wire
	private Label quantityLabel;
	@Wire
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
	
	@Listen("onClick=button#searchButton")
	public void onClick$searchButton(){
		search();
	}
	
	private void search(){
		items = new ListModelList();
		items.addAll(getSearchService().search(filterBox.getValue()));
		itemListbox.setModel(items);
		detailBox.setVisible(false);
	}
	
	@Listen("onChange=#filterBox")
	public void onChange$filterBox(){
		searchButton.setDisabled(filterBox.getValue().length()==0);
	}
	
	@Listen("onSelect = listbox")
	public void onSelect(){
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
}
