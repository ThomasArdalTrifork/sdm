package com.trifork.sdm.models;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.trifork.sdm.util.DateUtils;


/**
 * Convenience super-class that implements the Record interface.
 */
public abstract class AbstractRecord implements Record {

	static final Logger logger = Logger.getLogger(AbstractRecord.class);

	// TODO: These should be moved.
	static final Map<Class<? extends Record>, Method> idMethodCache = new HashMap<Class<? extends Record>, Method>();
	static final Map<Method, String> outputFieldNames = new HashMap<Method, String>();

	private long pid = -1l;

	private Date validFrom = DateUtils.PAST;
	private Date validTo = DateUtils.FUTURE;

	private Date modifiedDate;


	@Override
	public long getPID() {

		return pid;
	}


	@Override
	public void setPID(long pid) {

		this.pid = pid;
	}

	
	@Override
	public void setModifiedDate(Date modifiedDate) {
		
		this.modifiedDate = modifiedDate;
	}


	@Override
	public Date getModifiedDate() {
		
		return modifiedDate;
	}
	

	@Override
	public void setValidFrom(Date validfrom) {

		this.validFrom = validfrom;
	}


	@Override
	public Date getValidFrom() {

		return validFrom;
	}


	@Override
	public Date getValidTo() {

		return validTo;
	}


	@Override
	public void setValidTo(Date validTo) {

		this.validTo = validTo;
	}


	@Override
	public Object getKey() {

		// TODO: This method should not really be part of this class.

		Method idMethod = EntityHelper.getIdMethod(getClass());

		try {
			return idMethod.invoke(this);
		}
		catch (Exception e) {
			logger.error("Error getting id for object of class: " + getClass());
			return null;
		}
	}
}
