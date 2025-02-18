package com.ioteg.controllers.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AttributeResponse {
	@NotNull
	private Long id;
	
	@NotEmpty
	@NotNull
	protected String type;
	protected String value;
	protected Double min;
	protected String step;
	protected String unit;
	protected Double max;
	protected Integer precision;
	protected Integer length;
	protected String strCase;
	protected String begin;
	protected String end;
	protected String endcharacter;
	protected String format;
	protected Boolean isNumeric;
	protected String generationType;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the min
	 */
	public Double getMin() {
		return min;
	}
	/**
	 * @param min the min to set
	 */
	public void setMin(Double min) {
		this.min = min;
	}
	/**
	 * @return the step
	 */
	public String getStep() {
		return step;
	}
	/**
	 * @param step the step to set
	 */
	public void setStep(String step) {
		this.step = step;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the max
	 */
	public Double getMax() {
		return max;
	}
	/**
	 * @param max the max to set
	 */
	public void setMax(Double max) {
		this.max = max;
	}
	/**
	 * @return the precision
	 */
	public Integer getPrecision() {
		return precision;
	}
	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	/**
	 * @return the length
	 */
	public Integer getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(Integer length) {
		this.length = length;
	}
	/**
	 * @return the strCase
	 */
	public String getStrCase() {
		return strCase;
	}
	/**
	 * @param strCase the strCase to set
	 */
	public void setStrCase(String strCase) {
		this.strCase = strCase;
	}
	/**
	 * @return the begin
	 */
	public String getBegin() {
		return begin;
	}
	/**
	 * @param begin the begin to set
	 */
	public void setBegin(String begin) {
		this.begin = begin;
	}
	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}
	/**
	 * @return the endcharacter
	 */
	public String getEndcharacter() {
		return endcharacter;
	}
	/**
	 * @param endcharacter the endcharacter to set
	 */
	public void setEndcharacter(String endcharacter) {
		this.endcharacter = endcharacter;
	}
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return the isNumeric
	 */
	public Boolean getIsNumeric() {
		return isNumeric;
	}
	/**
	 * @param isNumeric the isNumeric to set
	 */
	public void setIsNumeric(Boolean isNumeric) {
		this.isNumeric = isNumeric;
	}
	/**
	 * @return the generationType
	 */
	public String getGenerationType() {
		return generationType;
	}
	/**
	 * @param generationType the generationType to set
	 */
	public void setGenerationType(String generationType) {
		this.generationType = generationType;
	}
}
