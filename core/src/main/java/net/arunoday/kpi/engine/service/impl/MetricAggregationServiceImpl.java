package net.arunoday.kpi.engine.service.impl;

import static net.arunoday.kpi.engine.entity.MetricOperation.AVG;
import static net.arunoday.kpi.engine.entity.MetricOperation.MAX;
import static net.arunoday.kpi.engine.entity.MetricOperation.MIN;
import static net.arunoday.kpi.engine.entity.MetricOperation.SUM;
import static net.arunoday.kpi.engine.entity.MetricResolution.MINUTE;
import net.arunoday.kpi.engine.entity.AggregationResult;
import net.arunoday.kpi.engine.entity.GaugeMetricEntity;
import net.arunoday.kpi.engine.repository.GaugeEventRepository;
import net.arunoday.kpi.engine.repository.GaugeMetricRepository;
import net.arunoday.kpi.engine.service.MetricAggregationService;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link MetricAggregationService}
 * 
 * @author Aparna Chaudhary
 * 
 */
@Service
public class MetricAggregationServiceImpl implements MetricAggregationService {

	@Autowired
	private GaugeEventRepository<String> gaugeEventRepository;
	@Autowired
	private GaugeMetricRepository<String> gaugeMetricRepository;

	@Override
	public void performAggregationPerMinute() {
		DateTime startDate = new DateTime();
		for (String eventType : gaugeEventRepository.findEventTypes()) {

			AggregationResult aggregationResult = gaugeEventRepository.performAggregation(eventType,
					startDate.minusSeconds(60).toDate(), startDate.toDate());

			GaugeMetricEntity event = new GaugeMetricEntity(eventType, MINUTE.getCode(), startDate.toDate(),
					MIN.getOperation(), aggregationResult.getMin());
			gaugeMetricRepository.save(event);

			event = new GaugeMetricEntity(eventType, MINUTE.getCode(), startDate.toDate(), MAX.getOperation(),
					aggregationResult.getMax());
			gaugeMetricRepository.save(event);

			event = new GaugeMetricEntity(eventType, MINUTE.getCode(), startDate.toDate(), AVG.getOperation(),
					aggregationResult.getAvg());
			gaugeMetricRepository.save(event);

			event = new GaugeMetricEntity(eventType, MINUTE.getCode(), startDate.toDate(), SUM.getOperation(),
					aggregationResult.getSum());
			gaugeMetricRepository.save(event);
		}

	}
}
