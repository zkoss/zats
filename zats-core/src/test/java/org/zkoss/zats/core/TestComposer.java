package org.zkoss.zats.core;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;

public class TestComposer extends GenericForwardComposer
{
	private static final long serialVersionUID = 1L;
	private Label msg;
	private int count;

	@Override
	public void doAfterCompose(Component comp) throws Exception
	{
		super.doAfterCompose(comp);
		msg.setValue("hello");
		desktop.setAttribute("msg", "desktop");
		session.setAttribute("msg", "session");
	}

	public void onClick$btn()
	{
		msg.setValue("" + count);
		desktop.setAttribute("msg", "d" + count);
		session.setAttribute("msg", "s" + count);
		++count;
	}

	public void onClick$dump()
	{
		System.out.println("msg in desktop: " + desktop.getAttribute("msg"));
		System.out.println("msg in session: " + session.getAttribute("msg"));
	}
}
