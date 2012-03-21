/* GenericTypeAgentBuilder.java

	Purpose:
		
	Description:
		
	History:
		Mar 20, 2012 Created by pao

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zats.mimic.impl.operation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.ConversationException;
import org.zkoss.zats.mimic.operation.TypeAgent;

/**
 * The generic builder of typing operation.
 * 
 * @author pao
 */
public class GenericTypeAgentBuilder implements OperationAgentBuilder<TypeAgent> {
	public final static int TEXT = 0;
	public final static int INTEGER = 1;
	public final static int DECIMAL = 2;
	public final static int DATE = 3;
	public final static int TIME = 4;
	private int type;
	private static SimpleDateFormat df;
	private SimpleDateFormat dtf;
	private SimpleDateFormat tf;

	public GenericTypeAgentBuilder(int type) {
		if (type < 0 || type > 4)
			throw new IllegalArgumentException("unknown type: " + type);
		this.type = type;
		df = new SimpleDateFormat("yyyy/MM/dd");
		dtf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		tf = new SimpleDateFormat("HH:mm:ss");
	}

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
				value = value.trim();
				Object parsed = null;
				if (type == TEXT)
					parsed = value;
				else if (type == INTEGER)
					parsed = new BigInteger(value);
				else if (type == DECIMAL)
					parsed = new BigDecimal(value);
				else if (type == DATE) {
					parsed = parse(df, value);
					if (parsed == null)
						parsed = parse(dtf, value);
					if (parsed == null)
						throw new ParseException(value
								+ " can't parse to date", 0);
				} else if (type == TIME) {
					parsed = parse(tf, value);
					if (parsed == null)
						throw new ParseException(value
								+ " can't parse to time", 0);
				}

				AuUtility.postFocus(target);
				AuUtility.postChanging(target, value);
				AuUtility.postChange(target, parsed);
				AuUtility.postBlur(target);

			} catch (Exception e) {
				throw new ConversationException("value \"" + value
						+ "\"is invalid for the component: "
						+ target, e);
			}
		}
	}

	private static Date parse(DateFormat f, String value) {
		try {
			return f.parse(value);
		} catch (Exception e) {
		}
		return null;
	}
}
