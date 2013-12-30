package net.arunoday.metric.store.repository.impl;

import static net.arunoday.metric.store.entity.GaugeEventEntity.EVENT_TYPE_FIELD;
import static net.arunoday.metric.store.entity.GaugeEventEntity.OCCURED_ON_FIELD;
import static net.arunoday.metric.store.entity.GaugeEventEntity.VALUE_FIELD;
import static net.arunoday.metric.store.entity.MetricOperation.AVG;
import static net.arunoday.metric.store.entity.MetricOperation.MAX;
import static net.arunoday.metric.store.entity.MetricOperation.MIN;
import static net.arunoday.metric.store.entity.MetricOperation.SUM;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.arunoday.metric.store.entity.AggregationResult;
import net.arunoday.metric.store.entity.GaugeEventEntity;
import net.arunoday.metric.store.repository.GaugeEventRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * @author Aparna Chaudhary
 */
@Repository
public class GaugeEventRepositoryImpl implements GaugeEventRepository<String> {

	private static final Logger logger = LoggerFactory.getLogger(GaugeEventRepositoryImpl.class);

	@Autowired
	@Qualifier("eventMongoTemplate")
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
		query.addCriteria(new Criteria(OCCURED_ON_FIELD).gte(startTime).lt(endTime));
		query.limit(limit);
		return mongoTemplate.find(query, GaugeEventEntity.class, getCollectionName(eventType));
	}

	@Override
	public Collection<String> findEventTypes() {
		List<String> collections = new ArrayList<String>();
		for (String collectionName : mongoTemplate.getCollectionNames()) {
			if (StringUtils.endsWithIgnoreCase(collectionName, EVENT_COLLECTION)) {
				collections.add(StringUtils.delete(collectionName, EVENT_COLLECTION));
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
	public AggregationResult performAggregation(String eventName, Date startDate, Date endDate) {
		long lStartTime = System.currentTimeMillis();

		Criteria criteria = Criteria.where(EVENT_TYPE_FIELD).is(eventName);
		if (startDate != null && endDate != null) {
			criteria.andOperator(Criteria.where(OCCURED_ON_FIELD).gte(startDate), Criteria.where(OCCURED_ON_FIELD)
					.lt(endDate));
		} else if (startDate != null) {
			criteria.andOperator(Criteria.where(OCCURED_ON_FIELD).gte(startDate));
		} else if (endDate != null) {
			criteria.andOperator(Criteria.where(OCCURED_ON_FIELD).lt(endDate));
		}
		MatchOperation matchOperation = Aggregation.match(criteria);

		GroupOperation groupOperation = Aggregation.group(EVENT_TYPE_FIELD).min(VALUE_FIELD).as(MIN.getOperation())
				.max(VALUE_FIELD).as(MAX.getOperation()).avg(VALUE_FIELD).as(AVG.getOperation()).sum(VALUE_FIELD)
				.as(SUM.getOperation());

		AggregationOperation[] operations = { matchOperation, groupOperation, Aggregation.limit(1) };
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<DBObject> result = mongoTemplate.aggregate(aggregation, getCollectionName(eventName),
				DBObject.class);

		logger.debug("Criteria used for aggregation : " + criteria.getCriteriaObject());
		DBObject mappedResult = result.getUniqueMappedResult();

		AggregationResult aggregationResult = new AggregationResult();
		if (mappedResult != null) {
			if (mappedResult.get(MIN.getOperation()) != null) {
				aggregationResult.setMin((Double) mappedResult.get(MIN.getOperation()));
			}
			if (mappedResult.get(MAX.getOperation()) != null) {
				aggregationResult.setMax((Double) mappedResult.get(MAX.getOperation()));
			}
			if (mappedResult.get(SUM.getOperation()) != null) {
				aggregationResult.setSum((Double) mappedResult.get(SUM.getOperation()));
			}
			if (mappedResult.get(AVG.getOperation()) != null) {
				aggregationResult.setAvg((Double) mappedResult.get(AVG.getOperation()));
			}
		}

		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total Time performAggregation(): %s msec ", (lEndTime - lStartTime)));

		return aggregationResult;
	}

	protected String getCollectionName(String eventName) {
		Assert.notNull(eventName, "eventName must not be null!");
		return eventName.concat(EVENT_COLLECTION);
	}

}
