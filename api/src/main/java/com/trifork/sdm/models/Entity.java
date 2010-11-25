package com.trifork.sdm.models;

import java.util.Calendar;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Describes a contract that all models in SDM uphold.
 * 
 * It ensures that models conform to the standard auditing structure, and
 * serializable in our system.
 */
@XmlType
public interface Entity
{
	// TODO: Is there any particular reason why this is
	// not just an integer or long?

	@XmlTransient
	Object getEntityId();

	@XmlElement
	Calendar getValidFrom();

	@XmlElement
	Calendar getValidTo();
	
	Map<String, Object> serialize(); // TODO: How should this serialize the entity?
}
