package net.arunoday.metric.store.service.impl;

import net.arunoday.metric.store.entity.AggregatedValue;
import net.arunoday.metric.store.entity.MetricResolution;
import net.arunoday.metric.store.repository.GaugeMetricRepository;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aparna Chaudhary
 */
@RestController
public class GaugeMetricRestService {

	@Autowired
	private GaugeMetricRepository<String> metricRepository;

	@RequestMapping(value = "/metric/minute/{eventId}/{year}/{month}/{day}/{hour}/{minute}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<AggregatedValue> listMinuteAggregations(@PathVariable("eventId") String eventId,
			@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day,
			@PathVariable("hour") int hour, @PathVariable("minute") int minute) {
		if (year == 0) {
			throw new IllegalArgumentException("'year' cannot be zero.");
		}
		// TODO: Date validations
		DateTime startDate = new DateTime(year, month, day, hour, minute);
		DateTime endDate = new DateTime(year, month, day, hour, minute);
		if (minute != 0) {
			endDate = startDate.plusMinutes(1);
		}
		return metricRepository.find(eventId, MetricResolution.MINUTE, startDate.toDate(), endDate.toDate());
	}

	@RequestMapping(value = "/metric/minute/{eventId}/{year}/{month}/{day}/{hour}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<AggregatedValue> listMinuteAggregations(@PathVariable("eventId") String eventId,
			@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day,
			@PathVariable("hour") int hour) {
		if (year == 0) {
			throw new IllegalArgumentException("'year' cannot be zero.");
		}
		DateTime startDate = new DateTime(year, month, day, hour, 0);
		DateTime endDate = new DateTime(year, month, day, hour, 0);
		if (hour != 0) {
			endDate = startDate.plusHours(1);
		}
		return metricRepository.find(eventId, MetricResolution.MINUTE, startDate.toDate(), endDate.toDate());
	}

	@RequestMapping(value = "/metric/hourly/{eventId}/{year}/{month}/{day}/{hour}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<AggregatedValue> listHourlyAggregations(@PathVariable("eventId") String eventId,
			@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day,
			@PathVariable("hour") int hour) {
		// FIXME
		if (year == 0) {
			throw new IllegalArgumentException("'year' cannot be zero.");
		}
		DateTime startDate = new DateTime(year, month, day, hour, 0);
		DateTime endDate = new DateTime(year++, month, day, hour, 0);
		if (hour != 0) {
			endDate = startDate.plusHours(1);
		}
		return metricRepository.find(eventId, MetricResolution.HOUR, startDate.toDate(), endDate.toDate());
	}

	@RequestMapping(value = "/metric/hourly/{eventId}/{year}/{month}/{day}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<AggregatedValue> listHourlyAggregations(@PathVariable("eventId") String eventId,
			@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day) {
		// FIXME
		if (year == 0) {
			throw new IllegalArgumentException("'year' cannot be zero.");
		}
		DateTime startDate = new DateTime(year, month, day, 0, 0);
		DateTime endDate = new DateTime(year++, month, day, 0, 0);
		if (day != 0) {
			endDate = startDate.plusDays(1);
		}
		return metricRepository.find(eventId, MetricResolution.HOUR, startDate.toDate(), endDate.toDate());
	}

	/**
	 * @param eventId
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	@RequestMapping(value = "/metric/daily/{eventId}/{year}/{month}/{day}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<AggregatedValue> listDailyAggregations(@PathVariable("eventId") String eventId,
			@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day) {
		// FIXME
		if (year == 0) {
			throw new IllegalArgumentException("'year' cannot be zero.");
		}
		DateTime startDate = new DateTime(year, month, day, 0, 0);
		DateTime endDate = new DateTime(year, month, day, 0, 0);

		return metricRepository.find(eventId, MetricResolution.DAY, startDate.toDate(), endDate.toDate());
	}

	/**
	 * @param eventId
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping(value = "/metric/monthly/{eventId}/{year}/{month}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<AggregatedValue> listMonthlyAggregations(@PathVariable("eventId") String eventId,
			@PathVariable("year") int year, @PathVariable("month") int month) {
		// FIXME
		if (year == 0) {
			throw new IllegalArgumentException("'year' cannot be zero.");
		}
		DateTime startDate = new DateTime(year, month, 0, 0, 0);
		DateTime endDate = new DateTime(year++, month, 0, 0, 0);
		if (month != 0) {
			endDate = startDate.plusMonths(1);
		}
		return metricRepository.find(eventId, MetricResolution.MONTH, startDate.toDate(), endDate.toDate());
	}

	/**
	 * @param eventId
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "/metric/yearly/{eventId}/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<AggregatedValue> listYearlyAggregations(@PathVariable("eventId") String eventId,
			@PathVariable("year") int year) {
		if (year == 0) {
			throw new IllegalArgumentException("'year' cannot be zero.");
		}
		DateTime startDate = new DateTime(year, 0, 0, 0, 0);
		return metricRepository.find(eventId, MetricResolution.YEAR, startDate.toDate(), startDate.plusYears(1).toDate());
	}
}
