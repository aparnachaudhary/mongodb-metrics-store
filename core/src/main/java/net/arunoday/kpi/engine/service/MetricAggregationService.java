package net.arunoday.kpi.engine.service;


/**
 * Service API for metric aggregation.
 * 
 * @author Aparna Chaudhary
 * 
 */
public interface MetricAggregationService {

	/**
	 * Calculate aggregates per minute and store in metrics collection.
	 * 
	 */
	void performAggregationPerMinute();

	/**
	 * Calculate aggregates per hour and store in metrics collection.
	 * 
	 */
	void performAggregationPerHour();

	/**
	 * Calculate aggregates per day and store in metrics collection.
	 * 
	 */
	void performAggregationPerDay();
}
