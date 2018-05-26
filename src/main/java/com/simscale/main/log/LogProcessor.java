package com.simscale.main.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalField;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simscale.main.model.LogEntry;
import com.simscale.main.model.LogTracer;
import com.simscale.main.util.ConverterService;

public class LogProcessor {

	private HashMap<String, LogTracer> entries = new HashMap<>();
	private Set<LogTracer> finishedEntries = new LinkedHashSet<>();
	private FileOutputStream outputFileStream;
	private int rowId = 0;
	private int numberOfTracesFound = 0;
	private double outputSizeMedian = 0;
	private String outFileName;

	public int processLogFile(String inputFileName, String outputFileName) {
		this.outFileName = outputFileName;
		BufferedReader br;
		try {
			validateFile( inputFileName );
			processFile( inputFileName );
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		} finally {
			if (outputFileStream != null) {
				try {
					outputFileStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	private void validateFile(String fileName) throws Exception {
		File file = new File( fileName );
		if (!file.exists())
			throw new Exception( fileName + " does not exist" );
	}

	private void processFile(String fileName) throws IOException {
		Scanner scan = new Scanner( new File( fileName ) );
		while (scan.hasNextLine()) {
			processLine( scan.nextLine() );
		}
		flushAllRemainingEntries();
		printStats();
	}

	private void printStats() {
		System.out.println( "************************* STATS **************************** " );
		System.out.println( "Number of lines read: " + rowId );
		System.out.println( "Number of traces found: " + numberOfTracesFound );
		System.out.println( "Number of orphans traces found: " + entries.size() );
		System.out.println( "JSON file size (number of chars) average: " + outputSizeMedian );
		System.out.println( "************************************************************" );
	}

	private void processLine(String line) {
		rowId++;
		String[] values = line.split( " " );
		LogEntry logEntry = mapFromValuesToCallEntry( values );
		if (!entries.containsKey( logEntry.getTraceId() ))
			entries.put( logEntry.getTraceId(), new LogTracer( logEntry.getTraceId() ) );
		LogTracer logTracer = entries.get( logEntry.getTraceId() );
		logTracer.addCall( logEntry );
		flushEntries( logTracer, logEntry.getStart() );
	}

	private void flushEntriesOlderThanOneSecond(Instant currentLogTime) {
		finishedEntries
				.stream()
				.filter( logTracer -> {
					Duration duration = Duration.between( logTracer.getRoot().getEnd(), currentLogTime );
					return duration.getSeconds() >= 1;
				} ).forEach( logTracer -> {
					print( logTracer );
					entries.remove( logTracer.getId() );
				}
		);
	}

	private void flushAllRemainingEntries() {
		for (LogTracer logTracer : finishedEntries) {
			print( logTracer );
			entries.remove( logTracer.getId() );
		}
	}

	private void flushEntries(LogTracer logTracer, Instant currentLogTime) {
		if (logTracer.isRootFound()) {
			numberOfTracesFound++;
			finishedEntries.add( logTracer );
		}
		if (rowId % 5 == 0)
			flushEntriesOlderThanOneSecond( currentLogTime );
	}

	public void print(LogTracer tracer) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonAsString = mapper.writeValueAsString( tracer );
			if (outputFileStream == null)
				outputFileStream = new FileOutputStream( outFileName );
			outputFileStream.write( jsonAsString.getBytes() );
			outputFileStream.write( "\n".getBytes() );
			addOutputSizeMedian( jsonAsString.length() );
			System.out.println( jsonAsString );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addOutputSizeMedian(int length) {
		if (outputSizeMedian == 0.0)
			outputSizeMedian = length;
		else
			outputSizeMedian = (outputSizeMedian + length) / 2;
	}

	private LogEntry mapFromValuesToCallEntry(String[] values) {
		int index = 0;
		LogEntry logEntry = new LogEntry();
		logEntry.setStart( ConverterService.convertFromStringToTimestamp( values[index++] ) );
		logEntry.setEnd( ConverterService.convertFromStringToTimestamp( values[index++] ) );
		logEntry.setTraceId( values[index++] );
		logEntry.setService( values[index++] );
		String span = values[index++];
		String[] spanValues = span.split( "->" );
		logEntry.setCaller( spanValues[0] );
		logEntry.setCallee( spanValues[1] );
		return logEntry;
	}
}
