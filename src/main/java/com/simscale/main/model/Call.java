package com.simscale.main.model;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.simscale.main.util.InstantJsonSerializer;

public class Call implements Comparable{
	@JsonSerialize(using = InstantJsonSerializer.class)
	private Instant start;
	@JsonSerialize(using = InstantJsonSerializer.class)
	private Instant end;
	private String service;
	private String span;
	private static final Comparator<Call> COMPARATOR = Comparator.comparing( (Call c) -> c.end )
			.thenComparing( c->c.start )
			.thenComparing( c->c.service )
			.thenComparing( c->c.span );
	private SortedSet<Call> calls = new TreeSet<>( COMPARATOR );

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

	public void setCalls(SortedSet<Call> calls) {
		this.calls = calls;
	}

	public void addCallEntry(Call call) {
		if (calls == null)
			calls = new TreeSet<>();
		calls.add( call );
	}

	@Override public int compareTo(Object o) {
		if (this == o)
			return 0;
		Call other = (Call) o;
		return this.end.compareTo( other.end );
	}

	@Override public String toString() {
		return "Call{" +
				"service='" + service + '\'' +
				'}';
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Call call = (Call) o;
		return Objects.equals( start, call.start ) &&
				Objects.equals( end, call.end ) &&
				Objects.equals( service, call.service ) &&
				Objects.equals( span, call.span );
	}

	@Override public int hashCode() {
		return Objects.hash( start, end, service, span );
	}
}
