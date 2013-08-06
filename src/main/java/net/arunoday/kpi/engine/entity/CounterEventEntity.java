/**
 * 
 */
package net.arunoday.kpi.engine.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Counter event entity.
 * 
 * @author Aparna
 * 
 */
@Document(collection = "counter_events")
public class CounterEventEntity {

	@Id
	private String id;
	private Date occuredOn;
	private String name;
	private Long totalCount;
	private ContextData contextData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getOccuredOn() {
		return occuredOn;
	}

	public void setOccuredOn(Date occuredOn) {
		this.occuredOn = occuredOn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public ContextData getContextData() {
		return contextData;
	}

	public void setContextData(ContextData contextData) {
		this.contextData = contextData;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}
		CounterEventEntity that = (CounterEventEntity) obj;
		return this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {

		return getId().hashCode();
	}

}
