package form;

import static org.junit.Assert.assertEquals;
import static org.zkoss.zats.mimic.Searcher.find;

import org.junit.Test;
import org.zkoss.zats.mimic.Conversations;
import org.zkoss.zats.mimic.operation.Checkable;
import org.zkoss.zul.Label;

import util.TestCaseBase;


public class RadioTest extends TestCaseBase {

	@Test
	public void testRadio(){
		Conversations.open("/form/radio.zul");
		
		find("radio[label='Performance'").as(Checkable.class).check(true);
		Label label = find("label[id='choice']").cast(Label.class);
		assertEquals("Performance", label.getValue());
		
		find("radio[label='Forum'").as(Checkable.class).check(true);
		assertEquals("Forum", label.getValue());
		
	}
	
	@Test
	public void testCheckbox(){
		Conversations.open("/form/radio.zul");
		
		find("checkbox[label='Border']").as(Checkable.class).check(true);
		Label label = find("label[id='layout']").cast(Label.class);
		assertEquals("Border ", label.getValue());
		
		find("checkbox[label='Box']").as(Checkable.class).check(true);
		assertEquals("Border Box ", label.getValue());
		
	}
}
