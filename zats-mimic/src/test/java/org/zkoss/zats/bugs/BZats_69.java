package org.zkoss.zats.bugs;

import org.junit.Assert;
import org.junit.Test;
import org.zkoss.zats.common.json.JSONArray;
import org.zkoss.zats.mimic.impl.au.AuUtility;

import java.util.Map;

/**
 * http://tracker.zkoss.org/browse/ZATS-69
 */
public class BZats_69 {

	String rawResponse = "<script class=\"z-runonce\" type=\"text/javascript\">\n" +
			"zkmx(\n" +
			"    [0,'qNBQ_',{dt:'z_pgz',cu:'',uu:'\\x2Fzkau',ru:'\\x2Fdummy.zul'},{},[]],\n" +
			"    0,\n" +
			"    [\n" +
			"        'script','[\"dummy(\\'test\\');\"]',\n" +
			"        'clearBusy',null,\n" +
			"        'script','[\"dummy(\\'test\\');\"]'\n" +
			"    ]\n" +
			");\n" +
			"</script>";

	@Test
	public void parseClearBusyAuCommand() {
		Map<String, Object> parsedResponse = AuUtility.parseAuResponseFromLayout(rawResponse);
		JSONArray parsedCommands = (JSONArray) parsedResponse.get("rs");
		Assert.assertEquals(3, parsedCommands.size());
		Assert.assertEquals("[\"clearBusy\",null]", parsedCommands.get(1).toString());
	}
}
