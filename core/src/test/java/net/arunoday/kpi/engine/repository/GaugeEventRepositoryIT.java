/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import static net.arunoday.kpi.engine.entity.MetricOperation.AVG;
import static net.arunoday.kpi.engine.entity.MetricOperation.MAX;
import static net.arunoday.kpi.engine.entity.MetricOperation.MIN;
import static net.arunoday.kpi.engine.entity.MetricOperation.SUM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arunoday.kpi.engine.entity.ContextData;
import net.arunoday.kpi.engine.entity.GaugeEventEntity;

import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Tests {@link GaugeEventRepository}
 * 
 * @author Aparna Chaudhary
 * 
 */
public class GaugeEventRepositoryIT extends AbstractRepositoryIT {

	private static final String EVENT_TYPE_HOMEREQUEST = "home_response_time";

	@Autowired
	private GaugeEventRepository<String> repository;

	@Before
	public void before() {
		cleanUpDB(GaugeEventRepository.EVENT_COLLECTION);
	}

	@Test
	public void testSave() {
		GaugeEventEntity event = new GaugeEventEntity(new Date(), EVENT_TYPE_HOMEREQUEST, RandomUtils.nextDouble());

		ContextData data = new ContextData();
		data.put("request", "localhost");
		event.setContextData(data);
		repository.save(event);

		assertEquals("Expected one event to be stored", 1, repository.count(event.getEventType()));
	}

	@Test
	public void testSaveWithNestedContextData() {
		GaugeEventEntity event = new GaugeEventEntity(new Date(), EVENT_TYPE_HOMEREQUEST, RandomUtils.nextDouble());

		ContextData data = new ContextData();
		data.put("request", "localhost");
		Map<String, String> innerData = new HashMap<String, String>();
		innerData.put("ID1", "VALUE1");
		innerData.put("ID2", "VALUE2");
		data.put("data", innerData);

		event.setContextData(data);
		event = repository.save(event);

		assertEquals("Expected one event to be stored", 1, repository.count(event.getEventType()));

		GaugeEventEntity retrievedEvent = repository.findOne(event.getId(), event.getEventType());
		assertEquals("", retrievedEvent.getContextData(), event.getContextData());
	}

	@Test
	public void testFindOne() {
		GaugeEventEntity event = new GaugeEventEntity(new Date(), EVENT_TYPE_HOMEREQUEST, RandomUtils.nextDouble());

		ContextData data = new ContextData();
		data.put("request", "localhost");
		data.put("memory", RandomUtils.nextInt());
		event.setContextData(data);
		event = repository.save(event);

		assertEquals("Expected different event ", event, repository.findOne(event.getId(), EVENT_TYPE_HOMEREQUEST));
		assertNull("Expected no event matching the key", repository.findOne("non-existent", EVENT_TYPE_HOMEREQUEST));
	}

	@Test
	public void testFindWithCriteria() {

		DateTime startDate = new DateTime("2013-08-10T16:00:00.389Z");
		storeEvents(25, startDate, "localhost");
		storeEvents(20, startDate, "someremotemachine");

		List<GaugeEventEntity> results = (List<GaugeEventEntity>) repository.find(EVENT_TYPE_HOMEREQUEST,
				Criteria.where("contextData.request").is("localhost"), startDate.toDate(), startDate.plusSeconds(10).toDate(),
				3);
		assertEquals(startDate.toDate(), results.get(0).getOccuredOn());
		assertEquals("", 3, results.size());

		results = (List<GaugeEventEntity>) repository.find(EVENT_TYPE_HOMEREQUEST, Criteria.where("contextData.index")
				.is(2), startDate.toDate(), startDate.plusSeconds(10).toDate(), 3);
		assertEquals(startDate.plusSeconds(2).toDate(), results.get(0).getOccuredOn());
		assertEquals("", 2, results.size());

	}

	@Test
	public void testFindEventTypes() {

		GaugeEventEntity event1 = new GaugeEventEntity(new Date(), "request1", RandomUtils.nextDouble());
		GaugeEventEntity event2 = new GaugeEventEntity(new Date(), "request2", RandomUtils.nextDouble());

		repository.save(event1);
		repository.save(event2);

		assertEquals("Unexpected event types", Arrays.asList("request1_gauge_events", "request2_gauge_events"),
				repository.findEventTypes());
	}

	@Test
	public void testAggregateOperations() {
		DateTime startDate = new DateTime("2013-08-10T16:00:00.389Z");
		for (int i = 1; i < 25; i++) {
			GaugeEventEntity event = new GaugeEventEntity(startDate.plusSeconds(i).toDate(), EVENT_TYPE_HOMEREQUEST, i);
			ContextData data = new ContextData();
			data.put("request", "somerequest");
			data.put("index", i);
			event.setContextData(data);
			event = repository.save(event);
		}

		double minValue = repository.performAggregation(EVENT_TYPE_HOMEREQUEST, MIN, startDate.toDate(), startDate
				.plusSeconds(25).toDate());
		assertEquals("Unexpected Min value", 1, minValue, 0.00);

		double maxValue = repository.performAggregation(EVENT_TYPE_HOMEREQUEST, MAX, startDate.toDate(), startDate
				.plusSeconds(25).toDate());
		assertEquals("Unexpected Max value", 24, maxValue, 0.00);

		double avgValue = repository.performAggregation(EVENT_TYPE_HOMEREQUEST, AVG, startDate.toDate(), startDate
				.plusSeconds(25).toDate());
		assertEquals("Unexpected Max value", 12.5, avgValue, 0.00);

		double sum = repository.performAggregation(EVENT_TYPE_HOMEREQUEST, SUM, startDate.toDate(),
				startDate.plusSeconds(25).toDate());
		assertEquals("Unexpected Max value", 300, sum, 0.00);

		assertEquals("Unexpected Max value", 300,
				repository.performAggregation(EVENT_TYPE_HOMEREQUEST, SUM, startDate.toDate(), null), 0.00);

		assertEquals("Unexpected Max value", 300,
				repository.performAggregation(EVENT_TYPE_HOMEREQUEST, SUM, null, startDate.plusSeconds(25).toDate()), 0.00);

		assertEquals("Unexpected Max value", 300, repository.performAggregation(EVENT_TYPE_HOMEREQUEST, SUM, null, null),
				0.00);
	}

	private void storeEvents(int count, DateTime startDate, String requestType) {
		for (int i = 0; i < count; i++) {
			GaugeEventEntity event = new GaugeEventEntity(startDate.plusSeconds(i).toDate(), EVENT_TYPE_HOMEREQUEST,
					RandomUtils.nextDouble());

			ContextData data = new ContextData();
			data.put("request", requestType);
			data.put("index", i);
			event.setContextData(data);
			event = repository.save(event);
		}
	}

}