package net.arunoday.kpi.engine.repository;

import net.arunoday.kpi.engine.entity.CounterEventEntity;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import com.mongodb.DBObject;

/**
 * Defines custom repository operations for {@link CounterEventEntity}
 * 
 * @author Aparna
 * 
 */
public interface CounterEventRepositoryCustom {

	/**
	 * Returns minimum value for count for given event name.
	 * 
	 * @param eventName name of the event for which min count is required
	 * @return min count for the given event
	 */
	Long retrieveMinCount(String eventName);

	/**
	 * Performs aggregation based on the input aggregation operations.
	 * 
	 * @param operations aggregation operations like match, group, limit, sort
	 * @return result of aggregation
	 */
	AggregationResults<DBObject> performAggregation(AggregationOperation[] operations);

}
