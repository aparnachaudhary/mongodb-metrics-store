package net.arunoday.kpi.engine.repository;

import java.io.Serializable;

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

}
