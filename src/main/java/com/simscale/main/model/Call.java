package com.simscale.main.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.simscale.main.util.InstantJsonSerializer;

public class Call {
	@JsonSerialize(using = InstantJsonSerializer.class)
	private Instant start;
	@JsonSerialize(using = InstantJsonSerializer.class)
	private Instant end;
	private String service;
	private String span;
	private Set<Call> calls = new HashSet<>(  );

	public Call(String service, Instant start, Instant end) {
		this.service = service;
		this.start = start;
		this.end = end;
	}

	public Instant getStart() {
		return start;
	}

	public void setStart(Instant start) {
		this.start = start;
	}

	public Instant getEnd() {
		return end;
	}

	public void setEnd(Instant end) {
		this.end = end;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSpan() {
		return span;
	}

	public void setSpan(String span) {
		this.span = span;
	}

	public Set<Call> getCalls() {
		return calls;
	}

	public void setCalls(Set<Call> calls) {
		this.calls = calls;
	}

	public void addCallEntry(Call call) {
		if (calls == null)
			calls = new HashSet<>();
		calls.add( call );
	}


	@Override public String toString() {
		return "Call{" +
				"service='" + service + '\'' +
				'}';
	}
}
