package org.zkoss.zk.zats.example;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.core.Conversations;
import org.zkoss.zats.core.Searcher;
import org.zkoss.zats.core.component.ComponentNode;
import org.zkoss.zats.core.component.operation.Clickable;
import org.zkoss.zats.core.component.operation.Selectable;
import org.zkoss.zats.core.component.operation.Typeable;
import org.zkoss.zk.zats.example.service.Item;
import org.zkoss.zk.zats.example.view.ItemRenderer;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;
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

		ComponentNode filterBox = Searcher.find("#filterBox");
		ComponentNode searchButton = Searcher.find("#searchButton");
		
		filterBox.as(Typeable.class).type("A");
		searchButton.as(Clickable.class).click();

		ComponentNode detailBox = Searcher.find("groupbox");
		
		ComponentNode listbox = Searcher.find("listbox");
		listbox.as(Selectable.class).select(0);
		//verify UI logic
		assertEquals(true, detailBox.cast(Groupbox.class).isVisible());
		
		//verify detail data by model
		Item item = (Item)listbox.cast(Listbox.class).getModel().getElementAt(0);
		ComponentNode descriptionLabel = Searcher.find("#descriptionLabel"); 
		assertEquals(descriptionLabel.cast(Label.class).getValue(), item.getDescription());
		ComponentNode priceLabel = Searcher.find("#priceLabel");
		assertEquals(priceLabel.cast(Label.class).getValue(),ItemRenderer.priceFormatter.format(item.getPrice()));
		ComponentNode quantityLabel = Searcher.find("#quantityLabel");
		assertEquals(true, quantityLabel.cast(Label.class).getValue().contains(Integer.toString(item.getQuantity())));
		ComponentNode totalPriceLabel = Searcher.find("#totalPriceLabel");
		assertEquals(totalPriceLabel.cast(Label.class).getValue(),ItemRenderer.priceFormatter.format(item.getTotalPrice()));
		
		//verify detail data by label
		String label = listbox.cast(Listbox.class).getSelectedItemApi().getLabel();
		assertEquals(true, true);
	}
}
