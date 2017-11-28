package org.zkoss.zats.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.DesktopAgent;

/**
 * A {@link TestRule} implementing {@link ExternalResource} automatically creating and destroying a new Zats {@link Client} instance.</br>
 * Used with {@link org.junit.Rule} this will provide a pluggable alternative to separate methods annotated with {@link Before} and {@link After}
 */
public class AutoClient extends ExternalResource {
	private AutoEnvironment autoEnv;
	private Client client;

	public AutoClient(AutoEnvironment autoEnv) {
		this.autoEnv = autoEnv;
	}

	@Override
	protected void before() throws Throwable {
		client = autoEnv.getZatsEnvironment().newClient();
	}

	@Override
	protected void after() {
		client.destroy();
	}

	/**
	 * convenience method to load a zul page directly (calls: {@link Client#connect(String)})
	 *
	 * @param zulPath
	 * @return {@link DesktopAgent}
	 */
	public DesktopAgent connect(String zulPath) {
		return client.connect(zulPath);
	}

	/**
	 * allows access to the automatically created Zats {@link Client} instance. To access all functionality.
	 *
	 * @return the current Zats {@link Client}
	 */
	public Client getClient() {
		return client;
	}
}
