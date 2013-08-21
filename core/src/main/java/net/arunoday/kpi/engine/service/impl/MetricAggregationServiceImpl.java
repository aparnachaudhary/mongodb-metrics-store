package net.arunoday.kpi.engine.service.impl;

import net.arunoday.kpi.engine.entity.MetricResolution;
import net.arunoday.kpi.engine.repository.GaugeEventRepository;
import net.arunoday.kpi.engine.repository.GaugeMetricRepository;
import net.arunoday.kpi.engine.service.MetricAggregationService;

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
	public void performAggregation(MetricResolution resolution) {

	}

}
