package net.arunoday.kpi.engine.entity;

import java.util.Map;

/**
 * Result of aggregation.
 * 
 * @author Aparna
 * 
 */
public class AggregatedValue {

	private String id;
	private Map<String, Object> value;

	public String getId() {
		return id;
	}

	public Map<String, Object> getValue() {
		return value;
	}

	public void setValue(Map<String, Object> value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ValueObject [id=" + id + ", value=" + value + "]";
	}

}
