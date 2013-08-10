/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import java.io.Serializable;

import net.arunoday.kpi.engine.entity.MeasurementEventEntity;

/**
 * MongoDB Repository for {@link MeasurementEventEntity}
 * 
 * @author Aparna
 * 
 */
public interface MeasurementEventRepository<T, ID extends Serializable> {
	
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
	 * Returns whether an entity with the given id and event name exists.
	 * 
	 * @param id must not be {@literal null}.
	 * @param eventType event name
	 * @return true if an entity with the given id exists, {@literal false} otherwise
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	boolean exists(ID id, String eventType);

	/**
	 * Returns all instances of the {@link MeasurementEventEntity} by event type.
	 * 
	 * @param eventType event name
	 * @return all entities
	 */
	Iterable<T> findAll(String eventType);

	/**
	 * Returns all instances of the type with the given IDs.
	 * 
	 * @param ids
	 * @return
	 */
	Iterable<T> findAll(Iterable<ID> ids);

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


