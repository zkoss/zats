package org.zkoss.zats.example.testcase.operation;

import junit.framework.Assert;

import org.junit.*;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
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
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("./src/main/webapp");

	@Rule
	public AutoClient autoClient = env.autoClient();

	@Test
	public void testAgent() {
		DesktopAgent desktop = autoClient.connect("/render.zul");

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
