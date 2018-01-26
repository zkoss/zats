package org.zkoss.zats.bugs;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.zkoss.zats.common.json.JSONArray;
import org.zkoss.zats.mimic.impl.au.AuUtility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * http://tracker.zkoss.org/browse/ZATS-69
 */
public class BZats_69 {

	@Test
	public void parseClearBusyAuCommand() throws IOException {
		InputStream response = BZats_69.class.getResourceAsStream("responseZATS_69");
		String rawResponse = IOUtils.toString(response);
		Map<String, Object> parsedResponse = AuUtility.parseAuResponseFromLayout(rawResponse);
		JSONArray parsedCommands = (JSONArray) parsedResponse.get("rs");
		Assert.assertEquals(3, parsedCommands.size());
		Assert.assertEquals("[\"clearBusy\",null]", parsedCommands.get(1).toString());
	}
}
