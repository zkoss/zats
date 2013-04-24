/* AbstractInputAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation.input;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.EventDataManager;
import org.zkoss.zats.mimic.impl.OperationAgentBuilder;
import org.zkoss.zats.mimic.impl.operation.AgentDelegator;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectionEvent;
import org.zkoss.zul.impl.InputElement;

/**
 * The abstract builder of input operation. Because different ZK components accept different format of text input.
 * 
 * @author dennis
 */
public abstract class AbstractInputAgentBuilder implements OperationAgentBuilder<ComponentAgent,InputAgent> {

	protected static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd"; // see datebox
	protected static final String DEFAULT_TIME_FORMAT = "HH:mm"; // see timebox

	
	public Class<InputAgent> getOperationClass() {
		return InputAgent.class;
	}
	
	static public abstract class AbstractInputAgentImpl extends AgentDelegator<ComponentAgent> implements InputAgent{
		public AbstractInputAgentImpl(ComponentAgent target) {
			super(target);
		}
		/**
		 * sub-class should put parsed value(s) into AU data 
		 */
		protected abstract void putValue(ComponentAgent target, String raw, Map<String, Object> data);
		/**
		 * sub-class should convert value into raw string to set to {@link InputAgent#type(String)}
		 */
		protected abstract String toRawString(ComponentAgent target, Object value);
		
		public void input(Object value){
			type(toRawString(target,value));
		}

		public void type(String value) {
			try {
				ClientCtrl cctrl = (ClientCtrl) target.getClient();
				String cmd = Events.ON_CHANGE;
				InputEvent event = new InputEvent(cmd, (Component) target.getDelegatee(), value, null);
				Map<String, Object> data = EventDataManager.getInstance().build(event);
				putValue(target, value, data); // parse value and put into data collection
				String desktopId = target.getDesktop().getId();
				cctrl.postUpdate(desktopId, target.getUuid(), cmd, data, false);
				cctrl.flush(desktopId);
			} catch (Exception e) {
				throw new AgentException("value \"" + value
						+ "\"is invalid for the component: "
						+ target, e);
			}
		}

		public void typing(String value) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_CHANGING;
			InputEvent event = new InputEvent(cmd, (Component) target.getDelegatee(), value, null);
			Map<String, Object> data = EventDataManager.getInstance().build(event);
			((ClientCtrl) target.getClient()).postUpdate(desktopId, target.getUuid(), cmd, data, false);
			((ClientCtrl) getClient()).flush(desktopId);
		}

		public void select(int start, int end) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_SELECTION;
			String selectedText = ((InputElement)target.getDelegatee()).getText().substring(start, end);
			SelectionEvent event = new SelectionEvent(cmd, (Component) target.getDelegatee(), start, end,selectedText);
			Map<String, Object> data = EventDataManager.getInstance().build(event);
			((ClientCtrl) target.getClient()).postUpdate(desktopId, target.getUuid(), cmd, data, false);
			((ClientCtrl) getClient()).flush(desktopId);
		}
	}

	protected static Date parseDate(String format, String value) {
		try {
			return new SimpleDateFormat(format).parse(value);
		} catch (Exception e) {
			throw new AgentException(e.getMessage(),e);
		}
	}
	
	protected static String formatDate(String format, Date value) {
		try {
			return new SimpleDateFormat(format).format(value);
		} catch (Exception e) {
			throw new AgentException(e.getMessage(),e);
		}
	}
	
	protected static Number parseNumber(String format, String value) {
		try {
			return new DecimalFormat(format).parse(value);
		} catch (Exception e) {
			throw new AgentException(e.getMessage(),e);
		}
	}
	
	protected static String formatNumber(String format,Number number){
		try {
			return new DecimalFormat(format).format(number);
		} catch (Exception e) {
			throw new AgentException(e.getMessage(),e);
		}
	}
}
