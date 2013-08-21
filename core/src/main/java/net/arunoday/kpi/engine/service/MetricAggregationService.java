package net.arunoday.kpi.engine.service;

import net.arunoday.kpi.engine.entity.MetricResolution;

/**
 * Service API for metric aggregation.
 * 
 * @author Aparna Chaudhary
 * 
 */
public interface MetricAggregationService {

	/**
	 * @param resolution
	 */
	void performAggregation(MetricResolution resolution);
}
