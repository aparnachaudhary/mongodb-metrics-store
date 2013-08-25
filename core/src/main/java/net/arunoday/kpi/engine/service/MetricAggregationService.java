package net.arunoday.kpi.engine.service;


/**
 * Service API for metric aggregation.
 * 
 * @author Aparna Chaudhary
 * 
 */
public interface MetricAggregationService {

	/**
	 * Calculate aggregates per hour and store in metrics collection.
	 * 
	 */
	void performAggregationPerHour();

	/**
	 * Calculate aggregates per minute and store in metrics collection.
	 * 
	 */
	void performAggregationPerMinute();
}
