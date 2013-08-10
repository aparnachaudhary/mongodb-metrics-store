/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.arunoday.kpi.engine.entity.ContextData;
import net.arunoday.kpi.engine.entity.MeasurementEventEntity;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests {@link MeasurementEventRepository}
 * 
 * @author Aparna
 * 
 */
public class MeasurementEventRepositoryIT extends AbstractRepositoryIT {

	private static final String EVENT_TYPE_HOMEREQUEST = "request_home";

	@Autowired
	private MeasurementEventRepository<MeasurementEventEntity, String> repository;

	@Before
	public void before() {
		repository.deleteAll(EVENT_TYPE_HOMEREQUEST);
	}

	@Test
	public void testSave() {
		MeasurementEventEntity event = new MeasurementEventEntity();
		event.setOccuredOn(new Date());
		event.setEventType(EVENT_TYPE_HOMEREQUEST);
		ContextData data = new ContextData();
		data.put("request", "localhost");
		data.put("memory", RandomUtils.nextInt());
		event.setContextData(data);
		repository.save(event);

		assertEquals("Expected one event to be stored", 1, repository.count(event.getEventType()));
	}

	@Test
	public void testSaveWithNestedContextData() {
		MeasurementEventEntity event = new MeasurementEventEntity();
		event.setOccuredOn(new Date());
		event.setEventType(EVENT_TYPE_HOMEREQUEST);
		ContextData data = new ContextData();
		data.put("request", "localhost");
		data.put("memory", RandomUtils.nextInt());
		Map<String, String> innerData = new HashMap<String, String>();
		innerData.put("ID1", "VALUE1");
		innerData.put("ID2", "VALUE2");
		data.put("data", innerData);
		event.setContextData(data);
		event = repository.save(event);

		assertEquals("Expected one event to be stored", 1, repository.count(event.getEventType()));

		MeasurementEventEntity retrievedEvent = repository.findOne(event.getId(), event.getEventType());
		assertEquals("", retrievedEvent.getContextData(), event.getContextData());
	}

	@Test
	public void testFindOne() {
		MeasurementEventEntity event = new MeasurementEventEntity();
		event.setOccuredOn(new Date());
		event.setEventType(EVENT_TYPE_HOMEREQUEST);
		ContextData data = new ContextData();
		data.put("request", "localhost");
		data.put("memory", RandomUtils.nextInt());
		event.setContextData(data);
		event = repository.save(event);

		assertEquals("Expected different event ", event, repository.findOne(event.getId(), EVENT_TYPE_HOMEREQUEST));
		assertNull("Expected no event matching the key", repository.findOne("non-existent", EVENT_TYPE_HOMEREQUEST));
	}
	
}
