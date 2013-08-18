package net.arunoday.kpi.engine.repository.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import net.arunoday.kpi.engine.entity.CounterEventEntity;
import net.arunoday.kpi.engine.repository.CounterEventRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

import com.mongodb.DBObject;

/**
 * Default MongoDB implementation for {@link CounterEventRepository}
 * 
 * @author Aparna
 * 
 */
@Repository
public class CounterEventRepositoryImpl implements CounterEventRepository<String> {

	private static final Logger logger = LoggerFactory.getLogger(CounterEventRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	public CounterEventEntity save(CounterEventEntity entity, boolean isDecrement) {
		Assert.notNull(entity, "The given entity can not be null!");

		Query query = new Query(where("name").is(entity.getName()));
		query = query.with(new Sort(Sort.Direction.DESC, "occuredOn"));
		query.limit(1);

		CounterEventEntity existingEntity = mongoTemplate.findOne(query, CounterEventEntity.class);
		Long existingCount = 0L;
		if(existingEntity != null){
			existingCount = existingEntity.getTotalCount();
		}
		if (isDecrement) {
			entity.setTotalCount(existingCount - entity.getTotalCount());
		} else {
			entity.setTotalCount(existingCount + entity.getTotalCount());
		}
		mongoTemplate.save(entity);
		return entity;
	}

	@Override
	public CounterEventEntity findOne(String id, String eventType) {
		return mongoTemplate.findById(id, CounterEventEntity.class);
	}

	@Override
	public void delete(String id, String eventName) {
		mongoTemplate.remove(new Query(where("id").is(id).and("name").is(eventName)));
	}

	@Override
	public void delete(CounterEventEntity entity) {
		delete(entity.getId(), entity.getName());
	}

	@Override
	public void delete(Iterable<CounterEventEntity> entities) {
		Assert.notNull(entities, "The given Iterable of entities can not be null!");
		for (CounterEventEntity entity : entities) {
			delete(entity);
		}

	}

	@Override
	public void deleteAll(String eventName) {
		mongoTemplate.remove(new Query(where("name").is(eventName)), CounterEventEntity.class);
	}

	@Override
	public void deleteAll() {
		mongoTemplate.remove(new Query(), CounterEventEntity.class);
	}

	@Override
	public Long retrieveMinCount(String eventName) {
		long lStartTime = System.currentTimeMillis();

		MatchOperation matchOperation = Aggregation.match(Criteria.where("name").is(eventName));
		GroupOperation groupOperation = Aggregation.group("name").min("totalCount").as("minCount");

		AggregationOperation[] operations = { matchOperation, groupOperation, Aggregation.limit(1) };
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<DBObject> result = mongoTemplate
				.aggregate(aggregation, CounterEventEntity.class, DBObject.class);
		Long minTotal = (Long) result.getUniqueMappedResult().get("minCount");
		
		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total Time retrieveMinCount(): %s msec ", (lEndTime - lStartTime)));

		return minTotal;
	}

	@Override
	public Long retrieveMaxCount(String eventName) {
		long lStartTime = System.currentTimeMillis();

		MatchOperation matchOperation = Aggregation.match(Criteria.where("name").is(eventName));
		GroupOperation groupOperation = Aggregation.group("name").max("totalCount").as("maxCount");

		AggregationOperation[] operations = { matchOperation, groupOperation, Aggregation.limit(1) };
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<DBObject> result = mongoTemplate
				.aggregate(aggregation, CounterEventEntity.class, DBObject.class);
		Long maxTotal = (Long) result.getUniqueMappedResult().get("maxCount");

		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total Time retrieveMaxCount(): %s msec ", (lEndTime - lStartTime)));

		return maxTotal;
	}

	@Override
	public AggregationResults<DBObject> performAggregation(AggregationOperation[] operations) {
		long lStartTime = System.currentTimeMillis();

		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<DBObject> result = mongoTemplate
				.aggregate(aggregation, CounterEventEntity.class, DBObject.class);

		long lEndTime = System.currentTimeMillis();
		logger.debug(String.format("Total Time performAggregation(): %s msec ", (lEndTime - lStartTime)));

		return result;
	}

}
