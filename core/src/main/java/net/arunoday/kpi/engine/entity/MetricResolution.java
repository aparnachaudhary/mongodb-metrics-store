package net.arunoday.kpi.engine.entity;

/**
 * Resolution for Metric data
 * 
 * @author Aparna
 * 
 */
public enum MetricResolution {

	/** Yearly aggregate */
	YEAR("y"),
	/** Monthly aggregate */
	MONTH("m"),
	/** Weekly aggregate */
	WEEK("w"),
	/** Daily Aggregate */
	DAY("d"),
	/** Hourly Aggregate */
	HOUR("h"),
	/** Aggregation per minute */
	MINUTE("m");

	private String code;

	/**
	 * Constructor
	 * @param code metric code
	 */
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
