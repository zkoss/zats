package org.zkoss.zk.zats.example;

import static org.junit.Assert.assertEquals;
import static org.zkoss.zats.mimic.Searcher.find;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zul.*;

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
		ComponentAgent itemName = find("textbox");
		ComponentAgent priority = find("intbox");
		ComponentAgent date = find("datebox");

		//add
		itemName.as(TypeAgent.class).type("one-item");
		priority.as(TypeAgent.class).type("3");
		date.as(TypeAgent.class).type("2012-03-16");
		find("button[label='Add'] ").as(ClickAgent.class).click();
		
		//verify each listcell's label
		ComponentAgent listbox = find("listbox");
		List cells = listbox.getChild(1).as(Listitem.class).getChildren();
		assertEquals("one-item",((Listcell)cells.get(0)).getLabel());
		assertEquals("3",((Listcell)cells.get(1)).getLabel());
		assertEquals("2012/03/16",((Listcell)cells.get(2)).getLabel());
		
		//update
		listbox.as(SelectAgent.class).select(0);
		//verify select
		assertEquals("one-item",itemName.as(Textbox.class).getValue());
		assertEquals((Integer)3,priority.as(Intbox.class).getValue());
		assertEquals("2012-03-16",date.as(Datebox.class).getRawText());
		//modify the todo item
		itemName.as(TypeAgent.class).type("one-item modified");
		priority.as(TypeAgent.class).type("5");
		find("button[label='Update'] ").as(ClickAgent.class).click();
		assertEquals("one-item modified",((Listcell)cells.get(0)).getLabel());
		assertEquals("5",((Listcell)cells.get(1)).getLabel());
		
		//reset
		listbox.as(SelectAgent.class).select(0);
		assertEquals("one-item modified",((Listcell)cells.get(0)).getLabel());
		find("button[label='Reset'] ").as(ClickAgent.class).click();
		assertEquals("",itemName.as(Textbox.class).getValue());
		assertEquals((Integer)0,priority.as(Intbox.class).getValue());
		assertEquals(true, date.as(Datebox.class).getValue()==null);

		//delete
		assertEquals(2,listbox.getChildren().size());
		listbox.as(SelectAgent.class).select(0);
		find("button[label='Delete'] ").as(ClickAgent.class).click();
		assertEquals(1,listbox.getChildren().size());
		
//		find("textbox").as(Textbox.class).setValue("abc"); cause IllegalStateException: Components can be accessed only in event listeners
	}
}
