package org.zkoss.zk.zats.example;

import static org.zkoss.zats.mimic.Searcher.find;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.operation.Clickable;
import org.zkoss.zats.mimic.operation.Typeable;
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
		find("textbox").as(Typeable.class).type("one-item");
//		find("textbox").as(Textbox.class).setValue("abc"); cause IllegalStateException: Components can be accessed only in event listeners
		find("intbox").as(Typeable.class).type("3");
		find("datebox").as(Typeable.class).type("2012/03/16");
		find("button[label = 'Add'] ").as(Clickable.class).click();
		
		//verify each listcell's label
		Listitem listitem = find("listbox").getChild(1).as(Listitem.class);
		
		
		
		//update
		
		//reset
		
		//delete
	}
}
