package com.simscale.main.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LogTracer {
	private String id;
	private Call root;
	@JsonIgnore
	private Map<String, Set<Call>> callerMap = new HashMap<>();

	private Map<String, Set<Call>> calleeMap = new HashMap<>();

	public LogTracer(String traceId) {
		this.id = traceId;
	}

	public String getId() {
		return id;
	}

	public void addCall(LogEntry logEntry) {

		init( logEntry );
		Call call = createCaller( logEntry );

		addCallerCall( logEntry.getCaller(), call );
		addCalleeCall( logEntry.getCallee(), call );

		updateCallerFromCalleeId( logEntry.getCallee(), call );
		updateCalleeFromCallerId( logEntry.getCaller(), call );

		if (logEntry.getCaller().equals( "null" ))
			root = call;
	}

	private void addCallerCall(String callerId, Call caller) {
		Set<Call> calls = callerMap.get( callerId );
		calls.add( caller );
	}

	private Call createCaller(LogEntry logEntry) {
		Call caller = new Call( logEntry.getService(), logEntry.getStart(), logEntry.getEnd() );
		caller.setSpan( logEntry.getCallee() );
		return caller;
	}

	private void init(LogEntry logEntry) {
		if (!callerMap.containsKey( logEntry.getCaller() ))
			callerMap.put( logEntry.getCaller(), new HashSet<>() );

		if (!calleeMap.containsKey( logEntry.getCallee() ))
			calleeMap.put( logEntry.getCallee(), new HashSet<>() );
	}

	@JsonIgnore
	public boolean isRootFound() {
		return root != null;
	}

	public Call getRoot() {
		return root;
	}

	private void updateCallerFromCalleeId(String calleeId, Call caller) {
		Set<Call> callees = callerMap.get( calleeId );
		if (callees != null) {
			callees.forEach( c -> {
				caller.addCallEntry( c );
			} );
		}
	}

	private void addCalleeCall(String calleeId, Call caller) {
		if (!calleeMap.containsKey( calleeId ))
			calleeMap.put( calleeId, new HashSet<>() );
		Set<Call> calleeCall = calleeMap.get( calleeId );
		calleeCall.add( caller );
	}


	private void updateCalleeFromCallerId(String callerId, Call caller) {
		if (calleeMap.containsKey( callerId )) {
			Set<Call> calls = calleeMap.get( callerId );
			calls.forEach( c -> {
				c.addCallEntry( caller );
			} );
		}
	}
}
