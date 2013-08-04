/**
 * 
 */
package net.arunoday.kpi.engine.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Data transfer object for measurement event representation.
 * 
 * @author Aparna
 * 
 */
public class MeasurementEvent implements Serializable {

	private static final long serialVersionUID = 3220480942151059770L;
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

}
