package com.simscale.main.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogTracer {
	private String id;
	private Call root;
	@JsonIgnore
	private Map<String, Set<Call>> entries = new HashMap<>();

	public LogTracer(String traceId) {
		this.id = traceId;
	}

	public String getId() {
		return id;
	}

	public void addCall(LogEntry logEntry) {

		if (!entries.containsKey( logEntry.getCaller() )){
			entries.put( logEntry.getCaller(), new HashSet<>(  ) );
		}

		Call caller = new Call( logEntry.getService(), logEntry.getStart(), logEntry.getEnd() );
		caller.setSpan( logEntry.getCallee() );
		Set<Call> calls = entries.get( logEntry.getCaller() );
		calls.add( caller );

		updateCallees( logEntry, caller );

		if (logEntry.getCaller().equals( "null" ))
			root = caller;
	}

	@JsonIgnore
	public boolean isRootFound() {
		return root != null;
	}

	public Call getRoot() {
		return root;
	}

	private void updateCallees(LogEntry logEntry, Call caller){
		Set<Call> callees = entries.get( logEntry.getCallee() );
		if(callees != null)
			callees.forEach( c -> {
				caller.addCallEntry( c );
			}  );
	}
}
