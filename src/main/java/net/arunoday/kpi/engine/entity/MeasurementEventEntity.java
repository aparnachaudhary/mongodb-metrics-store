/**
 * 
 */
package net.arunoday.kpi.engine.entity;

import java.util.Date;

/**
 * Measurement Event Entity.
 * 
 * @author Aparna
 * 
 */
public class MeasurementEventEntity {

	private String id;
	private Date occuredOn;
	/** */
	private String eventType;
	private ContextData contextData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getOccuredOn() {
		return occuredOn;
	}

	public void setOccuredOn(Date occuredOn) {
		this.occuredOn = occuredOn;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public ContextData getContextData() {
		return contextData;
	}

	public void setContextData(ContextData contextData) {
		this.contextData = contextData;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}
		MeasurementEventEntity that = (MeasurementEventEntity) obj;
		return this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public String toString() {
		return "MeasurementEventEntity [id=" + id + ", occuredOn=" + occuredOn + ", eventType=" + eventType
				+ ", contextData="
				+ contextData + "]";
	}



}
