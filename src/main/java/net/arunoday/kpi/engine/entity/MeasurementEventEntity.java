/**
 * 
 */
package net.arunoday.kpi.engine.entity;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Measurement Event Entity.
 * 
 * @author Aparna
 * 
 */
@Document
public class MeasurementEventEntity {

	@Id
	private String id;
	private Date occuredOn;
	private Map<String, Object> contextData;

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

	public Map<String, Object> getContextData() {
		return contextData;
	}

	public void setContextData(Map<String, Object> contextData) {
		this.contextData = contextData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contextData == null) ? 0 : contextData.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((occuredOn == null) ? 0 : occuredOn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasurementEventEntity other = (MeasurementEventEntity) obj;
		if (contextData == null) {
			if (other.contextData != null)
				return false;
		} else if (!contextData.equals(other.contextData))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (occuredOn == null) {
			if (other.occuredOn != null)
				return false;
		} else if (!occuredOn.equals(other.occuredOn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MeasurementEventEntity [id=" + id + ", occuredOn=" + occuredOn
				+ ", contextData=" + contextData + "]";
	}

}
