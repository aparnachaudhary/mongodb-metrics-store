package net.arunoday.kpi.engine.repository.impl;

import java.util.Date;

import net.arunoday.kpi.engine.entity.GaugeEventEntity;
import net.arunoday.kpi.engine.repository.GaugeEventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Default MongoDB implementation for {@link GaugeEventRepository}
 * 
 * @author Aparna
 * 
 */
@Repository
public class GaugeEventRepositoryImpl implements GaugeEventRepository<String> {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public GaugeEventEntity save(GaugeEventEntity entity) {
		mongoTemplate.save(entity, getCollectionName(entity.getEventType()));
		return entity;
	}

	@Override
	public Iterable<GaugeEventEntity> save(Iterable<GaugeEventEntity> entities) {
		throw new UnsupportedOperationException();
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
	public Iterable<GaugeEventEntity> find(String eventType, Criteria criteria, Date startTime, Date endTime,
			int limit) {
		Query query = new Query(criteria);
		query.addCriteria(new Criteria("occuredOn").gte(startTime).lt(endTime));
		query.limit(limit);
		return mongoTemplate.find(query, GaugeEventEntity.class, getCollectionName(eventType));
	}

	@Override
	public long count(String eventName) {
		return mongoTemplate.getCollection(getCollectionName(eventName)).count();
	}

	@Override
	public void delete(String id, String eventName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(GaugeEventEntity entity) {
		delete(entity.getId(), entity.getEventType());
	}

	@Override
	public void delete(Iterable<GaugeEventEntity> entities) {
		Assert.notNull(entities, "The given Iterable of entities can not be null!");
		for (GaugeEventEntity entity : entities) {
			delete(entity);
		}

	}

	@Override
	public void deleteAll(String eventName) {
		mongoTemplate.remove(new Query(), getCollectionName(eventName));
	}

	protected String getCollectionName(String eventName) {
		Assert.notNull(eventName, "eventName must not be null!");
		return eventName.concat(EVENT_COLLECTION);
	}

}
