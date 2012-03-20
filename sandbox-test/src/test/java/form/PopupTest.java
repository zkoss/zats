package form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.zkoss.zats.mimic.Searcher.find;

import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.Searcher;
import org.zkoss.zats.mimic.node.ComponentNode;
import org.zkoss.zats.mimic.operation.Clickable;
import org.zkoss.zul.Textbox;

import util.TestCaseBase;


public class PopupTest extends TestCaseBase {

	@Test
	public void testClickMenu(){
		Conversations.open("/form/popup.zul");
		Textbox intro = Searcher.find("textbox[width='250px'").as(Textbox.class);
		assertNotNull(intro.getValue());
		
		ComponentNode menupopup = find("menupopup");
		//clear text box text and verify
		menupopup.getChild(0).as(Clickable.class).click();
		assertEquals(0, intro.getValue().length());
	}
}
