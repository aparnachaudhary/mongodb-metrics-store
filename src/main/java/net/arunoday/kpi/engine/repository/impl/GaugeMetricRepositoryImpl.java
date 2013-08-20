package net.arunoday.kpi.engine.repository.impl;

import net.arunoday.kpi.engine.entity.GaugeMetricEntity;
import net.arunoday.kpi.engine.repository.GaugeMetricRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.mongodb.DBObject;

/**
 * Default MongoDB implementation for {@link GaugeMetricRepository}
 * 
 * @author Aparna
 * 
 */
@Repository
public class GaugeMetricRepositoryImpl implements GaugeMetricRepository<String> {

	private static final Logger logger = LoggerFactory.getLogger(GaugeMetricRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public GaugeMetricEntity save(GaugeMetricEntity entity) {
		mongoTemplate.save(entity, getCollectionName(entity.getEventType()));
		return entity;
	}

	@Override
	public AggregationResults<DBObject> performAggregation(AggregationOperation[] operations) {
		long lStartTime = System.currentTimeMillis();

		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<DBObject> result = mongoTemplate.aggregate(aggregation, GaugeMetricEntity.class, DBObject.class);

		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total Time performAggregation(): %s msec ", (lEndTime - lStartTime)));

		return result;
	}

	protected String getCollectionName(String eventName) {
		Assert.notNull(eventName, "eventName must not be null!");
		return eventName.concat(METRICS_COLLECTION);
	}
}
