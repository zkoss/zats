package org.zkoss.zats.junit;

import org.junit.*;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.zkoss.zats.mimic.DefaultZatsEnvironment;
import org.zkoss.zats.mimic.ZatsEnvironment;

/**
 * A {@link TestRule} implementing {@link ExternalResource} creating and destroying a {@link ZatsEnvironment}.</br>
 * Used with {@link org.junit.ClassRule} it provides a pluggable alternative to separate static methods annotated with {@link BeforeClass} and {@link AfterClass}</br>
 * </br>
 * Also see: <a href="https://dzone.com/articles/junit-49-class-and-suite-level-rules" target="_blank">dzone.com/articles/junit-49-class-and-suite-level-rules</a>
 */
public class AutoEnvironment extends ExternalResource {
	private ZatsEnvironment zatsEnvironment;

	private String resourceRoot;
	private String webInfPath;

	/**
	 * Creates a default {@link DefaultZatsEnvironment#DefaultZatsEnvironment()}
	 *
	 * @param resourceRoot - will be passed to {@link ZatsEnvironment#init(String)}
	 */
	public AutoEnvironment(String resourceRoot) {
		this.resourceRoot = resourceRoot;
	}

	/**
	 * Creates a {@link DefaultZatsEnvironment#DefaultZatsEnvironment()} with a specified WEB-INF folder
	 *
	 * @param webInfPath   - will be used as the constructor argument in {@link DefaultZatsEnvironment#DefaultZatsEnvironment(String)}
	 * @param resourceRoot - will be passed to {@link ZatsEnvironment#init(String)}
	 */
	public AutoEnvironment(String webInfPath, String resourceRoot) {
		this.webInfPath = webInfPath;
		this.resourceRoot = resourceRoot;
	}

	@Override
	protected void before() throws Throwable {
		if (webInfPath != null) {
			zatsEnvironment = new DefaultZatsEnvironment(webInfPath);
		} else {
			zatsEnvironment = new DefaultZatsEnvironment();
		}
		zatsEnvironment.init(resourceRoot);
	}

	@Override
	protected void after() {
		zatsEnvironment.destroy();
	}

	/**
	 * creates an {@link AutoClient} rule from the current {@link AutoEnvironment}
	 *
	 * @return an {@link AutoClient} rule
	 */
	public AutoClient autoClient() {
		return new AutoClient(this);
	}

	public ZatsEnvironment getZatsEnvironment() {
		return zatsEnvironment;
	}
}
