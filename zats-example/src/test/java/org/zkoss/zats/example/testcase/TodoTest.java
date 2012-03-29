package org.zkoss.zats.example.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.zkoss.zats.mimic.Conversations.query;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Textbox;

public class TodoTest {
	@BeforeClass
	public static void init() {
		Conversations.start("./src/main/webapp"); 
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
		//visit the target page
		Conversations.open("/todo.zul");

		//find components
		ComponentAgent itemName = query("textbox");
		ComponentAgent priority = query("intbox");
		ComponentAgent date = query("datebox");

		//add
		//itemName.as(TypeAgent.class).type("one-item");
		itemName.type("one-item");
		priority.type("3");
		date.type("2012-03-16");
		query("button[label='Add']").click();
		
		//verify each listcell's label
		ComponentAgent listbox = query("listbox");
		List<ComponentAgent> cells = listbox.queryAll("listitem").get(0).getChildren();
		assertEquals("one-item",cells.get(0).as(Listcell.class).getLabel());
		assertEquals("3",cells.get(1).as(Listcell.class).getLabel());
		assertEquals("2012/03/16",cells.get(2).as(Listcell.class).getLabel());
		
		//update
		listbox.select(0);
		//verify selected
		assertEquals("one-item",itemName.as(Textbox.class).getValue());
		assertEquals((Integer)3,priority.as(Intbox.class).getValue());
		assertEquals("2012-03-16",date.as(Datebox.class).getRawText());
		//modify the todo item
		itemName.type("one-item modified");
		priority.type("5");
		query("button[label='Update']").click();
		//retrieve Listitem again to verify it
		cells = listbox.queryAll("listitem").get(0).getChildren();
		assertEquals("one-item modified",cells.get(0).as(Listcell.class).getLabel());
		assertEquals("5",cells.get(1).as(Listcell.class).getLabel());
		
		//reset
		listbox.select(0);
		assertNotNull(itemName.as(Textbox.class).getValue());
		query("button[label='Reset']").click();
		assertEquals("",itemName.as(Textbox.class).getValue());
		assertEquals((Integer)0,priority.as(Intbox.class).getValue());
		assertEquals(true, date.as(Datebox.class).getValue()==null);

		//delete
		assertEquals(2,listbox.getChildren().size());
		listbox.select(0);
		query("button[label='Delete']").click();
		assertEquals(1,listbox.getChildren().size());
		
		//The next line causes IllegalStateException: Components can be accessed only in event listeners
		//find("textbox").as(Textbox.class).setValue("abc"); 
	}
}
