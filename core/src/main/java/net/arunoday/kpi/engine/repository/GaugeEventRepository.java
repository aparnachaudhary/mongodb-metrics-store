/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import net.arunoday.kpi.engine.entity.AggregationResult;
import net.arunoday.kpi.engine.entity.GaugeEventEntity;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * MongoDB Repository for {@link GaugeEventEntity}
 * 
 * @author Aparna
 * 
 */
public interface GaugeEventRepository<ID extends Serializable> {

	static final String EVENT_COLLECTION = "_gauge_events";

	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity gauge event entity
	 * @return the saved entity
	 */
	GaugeEventEntity save(GaugeEventEntity entity);

	/**
	 * Saves all given entities.
	 * 
	 * @param entities gauge event entities to be stored
	 * @return the saved entities
	 * @throws IllegalArgumentException in case the given entity is (@literal null}.
	 */
	Iterable<GaugeEventEntity> save(Iterable<GaugeEventEntity> entities);

	/**
	 * Retrieves an entity by its id and event name.
	 * 
	 * @param id must not be {@literal null}.
	 * @param eventType event name must not be {@literal null}.
	 * @return the entity with the given id or {@literal null} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	GaugeEventEntity findOne(ID id, String eventType);

	/**
	 * Returns all instances of the {@link GaugeEventEntity} by event type matching the filter criteria for the given time
	 * range.
	 * 
	 * @param eventType event name must not be {@literal null}.
	 * @param criteria for query execution
	 * @param startTime the start date for events, inclusive
	 * @param endTime the stop date for events, exclusive
	 * @param limit the maximum number of events to return; defaults to ten thousand
	 * @return matching event entities
	 */
	Iterable<GaugeEventEntity> find(String eventType, Criteria criteria, Date startTime, Date endTime, int limit);

	/**
	 * Returns all instances of the {@link GaugeEventEntity} by event type.
	 * 
	 * @param eventType event name
	 * @return all entities
	 */
	Iterable<GaugeEventEntity> findAll(String eventType);

	/**
	 * Returns stored event types
	 * 
	 * @return collection of stored event types
	 */
	Collection<String> findEventTypes();

	/**
	 * Returns the number of entities available for the given event name.
	 * 
	 * @param eventType event name must not be {@literal null}.
	 * @return the number of entities
	 */
	long count(String eventType);

	/**
	 * Deletes the entity with the given id and event name
	 * 
	 * @param id must not be {@literal null}.
	 * @param eventType event name must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	void delete(ID id, String eventType);

	/**
	 * Deletes a given entity.
	 * 
	 * @param entity gauge event to delete
	 * @throws IllegalArgumentException in case the given entity is (@literal null}.
	 */
	void delete(GaugeEventEntity entity);

	/**
	 * Deletes all entities managed by the repository for the given event name.
	 * 
	 * @param eventType event name must not be {@literal null}.
	 */
	void deleteAll(String eventType);

	/**
	 * Performs aggregation based for supported metric operations.
	 * 
	 * @param eventName name of the event for which aggregation is required
	 * @param startDate start date for event filtering; date is inclusive; ignored if null.
	 * @param endDate end date for event filtering; date is exclusive; ignored if null.
	 * @return result of aggregation
	 */
	AggregationResult performAggregation(String eventName, Date startDate, Date endDate);

	/**
	 * Performs hourly aggregation for the given event type.
	 * 
	 * @param eventName name of the event for which aggregation is required
	 * @param startDate start date for event filtering; date is inclusive; ignored if null.
	 * @param endDate end date for event filtering; date is exclusive; ignored if null.
	 */
	void aggregatePerHour(String eventName, Date startDate, Date endDate);

}
