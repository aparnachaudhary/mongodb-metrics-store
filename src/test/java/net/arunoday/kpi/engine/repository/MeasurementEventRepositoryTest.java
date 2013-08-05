/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.arunoday.kpi.engine.entity.MeasurementEventEntity;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests {@link MeasurementEventRepository}
 * 
 * @author Aparna
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MeasurementEventRepositoryTest {

	@Autowired
	MeasurementEventRepository repository;

	@Before
	public void before() {
		repository.deleteAll();
	}

	@Test
	public void testSave() {
		MeasurementEventEntity event = new MeasurementEventEntity();
		event.setOccuredOn(new Date());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("request", "localhost");
		data.put("memory", RandomUtils.nextInt());
		event.setContextData(data);

		repository.save(event);
		assertEquals("Expected one event to be stored", 1, repository.count());
	}

}
