package net.arunoday.kpi.engine.repository.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.arunoday.kpi.engine.entity.GaugeEventEntity;
import net.arunoday.kpi.engine.entity.MetricOperation;
import net.arunoday.kpi.engine.repository.GaugeEventRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.mongodb.DBObject;

/**
 * Default MongoDB implementation for {@link GaugeEventRepository}
 * 
 * @author Aparna
 * 
 */
@Repository
public class GaugeEventRepositoryImpl implements GaugeEventRepository<String> {

	private static final Logger logger = LoggerFactory.getLogger(GaugeEventRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public GaugeEventEntity save(GaugeEventEntity entity) {
		mongoTemplate.save(entity, getCollectionName(entity.getEventType()));
		return entity;
	}

	@Override
	public Iterable<GaugeEventEntity> save(Iterable<GaugeEventEntity> entities) {
		Assert.notNull(entities, "The given Iterable of entities can not be null!");
		List<GaugeEventEntity> result = new ArrayList<GaugeEventEntity>();
		for (GaugeEventEntity entity : entities) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	@Override
	public GaugeEventEntity findOne(String id, String eventType) {
		return mongoTemplate.findById(id, GaugeEventEntity.class, getCollectionName(eventType));
	}

	@Override
	public Iterable<GaugeEventEntity> findAll(String eventType) {
		return mongoTemplate.find(new Query(), GaugeEventEntity.class, getCollectionName(eventType));
	}

	@Override
	public Iterable<GaugeEventEntity> find(String eventType, Criteria criteria, Date startTime, Date endTime, int limit) {
		Query query = new Query(criteria);
		query.addCriteria(new Criteria("occuredOn").gte(startTime).lt(endTime));
		query.limit(limit);
		return mongoTemplate.find(query, GaugeEventEntity.class, getCollectionName(eventType));
	}

	@Override
	public Collection<String> findEventTypes() {
		List<String> collections = new ArrayList<String>();
		for (String collectionName : mongoTemplate.getCollectionNames()) {
			if (StringUtils.endsWithIgnoreCase(collectionName, EVENT_COLLECTION)) {
				collections.add(collectionName);
			}
		}
		return collections;
	}

	@Override
	public long count(String eventName) {
		return mongoTemplate.getCollection(getCollectionName(eventName)).count();
	}

	@Override
	public void delete(String id, String eventName) {
		mongoTemplate.remove(new Query(where("id").is(id)), getCollectionName(eventName));
	}

	@Override
	public void delete(GaugeEventEntity entity) {
		delete(entity.getId(), entity.getEventType());
	}

	@Override
	public void deleteAll(String eventName) {
		mongoTemplate.remove(new Query(), getCollectionName(eventName));
	}

	@Override
	public Double performAggregation(String eventName, MetricOperation metricOperation, Date startDate, Date endDate) {
		long lStartTime = System.currentTimeMillis();

		String valueField = "value";
		String eventType = "eventType";

		MatchOperation matchOperation = Aggregation.match(Criteria.where(eventType).is(eventName).and("occuredOn")
				.gte(startDate).lt(endDate));

		GroupOperation groupOperation = Aggregation.group(eventType).min(valueField)
				.as(MetricOperation.MIN.getOperation()).max(valueField).as(MetricOperation.MAX.getOperation())
				.avg(valueField).as(MetricOperation.AVG.getOperation()).sum(valueField)
				.as(MetricOperation.SUM.getOperation());

		AggregationOperation[] operations = { matchOperation, groupOperation, Aggregation.limit(1) };
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<DBObject> result = mongoTemplate.aggregate(aggregation, getCollectionName(eventName),
				DBObject.class);

		Double resultValue = (Double) result.getUniqueMappedResult().get(metricOperation.getOperation());

		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total Time performAggregation(): %s msec ", (lEndTime - lStartTime)));

		return resultValue;
	}

	protected String getCollectionName(String eventName) {
		Assert.notNull(eventName, "eventName must not be null!");
		return eventName.concat(EVENT_COLLECTION);
	}

}
