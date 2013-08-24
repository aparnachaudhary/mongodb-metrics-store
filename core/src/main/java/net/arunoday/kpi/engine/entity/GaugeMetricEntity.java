/**
 * 
 */
package net.arunoday.kpi.engine.entity;

import java.util.Date;

/**
 * Represents the Metric information for Gauge events.
 * 
 * @author Aparna Chaudhary
 * 
 */
public class GaugeMetricEntity {

	/** unique identifier for this event */
	private String id;
	/** Type of event. */
	private String eventType;
	/** Resolution of metric */
	private String resolution;
	/** Time of occurrence of event */
	private Date timestamp;
	private String operation;
	private double value;

	/**
	 * Public constructor
	 */
	public GaugeMetricEntity() {
		super();
	}

	public GaugeMetricEntity(String eventType, String resolution, Date timestamp, String operation, double value) {
		super();
		this.eventType = eventType;
		this.resolution = resolution;
		this.timestamp = timestamp;
		this.operation = operation;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public MetricResolution getResolution() {
		return MetricResolution.fromCode(resolution);
	}

	public void setResolution(MetricResolution resolution) {
		this.resolution = resolution.getCode();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}
		GaugeMetricEntity that = (GaugeMetricEntity) obj;
		return this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public String toString() {
		return "GaugeMetricEntity [id=" + id + ", eventType=" + eventType + ", resolution=" + resolution + ", timestamp="
				+ timestamp + ", operation=" + operation + ", value=" + value + "]";
	}

}
