package com.simscale.main.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.simscale.main.util.InstantJsonSerializer;

public class LogEntry {
	@JsonSerialize(using = InstantJsonSerializer.class)
	private Instant start;
	@JsonSerialize(using = InstantJsonSerializer.class)
	private Instant end;
	private String service;
	private String traceId;
	private String caller;
	private String callee;
	private Set<LogEntry> calls;
	private boolean used;

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

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getCallee() {
		return callee;
	}

	public void setCallee(String callee) {
		this.callee = callee;
	}

	public Set<LogEntry> getCalls() {
		return calls;
	}
	public void setCalls(Set<LogEntry> calls) {
		this.calls = calls;
	}

	public void addCallEntry(LogEntry logEntry){
		if(calls == null)
			calls = new HashSet<>(  );
		calls.add( logEntry );
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	@Override public String toString() {
		return "LogEntry{" +
				"start=" + start +
				", end=" + end +
				", service='" + service + '\'' +
				", traceId='" + traceId + '\'' +
				", caller='" + caller + '\'' +
				", callee='" + callee + '\'' +
				'}';
	}
}
