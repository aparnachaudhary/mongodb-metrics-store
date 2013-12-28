package net.arunoday.kpi.engine.repository;

import java.io.Serializable;
import java.util.Date;

import net.arunoday.kpi.engine.entity.GaugeMetricEntity;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import com.mongodb.DBObject;

/**
 * Repository interface for {@link GaugeMetricEntity}
 * 
 * @author Aparna
 * 
 */
public interface GaugeMetricRepository<ID extends Serializable> {

	static final String METRICS_COLLECTION = "_gauge_metrics";
	static final String EVENT_COLLECTION = "_gauge_events";

	/**
	 * Saves a given metric entity. Use the returned instance for further operations as the save operation might have
	 * changed the entity instance completely.
	 * 
	 * @param entity gauge metric entity
	 * @return the saved entity
	 */
	GaugeMetricEntity save(GaugeMetricEntity entity);

	/**
	 * Performs aggregation based on the input aggregation operations.
	 * 
	 * @param operations aggregation operations like match, group, limit, sort
	 * @return result of aggregation
	 */
	AggregationResults<DBObject> performAggregation(AggregationOperation[] operations);

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

}
