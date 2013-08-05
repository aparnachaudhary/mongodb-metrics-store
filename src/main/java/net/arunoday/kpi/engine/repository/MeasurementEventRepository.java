/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import net.arunoday.kpi.engine.entity.MeasurementEventEntity;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * MongoDB Repository for {@link MeasurementEventEntity}
 * 
 * @author Aparna
 * 
 */
public interface MeasurementEventRepository extends
		PagingAndSortingRepository<MeasurementEventEntity, Long> {

}
