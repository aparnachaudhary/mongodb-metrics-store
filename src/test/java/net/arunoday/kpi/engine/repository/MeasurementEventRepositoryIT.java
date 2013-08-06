/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

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

	@Autowired
	private MeasurementEventRepository repository;

	@Before
	public void before() {
		repository.deleteAll();
	}

	@Test
	public void testSave() {
		MeasurementEventEntity event = new MeasurementEventEntity();
		event.setOccuredOn(new Date());
		event.setName("request-home");
		ContextData data = new ContextData();
		data.put("request", "localhost");
		data.put("memory", RandomUtils.nextInt());
		event.setContextData(data);
		repository.save(event);

		assertEquals("Expected one event to be stored", 1, repository.count());
	}

	@Test
	public void testFindOne() {
		MeasurementEventEntity event = new MeasurementEventEntity();
		event.setOccuredOn(new Date());
		event.setName("request-home");
		ContextData data = new ContextData();
		data.put("request", "localhost");
		data.put("memory", RandomUtils.nextInt());
		event.setContextData(data);
		event = repository.save(event);

		assertEquals("Expected different event ", event, repository.findOne(event.getId()));
		assertNull("Expected no event matching the key", repository.findOne("non-existent"));
	}
	
}
