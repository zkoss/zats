package org.zkoss.zats.example.testcase;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.operation.CheckAgent;
import org.zkoss.zats.mimic.operation.MultipleSelectAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

/**
 * To demonstrate MultipleSelectAgent usage.
 * @author Hawk
 *
 */
public class MultipleSelectTest {
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
	public void testAgent() {
		DesktopAgent desktopAgent = Zats.newClient().connect("/multiple-select.zul");

		Label msg = desktopAgent.query("#msg").as(Label.class);
		Assert.assertEquals("", msg.getValue());

		ComponentAgent listbox = desktopAgent.query("#lb");
		Assert.assertEquals(4, listbox.as(Listbox.class).getChildren().size()); // include header
		List<ComponentAgent> items = listbox.queryAll("listitem");

		// listbox multiple selection
		items.get(0).as(MultipleSelectAgent.class).select();
		items.get(1).as(MultipleSelectAgent.class).select();
		items.get(2).as(MultipleSelectAgent.class).select();
		Assert.assertEquals("[i0, i1, i2]", msg.getValue());
		Assert.assertEquals(3, listbox.as(Listbox.class).getSelectedCount());

		items.get(1).as(MultipleSelectAgent.class).deselect();
		Assert.assertEquals("[i0, i2]", msg.getValue());
		Assert.assertEquals(2, listbox.as(Listbox.class).getSelectedCount());
		items.get(0).as(MultipleSelectAgent.class).deselect();
		Assert.assertEquals("[i2]", msg.getValue());
		Assert.assertEquals(1, listbox.as(Listbox.class).getSelectedCount());

		items.get(2).as(MultipleSelectAgent.class).deselect(); // should happen nothing
		Assert.assertEquals("[]", msg.getValue());
		Assert.assertEquals(0, listbox.as(Listbox.class).getSelectedCount());

		//single select in multiple selection mode
		String[] values = { "[i0]", "[i1]", "[i2]" };
		for (int i = 0; i < 3; ++i) {
			items.get(i).as(SelectAgent.class).select();
			Assert.assertEquals(values[i], msg.getValue());
		}
		
		//multiple select in single selection mode, throw an exception
		desktopAgent.query("#multipleMode").as(CheckAgent.class).check(false);
		try {
			items.get(0).as(MultipleSelectAgent.class).select();
			Assert.fail();
		}catch(RuntimeException e){
			System.out.println(e.getMessage());
		}
	}
}
