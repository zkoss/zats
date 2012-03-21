package org.zkoss.zk.zats.example;

import static org.zkoss.zats.mimic.Searcher.find;
import static org.junit.Assert.*;


import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

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
		Conversations.open("/todo.zul");

		//add
		find("textbox").as(TypeAgent.class).type("one-item");
//		find("textbox").as(Textbox.class).setValue("abc"); cause IllegalStateException: Components can be accessed only in event listeners
		find("intbox").as(TypeAgent.class).type("3");
		find("datebox").as(TypeAgent.class).type("2012/03/16");
		find("button[label='Add'] ").as(ClickAgent.class).click();
		
		//verify each listcell's label
//		Listitem listitem = find("listbox").getChild(1).as(Listitem.class);
		List cells = find("listbox").getChild(1).as(Listitem.class).getChildren();
		assertEquals("one-item",((Listcell)cells.get(0)).getLabel());
		assertEquals("3",((Listcell)cells.get(1)).getLabel());
		assertEquals("2012/03/16",((Listcell)cells.get(2)).getLabel());
		
		
		//update
		
		//reset
		
		//delete
	}
}
