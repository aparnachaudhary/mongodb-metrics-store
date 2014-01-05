package net.arunoday.metric.store.model;

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

	/**
	 * Checks if this result contains a given field
	 * 
	 * @param field field name
	 * @return if the field exists
	 */
	public boolean containsField(String field) {
		return value.containsKey(field);
	}

	/**
	 * Gets a value for the specific key
	 * 
	 * @param key field name
	 * @return the value
	 */
	public Object get(String key) {
		return value.get(key);
	}

	@Override
	public String toString() {
		return "Result [id=" + id + ", value=" + value + "]";
	}

}
