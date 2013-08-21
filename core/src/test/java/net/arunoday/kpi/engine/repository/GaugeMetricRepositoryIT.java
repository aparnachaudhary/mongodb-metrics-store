package net.arunoday.kpi.engine.repository;

import static net.arunoday.kpi.engine.entity.MetricResolution.MINUTE;
import static org.junit.Assert.assertEquals;
import net.arunoday.kpi.engine.entity.GaugeMetricEntity;

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
		event.setResolution(MINUTE);
		event.setEventType("request1");

		repository.save(event);

		assertEquals("UnExpected Resolution ", MINUTE.getCode(), event.getResolution().getCode());
	}
}
