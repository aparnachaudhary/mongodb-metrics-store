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
	private CounterEventRepository<String> repository;

	@Before
	public void before() {
		repository.deleteAll();
	}

	/**
	 * Tests the minimum value of count is returned correctly.
	 */
	@Test
	public void testMinRequestCount() {
		long initialTotal = 10;
		CounterEventEntity event;
		for (int i = 0; i < 100; i++) {
			event = new CounterEventEntity();
			event.setOccuredOn(new Date());
			event.setName("request-home");
			event.setTotalCount(initialTotal + i);
			event = repository.save(event, false);
		}
		for (long i = 0; i < 10; i++) {
			event = new CounterEventEntity();
			event.setOccuredOn(new Date());
			event.setName("request2-home");
			event.setTotalCount(i);
			event = repository.save(event, false);
		}
		assertSame(10L, repository.retrieveMinCount("request-home"));
		assertSame(0L, repository.retrieveMinCount("request2-home"));
	}

	/**
	 * Tests increment and decrement of counter values
	 */
	@Test
	public void testIncrementAndDecrement() {
		CounterEventEntity event = new CounterEventEntity();
		event.setOccuredOn(new Date());
		event.setName("request-home");
		event.setTotalCount(10L);
		event = repository.save(event, false);
		assertSame(10L, repository.retrieveMinCount("request-home"));

		// increment counter
		CounterEventEntity incrementCounter = new CounterEventEntity();
		incrementCounter.setOccuredOn(new Date());
		incrementCounter.setName("request-home");
		incrementCounter.setTotalCount(10L);
		incrementCounter = repository.save(incrementCounter, false);
		assertSame(10L, repository.retrieveMinCount("request-home"));
		assertSame(20L, repository.retrieveMaxCount("request-home"));

		// decrement counter
		CounterEventEntity decrementCounter = new CounterEventEntity();
		decrementCounter.setOccuredOn(new Date());
		decrementCounter.setName("request-home");
		decrementCounter.setTotalCount(5L);
		decrementCounter = repository.save(decrementCounter, true);
		assertSame(15L, decrementCounter.getTotalCount());
		assertSame(10L, repository.retrieveMinCount("request-home"));
		assertSame(20L, repository.retrieveMaxCount("request-home"));
	}

}
