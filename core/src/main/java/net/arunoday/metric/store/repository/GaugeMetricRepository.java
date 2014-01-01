package net.arunoday.metric.store.repository;

import java.io.Serializable;
import java.util.Date;

import net.arunoday.metric.store.entity.HierarchialAggregationResult;
import net.arunoday.metric.store.entity.MetricResolution;

/**
 * Repository interface for Gauge Metrics
 * 
 * @author Aparna Chaudhary
 */
public interface GaugeMetricRepository<ID extends Serializable> {

	static final String METRICS_COLLECTION = "_gauge_metrics";
	static final String EVENT_COLLECTION = "_gauge_events";

	/**
	 * Returns all instances of the {@link HierarchialAggregationResult} by event type matching the filter criteria for the given time
	 * range.
	 * 
	 * @param eventType event name must not be {@literal null}.
	 * @param resolution metric resolution
	 * @param startTime start date for metric filtering; date is inclusive; ignored if null.
	 * @param endTime end date for metric filtering; date is exclusive; ignored if null.
	 * @return matching metric entities
	 */
	Iterable<HierarchialAggregationResult> find(String eventType, MetricResolution resolution, Date startTime, Date endTime);

	/**
	 * Performs minute-wise aggregation for the given event type.
	 * 
	 * @param eventName name of the event for which aggregation is required
	 * @param startDate start date for event filtering; date is inclusive; ignored if null.
	 * @param endDate end date for event filtering; date is exclusive; ignored if null.
	 */
	void aggregatePerMinute(String eventName, Date startDate, Date endDate);

	/**
	 * Performs hourly aggregation for the given event type.
	 * 
	 * @param eventName name of the event for which aggregation is required
	 * @param startDate start date for event filtering; date is inclusive; ignored if null.
	 * @param endDate end date for event filtering; date is exclusive; ignored if null.
	 */
	void aggregatePerHour(String eventName, Date startDate, Date endDate);

	/**
	 * Performs daily aggregation for the given event type.
	 * 
	 * @param eventName name of the event for which aggregation is required
	 * @param startDate start date for event filtering; date is inclusive; ignored if null.
	 * @param endDate end date for event filtering; date is exclusive; ignored if null.
	 */
	void aggregatePerDay(String eventName, Date startDate, Date endDate);

	/**
	 * Performs monthly aggregation for the given event type.
	 * 
	 * @param eventName name of the event for which aggregation is required
	 * @param startDate start date for event filtering; date is inclusive; ignored if null.
	 * @param endDate end date for event filtering; date is exclusive; ignored if null.
	 */
	void aggregatePerMonth(String eventName, Date startDate, Date endDate);

	/**
	 * Performs yearly aggregation for the given event type.
	 * 
	 * @param eventName name of the event for which aggregation is required
	 * @param startDate start date for event filtering; date is inclusive; ignored if null.
	 * @param endDate end date for event filtering; date is exclusive; ignored if null.
	 */
	void aggregatePerYear(String eventName, Date startDate, Date endDate);
}
