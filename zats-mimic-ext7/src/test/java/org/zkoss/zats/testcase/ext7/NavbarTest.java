package org.zkoss.zats.testcase.ext7;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;

/**
 * 
 * @author Robert
 *
 */
public class NavbarTest {
	private Label target;
	private Label event;
	private Label label;
	private ComponentAgent comps;
	private List<ComponentAgent> navitemAgents;
	private String[] navitemLabels;

	@BeforeClass
	public static void init() {
		Zats.init(".");
	}

	@AfterClass
	public static void end() {
		Zats.end();
	}

	@After
	public void after() {
		Zats.cleanup();
	}
	
	@Before
	public void before() {
		DesktopAgent desktop = Zats.newClient().connect("/~./basic/nav.zul");
		
		target = desktop.query("#target").as(Label.class);
		event = desktop.query("#eventName").as(Label.class);
		label = desktop.query("#targetLabel").as(Label.class);
		assertEquals("", target.getValue());
		assertEquals("", event.getValue());
		assertEquals("", label.getValue());
		
		comps = desktop.query("#comps");
		Assert.assertNotNull(comps);
		
		navitemLabels = new String[] {"Home", "Step One" , "Step Two" , "Step Three", "About", "Contact"};
		navitemAgents = desktop.queryAll("navitem");
		assertEquals("unexpected number of navitems", navitemLabels.length, navitemAgents.size());
	}
	
	@Test
	public void testNavitemClick() {
		for(int i = 0 ; i < navitemLabels.length ; ++i) {
			navitemAgents.get(i).as(ClickAgent.class).click();
			checkTestLabels("navitem", navitemLabels[i], Events.ON_CLICK);

			navitemAgents.get(i).as(ClickAgent.class).doubleClick();
			checkTestLabels("navitem", navitemLabels[i], Events.ON_DOUBLE_CLICK);
			
			navitemAgents.get(i).as(ClickAgent.class).rightClick();
			checkTestLabels("navitem", navitemLabels[i], Events.ON_RIGHT_CLICK);
		}
	}

	@Test
	public void testNavbarSelection() {
		for(int i = 0 ; i < navitemLabels.length ; ++i) {
			navitemAgents.get(i).as(SelectAgent.class).select();
			checkTestLabels("navbar", navitemLabels[i], Events.ON_SELECT);
		}
	}

	private void checkTestLabels(String eventTargetType, String navitemLabel, String eventName) {
		assertEquals(eventTargetType, target.getValue());
		assertEquals(navitemLabel, label.getValue());
		assertEquals(eventName, event.getValue());
	}

}
