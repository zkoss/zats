package org.zkoss.zk.zats.example;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zk.zats.example.service.Item;
import org.zkoss.zk.zats.example.view.ItemRenderer;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.api.Listbox;

public class SearchTest {
	@BeforeClass
	public static void init() {
		Conversations.start("./src/main/webapp"); // user can load by
													// configuration file
	}

	@AfterClass
	public static void end() {
		Conversations.stop();
	}

	@After
	public void after() {
		Conversations.clean();
	}

	@Test
	public void test() {
		Conversations.open("/search.zul");

		ComponentAgent detailBox = Searcher.find("groupbox");
		ComponentAgent listbox = Searcher.find("listbox");
		listbox.as(SelectAgent.class).select(0);
		//verify UI logic
		assertEquals(true, detailBox.as(Groupbox.class).isVisible());
		//select an item & verify its detail
		//verify detail data by model
		Item item = (Item)listbox.as(Listbox.class).getModel().getElementAt(0);
		Label descriptionLabel = Searcher.find("#descriptionLabel").as(Label.class); 
		assertEquals(descriptionLabel.getValue(), item.getDescription());
		Label priceLabel = Searcher.find("#priceLabel").as(Label.class);
		assertEquals(priceLabel.getValue(),ItemRenderer.priceFormatter.format(item.getPrice()));
		Label quantityLabel = Searcher.find("#quantityLabel").as(Label.class);
		assertEquals(true, quantityLabel.getValue().contains(Integer.toString(item.getQuantity())));
		Label totalPriceLabel = Searcher.find("#totalPriceLabel").as(Label.class);
		assertEquals(totalPriceLabel.getValue(),ItemRenderer.priceFormatter.format(item.getTotalPrice()));
		
		//verify detail data by label
		Caption detailCaption = Searcher.find("#detailCaption").as(Caption.class);
		List<ComponentAgent> cellNodes = listbox.getChild(1).getChildren();
		assertEquals(detailCaption.getLabel(), cellNodes.get(0).as(Listcell.class).getLabel());
		assertEquals(priceLabel.getValue(),cellNodes.get(1).as(Listcell.class).getLabel());
		assertEquals(quantityLabel.getValue(),cellNodes.get(2).as(Listcell.class).getLabel());
		
		//search & verify result
		ComponentAgent filterBox = Searcher.find("#filterBox");
		ComponentAgent searchButton = Searcher.find("#searchButton");
		final String KEYWORD = "A";
		filterBox.as(TypeAgent.class).type(KEYWORD);
		searchButton.as(ClickAgent.class).click();
		List<ComponentAgent> nodes = listbox.getChildren();
		//skip listheader
		//verify name column
		for (int i=1 ; i <nodes.size() ; i++){
			Listcell cell = nodes.get(i).getChild(0).as(Listcell.class);
			assertEquals(true, cell.getLabel().contains(KEYWORD));
		}
	}
}
