package org.zkoss.zats.mimic.exception;

import java.util.List;

import org.zkoss.zats.mimic.impl.EmulatorClient;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ExecutionCleanup;

public class ZKExecutionCleanup implements ExecutionCleanup {

	public void cleanup(Execution exec, Execution parent, List errs)
			throws Exception {
		if (errs != null && errs.size() > 0) {
			ZKExceptionHandler.getInstance().setExceptions(errs);
		}
	}


}
