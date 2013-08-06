/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import net.arunoday.kpi.engine.entity.CounterEventEntity;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Aparna
 * 
 */
public interface CounterEventRepository extends PagingAndSortingRepository<CounterEventEntity, String>,
		CounterEventRepositoryCustom {

}
