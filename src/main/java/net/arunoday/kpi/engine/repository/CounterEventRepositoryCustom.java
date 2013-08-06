package net.arunoday.kpi.engine.repository;

import net.arunoday.kpi.engine.entity.CounterEventEntity;

/**
 * Defines custom repository operations for {@link CounterEventEntity}
 * 
 * @author Aparna
 * 
 */
public interface CounterEventRepositoryCustom {
	
	Long retrieveMinCount(String eventName);
}
