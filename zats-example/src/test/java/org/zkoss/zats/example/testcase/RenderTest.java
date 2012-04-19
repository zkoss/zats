package org.zkoss.zats.example.testcase;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.operation.RenderAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

/**
 * To demonstrate RenderAgent usage.
 * When a listbox's model holds large amount of data, its listcell contains no child component before we render it.
 * The behavior originated from ZK's optimization.  
 * @author Hawk
 *
 */
public class RenderTest {
	@BeforeClass
	public static void init() {
		Zats.init("./src/main/webapp"); // user can load by
													// configuration file
	}

	@AfterClass
	public static void end() {
		Zats.end();
	}

	@After
	public void after() {
		Zats.cleanup();
	}

	
	@Test
	public void testRendererAgent() {
		DesktopAgent desktop = Zats.newClient().connect("/render.zul");

		ComponentAgent listbox = desktop.query("#listbox");
		Label itemData = desktop.query("#itemData").as(Label.class);

		//selecting first item works correctly 
		listbox.getChild(1).as(SelectAgent.class).select();
		Assert.assertEquals("item0", itemData.getValue());
		
		//select a non-rendered item
		listbox.getChild(1000).as(SelectAgent.class).select();
		Assert.assertEquals("", itemData.getValue());
		//get a non-rendered listcell, check it has no child.
		Listcell listcell = listbox.getChild(1000).getChild(0).as(Listcell.class);
		Assert.assertTrue(listcell.getChildren().size()==0);

		listbox.as(RenderAgent.class).render(999, 999);

		listbox.getChild(1000).as(SelectAgent.class).select();
		Assert.assertTrue("item999".equals(itemData.getValue()));
		listcell = listbox.getChild(1000).getChild(0).as(Listcell.class);
		Assert.assertTrue(listcell.getChildren().size()>0);

	}
}
