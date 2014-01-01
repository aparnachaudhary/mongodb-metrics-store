package net.arunoday.metric.store.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.arunoday.metric.store.entity.GaugeEventEntity;
import net.arunoday.metric.store.entity.HierarchialAggregationResult;
import net.arunoday.metric.store.entity.MetricResolution;

import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests {@link GaugeMetricRepository}
 * 
 * @author Aparna Chaudhary
 */
public class GaugeMetricRepositoryIT extends AbstractRepositoryIT {

	private static final String EVENT_TYPE = "test";

	@Autowired
	private GaugeMetricRepository<String> metricRepository;
	@Autowired
	private GaugeEventRepository<String> eventRepository;

	@Before
	public void before() {
		cleanUpDB(GaugeEventRepository.EVENT_COLLECTION);
	}

	@Test
	public void testMinuteWiseAggregation() {
		mongoOperations.dropCollection(EVENT_TYPE.concat(".minute"));
		DateTime now = new DateTime();
		DateTime ts = new DateTime(2013, 8, 10, 16, 01, 30);

		storeEvents(10, ts, EVENT_TYPE);
		metricRepository.aggregatePerMinute(EVENT_TYPE, ts.toDate(), now.toDate());
		List<HierarchialAggregationResult> result = (List<HierarchialAggregationResult>) metricRepository.find(EVENT_TYPE,
				MetricResolution.MINUTE, ts.minusMinutes(5).toDate(), now.toDate());

		assertEquals("Invalid aggregation result ", 1, result.size());
		assertEquals(10, (Double) result.get(0).get("count"), 0.00);

		storeEvents(2, ts.plusSeconds(40), EVENT_TYPE);
		metricRepository.aggregatePerMinute(EVENT_TYPE, ts.toDate(), now.toDate());
		result = (List<HierarchialAggregationResult>) metricRepository.find(EVENT_TYPE, MetricResolution.MINUTE, ts
				.minusMinutes(5).toDate(), now.toDate());

		assertEquals("Invalid aggregation result ", 2, result.size());
		assertEquals(10, (Double) result.get(0).get("count"), 0.00);
		assertEquals(2, (Double) result.get(1).get("count"), 0.00);
	}

	private void storeEvents(int count, DateTime startDate, String requestType) {
		for (int i = 0; i < count; i++) {
			GaugeEventEntity event = new GaugeEventEntity(startDate.plusSeconds(i).toDate(), requestType,
					RandomUtils.nextDouble());
			event = eventRepository.save(event);
		}
	}

}
