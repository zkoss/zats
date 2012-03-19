package form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class HelloTest
{
	@BeforeClass
	public static void init()
	{
		Conversations.start("./src/main/webapp");
	}

	@AfterClass
	public static void end()
	{
		Conversations.stop();
	}

	@After
	public void after()
	{
		Conversations.clean();
	}

	@Test
	public void test()
	{
		Conversations.open("/form/hello.zul");
		ComponentNode view = Searcher.find("#view");
		assertEquals(1, view.getChildren().size());
		assertEquals("My First Window", view.getChild(0).cast(Window.class).getTitle());
		String value = view.getChild(0).getChild(0).cast(Label.class).getValue();
		assertTrue(value.indexOf("Hello, World!") >= 0);
	}
}
