package org.zkoss.zats.mimic.operation;

public interface Clickable extends Operation
{
	Clickable click();

	Clickable doubleClick();

	Clickable rightClick();
}
