package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.example.search.Item;
import org.zkoss.zats.example.search.ItemRenderer;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;

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
		Conversations.closeAll();
	}

	@Test
	public void test() {
		DesktopAgent desktop = Conversations.open().connect("/search.zul");

		ComponentAgent detailBox = desktop.query("groupbox");
		ComponentAgent listbox = desktop.query("listbox");
		desktop.queryAll("listbox > listitem").get(0).as(SelectAgent.class).select();
		
		//verify UI logic
		assertEquals(true, detailBox.as(Groupbox.class).isVisible());
		//select an item & verify its detail
		//verify detail data by model
		Item item = (Item)listbox.as(Listbox.class).getModel().getElementAt(0);
		Label descriptionLabel = desktop.query("#descriptionLabel").as(Label.class); 
		assertEquals(descriptionLabel.getValue(), item.getDescription());
		Label priceLabel = desktop.query("#priceLabel").as(Label.class);
		assertEquals(priceLabel.getValue(),ItemRenderer.priceFormatter.format(item.getPrice()));
		Label quantityLabel = desktop.query("#quantityLabel").as(Label.class);
		assertEquals(true, quantityLabel.getValue().contains(Integer.toString(item.getQuantity())));
		Label totalPriceLabel = desktop.query("#totalPriceLabel").as(Label.class);
		assertEquals(totalPriceLabel.getValue(),ItemRenderer.priceFormatter.format(item.getTotalPrice()));
		
		//verify detail data by label
		Caption detailCaption = desktop.query("#detailCaption").as(Caption.class);
		List<ComponentAgent> cellNodes = listbox.getChild(1).getChildren();
		assertEquals(detailCaption.getLabel(), cellNodes.get(0).as(Listcell.class).getLabel());
		assertEquals(priceLabel.getValue(),cellNodes.get(1).as(Listcell.class).getLabel());
		assertEquals(quantityLabel.getValue(),cellNodes.get(2).as(Listcell.class).getLabel());
		
		//search & verify result
		ComponentAgent filterBox = desktop.query("#filterBox");
		ComponentAgent searchButton = desktop.query("#searchButton");
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
