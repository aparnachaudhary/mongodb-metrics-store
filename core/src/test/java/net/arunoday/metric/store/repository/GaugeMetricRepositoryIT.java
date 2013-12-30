package net.arunoday.metric.store.repository;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests {@link GaugeMetricRepository}
 * 
 * @author Aparna Chaudhary
 */
public class GaugeMetricRepositoryIT extends AbstractRepositoryIT {

	@Autowired
	private GaugeMetricRepository<String> repository;

	@Before
	public void before() {
		cleanUpDB(GaugeMetricRepository.METRICS_COLLECTION);
	}

	@Test
	public void test() {
		assertTrue(true);
	}

}
