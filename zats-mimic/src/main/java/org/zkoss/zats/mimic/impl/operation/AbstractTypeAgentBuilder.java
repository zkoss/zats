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

import org.zkoss.lang.Strings;
import org.zkoss.zats.mimic.AgentException;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.operation.TypeAgent;
import org.zkoss.zul.impl.FormatInputElement;

/**
 * The abstract builder of typing operation.
 * 
 * @author dennis
 */
public abstract class AbstractTypeAgentBuilder implements OperationAgentBuilder<TypeAgent> {

	private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";//see datebox
	private static final String DEFAULT_TIME_FORMAT = "HH:mm";//see timebox

	public TypeAgent getOperation(final ComponentAgent target) {
		return new TypeAgentImpl(target);
	}
	
	class TypeAgentImpl extends AgentDelegator implements TypeAgent{
		public TypeAgentImpl(ComponentAgent target) {
			super(target);
		}

		public void type(String value) {
			try {
				// parse value
				Object parsed = getValue(target,value);

				AuUtility.postOnFocus(target);
				AuUtility.postOnChanging(target, value);
				AuUtility.postOnChange(target, parsed);
				AuUtility.postOnBlur(target);

			} catch (Exception e) {
				throw new AgentException("value \"" + value
						+ "\"is invalid for the component: "
						+ target, e);
			}
		}
	}

	protected abstract Object getValue(ComponentAgent target, String raw);
	

	static Date parseDate(String format, String value) {
		try {
			return new SimpleDateFormat(format).parse(value);
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
	
	static public class TextTypeAgentBuilder extends AbstractTypeAgentBuilder {
		protected Object getValue(ComponentAgent target,String raw){
			return raw;
		}
	}
	
	static public class IntegerTypeAgentBuilder extends AbstractTypeAgentBuilder {
		protected Object getValue(ComponentAgent target,String raw){
			if(Strings.isBlank(raw)) return null;
			
			Object comp = target.getDelegatee();
			String f = ((FormatInputElement)comp).getFormat();
			if(f!=null){
				return parseNumber(f,raw.trim());
			}else{
				return new BigInteger(raw.trim());
			}
		}
	}
	
	static public class IntegerStringTypeAgentBuilder extends AbstractTypeAgentBuilder {
		protected Object getValue(ComponentAgent target,String raw){
			if(Strings.isBlank(raw)) return null;
			
			Object comp = target.getDelegatee();
			String f = ((FormatInputElement)comp).getFormat();
			if(f!=null){
				return parseNumber(f,raw.trim()).toString();
			}else{
				return new BigInteger(raw.trim()).toString();//longbox
			}
		}
	}
	
	static public class DecimalTypeAgentBuilder extends AbstractTypeAgentBuilder {
		protected Object getValue(ComponentAgent target,String raw){
			if(Strings.isBlank(raw)) return null;
			
			Object comp = target.getDelegatee();
			String f = ((FormatInputElement)comp).getFormat();
			if(f!=null){
				return parseNumber(f,raw.trim());
			}else{
				return new BigDecimal(raw.trim());
			}
		}
	}
	static public class DecimalStringTypeAgentBuilder extends AbstractTypeAgentBuilder {
		protected Object getValue(ComponentAgent target,String raw){
			if(Strings.isBlank(raw)) return null;
			
			Object comp = target.getDelegatee();
			String f = ((FormatInputElement)comp).getFormat();
			if(f!=null){
				return parseNumber(f,raw.trim()).toString();
			}else{
				return new BigDecimal(raw.trim()).toString();//decimalbox
			}
		}
	}
	
	static public class DateTypeAgentBuilder extends AbstractTypeAgentBuilder {
		protected Object getValue(ComponentAgent target,String raw){
			if(Strings.isBlank(raw)) return null;
			
			Object comp = target.getDelegatee();
			String f = ((FormatInputElement)comp).getFormat();
			return parseDate(f==null?DEFAULT_DATE_FORMAT:f,raw.trim());
		}
	}
	
	static public class TimeTypeAgentBuilder extends AbstractTypeAgentBuilder {
		protected Object getValue(ComponentAgent target,String raw){
			if(Strings.isBlank(raw)) return null;
			
			Object comp = target.getDelegatee();
			String f = ((FormatInputElement)comp).getFormat();
			return parseDate(f==null?DEFAULT_TIME_FORMAT:f,raw.trim());
		}
	}
}
