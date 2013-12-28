package net.arunoday.kpi.engine.repository.impl;

import static net.arunoday.kpi.engine.entity.GaugeEventEntity.EVENT_TYPE_FIELD;
import static net.arunoday.kpi.engine.entity.GaugeEventEntity.OCCURED_ON_FIELD;

import java.util.Date;

import net.arunoday.kpi.engine.entity.AggregatedValue;
import net.arunoday.kpi.engine.entity.GaugeMetricEntity;
import net.arunoday.kpi.engine.repository.GaugeMetricRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
	@Qualifier("metricMongoTemplate")
	private MongoTemplate metricMongoTemplate;

	@Autowired
	@Qualifier("eventMongoTemplate")
	private MongoTemplate eventMongoTemplate;

	@Override
	public GaugeMetricEntity save(GaugeMetricEntity entity) {
		metricMongoTemplate.save(entity, getCollectionName(entity.getEventType()));
		return entity;
	}

	@Override
	public AggregationResults<DBObject> performAggregation(AggregationOperation[] operations) {
		long lStartTime = System.currentTimeMillis();

		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<DBObject> result = metricMongoTemplate.aggregate(aggregation, GaugeMetricEntity.class, DBObject.class);

		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total Time performAggregation(): %s msec ", (lEndTime - lStartTime)));

		return result;
	}

	@Override
	public void aggregatePerMinute(String eventName, Date startDate, Date endDate) {
		long lStartTime = System.currentTimeMillis();
		Criteria criteria = Criteria.where(EVENT_TYPE_FIELD).is(eventName);
		criteria = prepareDateCriteria(criteria, OCCURED_ON_FIELD, startDate, endDate);
		eventMongoTemplate.mapReduce(
				new Query(criteria),
				getEventCollectionName(eventName),
				"classpath:minute_map_function.js",
				"classpath:reduce_function.js",
				new MapReduceOptions().outputDatabase(metricMongoTemplate.getDb().getName())
						.outputCollection(eventName.concat(".minute")).outputTypeMerge()
						.finalizeFunction("classpath:finalize_function.js"), AggregatedValue.class);

		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total time aggregatePerMinute(): %s msec ", (lEndTime - lStartTime)));
	}

	@Override
	public void aggregatePerHour(String eventName, Date startDate, Date endDate) {
		long lStartTime = System.currentTimeMillis();

		Criteria criteria = new Criteria();
		criteria = prepareDateCriteria(criteria, "_id", startDate, endDate);
		metricMongoTemplate.mapReduce(new Query(criteria), eventName.concat(".minute"), "classpath:hourly_map_function.js",
				"classpath:reduce_function.js", new MapReduceOptions().outputCollection(eventName.concat(".hourly"))
						.outputTypeMerge().finalizeFunction("classpath:finalize_function.js"), AggregatedValue.class);

		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total time aggregatePerHour(): %s msec ", (lEndTime - lStartTime)));
	}

	@Override
	public void aggregatePerDay(String eventName, Date startDate, Date endDate) {
		long lStartTime = System.currentTimeMillis();

		Criteria criteria = new Criteria();
		criteria = prepareDateCriteria(criteria, "_id", startDate, endDate);
		metricMongoTemplate.mapReduce(new Query(criteria), eventName.concat(".hourly"), "classpath:daily_map_function.js",
				"classpath:reduce_function.js", new MapReduceOptions().outputCollection(eventName.concat(".daily"))
						.outputTypeMerge().finalizeFunction("classpath:finalize_function.js"), AggregatedValue.class);

		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total time aggregatePerDay(): %s msec ", (lEndTime - lStartTime)));
	}

	protected String getEventCollectionName(String eventName) {
		Assert.notNull(eventName, "eventName must not be null!");
		return eventName.concat(EVENT_COLLECTION);
	}

	private Criteria prepareDateCriteria(Criteria criteria, String dateField, Date startDate, Date endDate) {
		if (startDate != null && endDate != null) {
			criteria.andOperator(Criteria.where(dateField).gte(startDate), Criteria.where(dateField).lt(endDate));
		} else if (startDate != null) {
			criteria.andOperator(Criteria.where(dateField).gte(startDate));
		} else if (endDate != null) {
			criteria.andOperator(Criteria.where(dateField).lt(endDate));
		}
		logger.debug("Criteria used for aggregation : " + criteria.getCriteriaObject());
		return criteria;
	}

	protected String getCollectionName(String eventName) {
		Assert.notNull(eventName, "eventName must not be null!");
		return eventName.concat(METRICS_COLLECTION);
	}
}
