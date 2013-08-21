package net.arunoday.kpi.engine.entity;

/**
 * Resolution for Metric data
 * 
 * @author Aparna
 * 
 */
public enum MetricResolution {

	YEAR("y"),
	/** Monthly aggregate */
	MONTH("m"), WEEK("w"),
	/** Daily Aggregate */
	DAY("d"), HOUR("h"), MINUTE("m");

	private String code;

	private MetricResolution(String code) {
		this.code = code;
	}

	/**
	 * Returns resolution code
	 * 
	 * @return metric resolution code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Constructs {@link MetricResolution} based on code.
	 * 
	 * @param code resolution code
	 * @return {@link MetricResolution} for the code; {@literal null} if none found.
	 */
	public static MetricResolution fromCode(String code) {
		if (code != null) {
			for (MetricResolution metric : MetricResolution.values()) {
				if (code.equalsIgnoreCase(metric.code)) {
					return metric;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return code;
	}

}
