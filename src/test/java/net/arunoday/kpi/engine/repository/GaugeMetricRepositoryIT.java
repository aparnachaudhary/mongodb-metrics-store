package net.arunoday.kpi.engine.repository;

import static org.junit.Assert.assertEquals;
import net.arunoday.kpi.engine.entity.GaugeMetricEntity;
import net.arunoday.kpi.engine.entity.MetricResolution;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests {@link GaugeMetricRepository}
 * 
 * @author Aparna Chaudhary
 * 
 */
public class GaugeMetricRepositoryIT extends AbstractRepositoryIT {

	@Autowired
	private GaugeMetricRepository<String> repository;

	@Before
	public void before() {
		cleanUpDB(GaugeMetricRepository.METRICS_COLLECTION);
	}

	@Test
	public void testSave() {
		GaugeMetricEntity event = new GaugeMetricEntity();
		event.setResolution(MetricResolution.MINUTE);
		event.setEventType("request1");

		repository.save(event);

		assertEquals("UnExpected Resolution ", MetricResolution.MINUTE.getCode(), event.getResolution().getCode());
	}
}
