/* AbstractTypeAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.zkoss.json.JSONs;
import org.zkoss.lang.Strings;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.impl.ClientCtrl;
import org.zkoss.zats.mimic.impl.au.EventDataManager;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectionEvent;
import org.zkoss.zul.impl.FormatInputElement;
import org.zkoss.zul.impl.InputElement;

/**
 * The abstract builder of typing operation.
 * 
 * @author dennis
 */
public abstract class AbstractTypeAgentBuilder implements OperationAgentBuilder<ComponentAgent,TypeAgent> {

	protected static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd"; // see datebox
	protected static final String DEFAULT_TIME_FORMAT = "HH:mm"; // see timebox

	public TypeAgent getOperation(final ComponentAgent target) {
		return new TypeAgentImpl(target);
	}
	
	public Class<TypeAgent> getOperationClass() {
		return TypeAgent.class;
	}
	
	class TypeAgentImpl extends AgentDelegator<ComponentAgent> implements TypeAgent{
		public TypeAgentImpl(ComponentAgent target) {
			super(target);
		}
		
		public void input(Object value){
			type(toRawString(target,value));
		}

		public void type(String value) {
			try {
				ClientCtrl cctrl = (ClientCtrl) target.getClient();
				String cmd = Events.ON_CHANGE;
				InputEvent event = new InputEvent(cmd, (Component) target.getDelegatee(), value, null);
				Map<String, Object> data = EventDataManager.build(event);
				putValue(target, value, data); // parse value and put into data collection 
				cctrl.postUpdate(target.getDesktop().getId(), cmd, target.getUuid(), data, null);
				
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
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data, null);
		}

		/* (non-Javadoc)
		 * @see org.zkoss.zats.mimic.operation.TypeAgent#select(int, int)
		 */
		public void select(int start, int end) {
			String desktopId = target.getDesktop().getId();
			String cmd = Events.ON_SELECTION;
			String selectedText = ((InputElement)target.getDelegatee()).getText().substring(start, end);
			SelectionEvent event = new SelectionEvent(cmd, (Component) target.getDelegatee(), start, end,selectedText);
			Map<String, Object> data = EventDataManager.build(event);
			((ClientCtrl) target.getClient()).postUpdate(desktopId, cmd, target.getUuid(), data,null);
		}
	}

	/**
	 * extender should put parsed value(s) into data map 
	 */
	protected abstract void putValue(ComponentAgent target, String raw, Map<String, Object> data);
	/**
	 * extender converter value to raw string to set to {@link TypeAgent#type(String)}
	 */
	protected abstract String toRawString(ComponentAgent target, Object value);

	static Date parseDate(String format, String value) {
		try {
			return new SimpleDateFormat(format).parse(value);
		} catch (Exception e) {
			throw new AgentException(e.getMessage(),e);
		}
	}
	
	static String formatDate(String format, Date value) {
		try {
			return new SimpleDateFormat(format).format(value);
		} catch (Exception e) {
			throw new AgentException(e.getMessage(),e);
		}
	}
	
	static Number parseNumber(String format, String value) {
		try {
			return new DecimalFormat(format).parse(value);
		} catch (Exception e) {
			throw new AgentException(e.getMessage(),e);
		}
	}
	
	static String formatNumber(String format,Number number){
		try {
			return new DecimalFormat(format).format(number);
		} catch (Exception e) {
			throw new AgentException(e.getMessage(),e);
		}
	}
	
	static public class TextTypeAgentBuilder extends AbstractTypeAgentBuilder {
		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			data.put("value", raw);
		}
		
