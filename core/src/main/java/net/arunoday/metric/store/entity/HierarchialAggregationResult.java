package net.arunoday.metric.store.entity;

import java.util.Map;

/**
 * Result of hierarchial aggregation.
 * 
 * @author Aparna Chaudhary
 */
public class HierarchialAggregationResult {

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
		return "Result [id=" + id + ", value=" + value + "]";
	}

}
