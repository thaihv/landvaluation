package org.jdvn.lis.valuation.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
@Table(name = "event")
@DynamicInsert
@DynamicUpdate
@Analyzer(definition = "synonyms")
@Indexed
public class Event extends DomainObject<Long> implements Comparable<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime eventTimestamp;
	private int severity;
	private String comment;

	/**
	 * Gets id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets id.
	 * 
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets eventTimestamp.
	 * 
	 * @return the eventTimestamp
	 */
	public LocalDateTime getEventTimestamp() {
		return eventTimestamp;
	}

	/**
	 * Sets eventTimestamp.
	 * 
	 * @param eventTimestamp the eventTimestamp to set
	 */
	public void setEventTimestamp(LocalDateTime eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	/**
	 * Gets severity.
	 * 
	 * @return the severity
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * Sets severity.
	 * 
	 * @param severity the severity to set
	 */
	public void setSeverity(int severity) {
		this.severity = severity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Event [id=" + id + ", description=" + description + ", eventTimestamp=" + eventTimestamp + ", severity="
				+ severity + "]";
	}

	/**
	 * Gets comment.
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets comment.
	 * 
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int compareTo(Event o) {
		return new CompareToBuilder()
				.append(getDescription(), o.getDescription())
				.append(getId(), o.getId())
				.toComparison();
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return toString();
	}
}