		@Override
		protected String toRawString(ComponentAgent target, Object value){
			return value==null?"":value.toString();
		}
	}
	
	static public class IntegerTypeAgentBuilder extends AbstractTypeAgentBuilder {
		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			if(Strings.isBlank(raw)) {
				data.put("value", null);
			}else{
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					data.put("value", parseNumber(f, raw.trim()));
				else
					data.put("value", new BigInteger(raw.trim()));
			}
		}
		@Override
		protected String toRawString(ComponentAgent target, Object value){
			if(value==null)
				return null;
			if(value instanceof Number){
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					return formatNumber(f, (Number)value);
				return value.toString();
			}
			throw new IllegalArgumentException("unsupported value type "+value.getClass());
		}
	}
	
	static public class IntegerStringTypeAgentBuilder extends AbstractTypeAgentBuilder {
		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			if(Strings.isBlank(raw)) {
				data.put("value", null);
			}else{
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					data.put("value", parseNumber(f, raw.trim()).toString());
				else
					data.put("value", new BigInteger(raw.trim()).toString());//longbox
			}
		}

		@Override
		protected String toRawString(ComponentAgent target, Object value){
			if(value==null)
				return null;
			if(value instanceof Number){
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					return formatNumber(f, (Number)value);
				return value.toString();
			}
			throw new IllegalArgumentException("unsupported value type "+value.getClass());
		}
	}
	
	static public class DecimalTypeAgentBuilder extends AbstractTypeAgentBuilder {
		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			if(Strings.isBlank(raw)) {
				data.put("value", null);
			}else{
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					data.put("value", parseNumber(f, raw.trim()));
				else
					data.put("value", new BigDecimal(raw.trim()));
			}
		}
		
		@Override
		protected String toRawString(ComponentAgent target, Object value){
			if(value==null)
				return null;
			if(value instanceof Number){
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					return formatNumber(f, (Number)value);
				return value.toString();
			}
			throw new IllegalArgumentException("unsupported value type "+value.getClass());
		}
	}
	
	static public class DecimalStringTypeAgentBuilder extends AbstractTypeAgentBuilder {
		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			if(Strings.isBlank(raw)) {
				data.put("value", null);
			}else{
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					data.put("value", parseNumber(f, raw.trim()).toString());
				else
					data.put("value", new BigDecimal(raw.trim()).toString()); // decimalbox
			}
		}
		
		@Override
		protected String toRawString(ComponentAgent target, Object value){
			if(value==null)
				return null;
			if(value instanceof Number){
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				if (f != null)
					return formatNumber(f, (Number)value);
				return value.toString();
			}
			throw new IllegalArgumentException("unsupported value type "+value.getClass());
		}
	}
	
	static public class DateTypeAgentBuilder extends AbstractTypeAgentBuilder {
		@Override
		protected void putValue(ComponentAgent target,String raw,Map<String , Object> data){
			if(Strings.isBlank(raw)) {
				data.put("value", null);
			}else{
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				Date date = parseDate(f == null ? DEFAULT_DATE_FORMAT : f, raw.trim());
				data.put("value", JSONs.d2j(date));
				data.put("z_type_value", "Date");
			}
		}
		
		@Override
		protected String toRawString(ComponentAgent target, Object value){
			if(value==null)
				return null;
			if(value instanceof Date){
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				return formatDate(f == null ? DEFAULT_DATE_FORMAT : f, ((Date)value));
			}
			throw new IllegalArgumentException("unsupported value type "+value.getClass());
		}
	}
	
	static public class TimeTypeAgentBuilder extends AbstractTypeAgentBuilder {
		@Override
		protected void putValue(ComponentAgent target, String raw, Map<String, Object> data) {
			if(Strings.isBlank(raw)) {
				data.put("value", null);
			}else{
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				Date time = parseDate(f == null ? DEFAULT_TIME_FORMAT : f, raw.trim());
				data.put("value", JSONs.d2j(time));
				data.put("z_type_value", "Date");
			}
		}
		
		@Override
		protected String toRawString(ComponentAgent target, Object value){
			if(value==null)
				return null;
			if(value instanceof Date){
				Object comp = target.getDelegatee();
				String f = ((FormatInputElement) comp).getFormat();
				return formatDate(f == null ? DEFAULT_DATE_FORMAT : f, ((Date)value));
			}
			throw new IllegalArgumentException("unsupported value type "+value.getClass());
		}
	}
}
