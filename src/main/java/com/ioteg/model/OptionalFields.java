package com.ioteg.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a set of optional fields in the data model.
 *
 * @author Antonio Vélez Estévez
 * @version $Id: $Id
 */

@Entity
public class OptionalFields extends OwnedEntity{

	
	private Boolean mandatory;
	@Valid
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<Field> fields;

	public OptionalFields() {
		
	}

	/**
	 * <p>
	 * Constructor for OptionalFields.
	 * </p>
	 *
	 * @param id        a {@link java.lang.Long} object.
	 * @param mandatory a {@link java.lang.Boolean} object.
	 * @param fields    a {@link java.util.List} object.
	 */
	@JsonCreator
	public OptionalFields(@JsonProperty("id") Long id, @JsonProperty("mandatory") Boolean mandatory,
			@Valid @JsonProperty("fields") List<Field> fields) {
		if (mandatory == null)
			mandatory = true;
		if (fields == null)
			fields = new ArrayList<>();

		this.id = id;
		this.mandatory = mandatory;
		this.fields = fields;
	}
	
	
	
	/**
	 * @param mandatory
	 * @param fields
	 */
	public OptionalFields(Boolean mandatory, @Valid List<Field> fields) {
		this(null, mandatory, fields);
	}

	/**
	 * <p>
	 * Getter for the field <code>mandatory</code>.
	 * </p>
	 *
	 * @return the mandatory
	 */
	public Boolean getMandatory() {
		return mandatory;
	}

	/**
	 * <p>
	 * Setter for the field <code>mandatory</code>.
	 * </p>
	 *
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * <p>
	 * Getter for the field <code>fields</code>.
	 * </p>
	 *
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * <p>
	 * Setter for the field <code>fields</code>.
	 * </p>
	 *
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

}
