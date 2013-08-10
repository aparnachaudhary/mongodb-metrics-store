package net.arunoday.kpi.engine.repository.impl;

import net.arunoday.kpi.engine.entity.MeasurementEventEntity;
import net.arunoday.kpi.engine.repository.MeasurementEventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Default MongoDB implementation for {@link MeasurementEventRepository}
 * 
 * @author Aparna
 * 
 */
@Repository
public class MeasurementEventRepositoryImpl implements MeasurementEventRepository<MeasurementEventEntity, String> {

	@Autowired
	private MongoTemplate mongoTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public MeasurementEventEntity save(MeasurementEventEntity entity) {
		mongoTemplate.save(entity, getCollectionName(entity.getEventType()));
		return entity;
	}

	@Override
	public <S extends MeasurementEventEntity> Iterable<S> save(Iterable<S> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MeasurementEventEntity findOne(String id, String eventType) {
		return mongoTemplate.findById(id, MeasurementEventEntity.class, getCollectionName(eventType));
	}

	@Override
	public boolean exists(String id, String eventType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<MeasurementEventEntity> findAll(String eventType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<MeasurementEventEntity> findAll(Iterable<String> ids) {
		throw new UnsupportedOperationException();
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
	public void delete(MeasurementEventEntity entity) {
		delete(entity.getId(), entity.getEventType());
	}

	@Override
	public void delete(Iterable<? extends MeasurementEventEntity> entities) {
		Assert.notNull(entities, "The given Iterable of entities not be null!");
		for (MeasurementEventEntity entity : entities) {
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
