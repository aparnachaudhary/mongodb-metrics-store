/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import java.io.Serializable;
import java.util.Date;

import net.arunoday.kpi.engine.entity.GaugeEventEntity;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * MongoDB Repository for {@link GaugeEventEntity}
 * 
 * @author Aparna
 * 
 */
public interface GaugeEventRepository<T, ID extends Serializable> {

	static final String EVENT_COLLECTION = "_events";

	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	<S extends T> S save(S entity);

	/**
	 * Saves all given entities.
	 * 
	 * @param entities
	 * @return the saved entities
	 * @throws IllegalArgumentException in case the given entity is (@literal null}.
	 */
	<S extends T> Iterable<S> save(Iterable<S> entities);

	/**
	 * Retrieves an entity by its id and event name.
	 * 
	 * @param id must not be {@literal null}.
	 * @param eventType event name
	 * @return the entity with the given id or {@literal null} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	T findOne(ID id, String eventType);

	/**
	 * Returns all instances of the {@link GaugeEventEntity} by event type matching the filter criteria for the
	 * given time range.
	 * 
	 * @param eventType event name
	 * @param criteria for query execution
	 * @param startTime the start date for events, inclusive
	 * @param endTime the stop date for events, exclusive
	 * @param limit the maximum number of events to return; defaults to ten thousand
	 * @return matching event entities
	 */
	Iterable<T> find(String eventType, Criteria criteria, Date startTime, Date endTime, int limit);

	/**
	 * Returns all instances of the {@link GaugeEventEntity} by event type.
	 * 
	 * @param eventType event name
	 * @return all entities
	 */
	Iterable<T> findAll(String eventType);

	/**
	 * Returns the number of entities available for the given event name.
	 * 
	 * @param eventType event name
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
	void delete(ID id, String eventName);

	/**
	 * Deletes a given entity.
	 * 
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is (@literal null}.
	 */
	void delete(T entity);

	/**
	 * Deletes the given entities.
	 * 
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is (@literal null}.
	 */
	void delete(Iterable<? extends T> entities);

	/**
	 * Deletes all entities managed by the repository for the given event name.
	 * 
	 * @param eventType event name
	 */
	void deleteAll(String eventType);
}
