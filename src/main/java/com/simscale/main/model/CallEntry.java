package com.simscale.main.model;

import java.time.Instant;
import java.util.List;

public class CallEntry {
	private Instant start;
	private Instant end;
	private String service;
	private List<CallEntry> calls;
	private String id;
	private String caller;
	private String callee;

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

	public List<CallEntry> getCalls() {
		return calls;
	}

	public void setCalls(List<CallEntry> calls) {
		this.calls = calls;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override public String toString() {
		return "CallEntry{" +
				"start=" + start +
				", end=" + end +
				", service='" + service + '\'' +
				", id='" + id + '\'' +
				", caller='" + caller + '\'' +
				", callee='" + callee + '\'' +
				'}';
	}
}
