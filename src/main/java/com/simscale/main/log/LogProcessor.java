package com.simscale.main.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalField;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simscale.main.model.LogEntry;
import com.simscale.main.model.LogTracer;
import com.simscale.main.util.ConverterService;

public class LogProcessor {

	private static Logger logger = Logger.getLogger( "LogProcessor" );

	private HashMap<String, LogTracer> entries = new HashMap<>(  );
	private Set<LogTracer> finishedEntries = new LinkedHashSet<>(  );
	private FileOutputStream outputFileStream;
	private int rowId = 0;

	public int processLogFile(String fileName) {
		BufferedReader br;
		try {
			validateFile( fileName );
			processFile( fileName );
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	private void validateFile(String fileName) throws Exception {
		File file = new File( fileName );
		if (!file.exists())
			throw new Exception( fileName + " does not exist" );
	}

	private void processFile(String fileName) throws IOException {
		String line;
		try (BufferedReader br = new BufferedReader( new InputStreamReader(
				new FileInputStream( fileName ), "UTF-8" ) )) {
			while ((line = br.readLine()) != null) {
				processLine( line );
			}
			flushAllRemainingEntries();
		}
	}

	private void processLine(String line){
		String[] values =  line.split( " " );
		LogEntry logEntry = mapFromValuesToCallEntry( values );
		if(!entries.containsKey( logEntry.getTraceId() ))
			entries.put( logEntry.getTraceId(), new LogTracer( logEntry.getTraceId()) );
		LogTracer logTracer = entries.get( logEntry.getTraceId());
		logTracer.addCall( logEntry );
		flushEntries(logTracer, logEntry.getStart());
	}

	private void flushEntriesOlderThanOneSecond(Instant currentLogTime) {
		finishedEntries
				.stream()
				.filter( logTracer -> {
					Duration duration = Duration.between( logTracer.getRoot().getEnd(), currentLogTime );
					return duration.getSeconds() >= 1;
				}).map( logTracer -> {
					print( logTracer );
					entries.remove( logTracer.getId() );
					return null;
				});
	}

	private void flushAllRemainingEntries() {
		for (LogTracer logTracer : finishedEntries){
			print( logTracer );
			entries.remove( logTracer.getId() );
		}
	}

	private void flushEntries(LogTracer logTracer, Instant currentLogTime){
		if(logTracer.isRootFound()) {
			finishedEntries.add( logTracer );
		}
		if(rowId++ % 5 == 0)
			flushEntriesOlderThanOneSecond(currentLogTime);

	}

	public void print(LogTracer tracer) {
		ObjectMapper mapper = new ObjectMapper(  );
		try {
			String jsonAsString = mapper.writeValueAsString( tracer );
			if(outputFileStream == null)
				outputFileStream = new FileOutputStream("output.txt");
			outputFileStream.write( jsonAsString.getBytes() );
			outputFileStream.write( "\n".getBytes() );
			System.out.println(jsonAsString);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private LogEntry mapFromValuesToCallEntry(String[] values){
		int index = 0;
		LogEntry logEntry = new LogEntry();
		logEntry.setStart( ConverterService.convertFromStringToTimestamp( values[index++] ) );
		logEntry.setEnd( ConverterService.convertFromStringToTimestamp( values[index++] ) );
		logEntry.setTraceId( values[index++] );
		logEntry.setService( values[index++] );
		String span = values[index++];
		String[] spanValues = span.split( "->" );
		logEntry.setCaller(spanValues[0]);
		logEntry.setCallee( spanValues[1] );
		return logEntry;
	}
}
