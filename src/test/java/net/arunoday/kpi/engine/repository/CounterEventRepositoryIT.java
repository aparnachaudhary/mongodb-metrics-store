/**
 * 
 */
package net.arunoday.kpi.engine.repository;

import static org.junit.Assert.assertSame;

import java.util.Date;

import net.arunoday.kpi.engine.entity.CounterEventEntity;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests {@link CounterEventRepository}
 * 
 * @author Aparna
 * 
 */
public class CounterEventRepositoryIT extends AbstractRepositoryIT {

	@Autowired
	private CounterEventRepository repository;

	@Before
	public void before() {
		repository.deleteAll();
	}

	@Test
	public void testMinRequestCount() {
		long initialTotal = 10;
		CounterEventEntity event;
		for (int i = 0; i < 100; i++) {
			event = new CounterEventEntity();
			event.setOccuredOn(new Date());
			event.setName("request-home");
			event.setTotalCount(initialTotal + i);
			event = repository.save(event);
		}
		for (long i = 0; i < 10; i++) {
			event = new CounterEventEntity();
			event.setOccuredOn(new Date());
			event.setName("request2-home");
			event.setTotalCount(i);
			event = repository.save(event);
		}
		assertSame(10L, repository.retrieveMinCount("request-home"));
		assertSame(0L, repository.retrieveMinCount("request2-home"));
	}

}
