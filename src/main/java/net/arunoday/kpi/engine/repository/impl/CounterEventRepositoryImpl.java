package net.arunoday.kpi.engine.repository.impl;

import net.arunoday.kpi.engine.entity.CounterEventEntity;
import net.arunoday.kpi.engine.repository.CounterEventRepositoryCustom;

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

import com.mongodb.DBObject;

/**
 * Custom repository implementation for {@link CounterEventRepositoryCustom}
 * 
 * @author Aparna
 * 
 */
public class CounterEventRepositoryImpl implements CounterEventRepositoryCustom {

	private static final Logger logger = LoggerFactory.getLogger(CounterEventRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

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
