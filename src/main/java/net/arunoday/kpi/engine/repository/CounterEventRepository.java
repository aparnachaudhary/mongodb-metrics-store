package net.arunoday.kpi.engine.repository;

import java.io.Serializable;

import net.arunoday.kpi.engine.entity.CounterEventEntity;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import com.mongodb.DBObject;

/**
 * Defines custom repository operations for {@link CounterEventEntity}
 * 
 * @author Aparna
 * 
 */
public interface CounterEventRepository<ID extends Serializable> {

	/**
	 * Saves a given counter entity. Use the returned instance for further operations as the save operation might have
	 * changed the entity instance completely.
	 * 
	 * @param entity entity to save
	 * @param isDecrement counter is decremented if true; if false increment the existing counter by the current value
	 * @return the saved entity
	 */
	CounterEventEntity save(CounterEventEntity entity, boolean isDecrement);

	/**
	 * Retrieves an entity by its id and event name.
	 * 
	 * @param id must not be {@literal null}.
	 * @param eventType event name must not be {@literal null}.
	 * @return the entity with the given id or {@literal null} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	CounterEventEntity findOne(ID id, String eventType);

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
	void delete(CounterEventEntity entity);

	/**
	 * Deletes the given entities.
	 * 
	 * @param entities gauge events to be deleted
	 * @throws IllegalArgumentException in case the given {@link Iterable} is (@literal null}.
	 */
	void delete(Iterable<CounterEventEntity> entities);

	/**
	 * Deletes all entities managed by the repository for the given event name.
	 * 
	 * @param eventType event name must not be {@literal null}.
	 */
	void deleteAll(String eventType);

	/**
	 * Deletes all entities managed by the repository.
	 * 
	 */
	void deleteAll();

	/**
	 * Returns minimum value for count for given event name.
	 * 
	 * @param eventName name of the event for which min count is required
	 * @return min count for the given event
	 */
	Long retrieveMinCount(String eventName);

	/**
	 * Returns maximum value for count for given event name.
	 * 
	 * @param eventName name of the event for which max count is required
	 * @return max count for the given event
	 */
	Long retrieveMaxCount(String eventName);

	/**
	 * Performs aggregation based on the input aggregation operations.
	 * 
	 * @param operations aggregation operations like match, group, limit, sort
	 * @return result of aggregation
	 */
	AggregationResults<DBObject> performAggregation(AggregationOperation[] operations);

}
