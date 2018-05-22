package com.simscale.main.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;

import com.simscale.main.model.CallEntry;
import com.simscale.main.model.LogLinkedList;
import com.simscale.main.util.ConverterService;

public class LogProcessor {

	private static Logger logger = Logger.getLogger( "LogProcessor" );

	private HashMap<String, LogLinkedList> entries = new HashMap<>(  );

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
			while ((line = br.readLine()) != null)
				processLine( line );
		}
	}

	private void processLine(String line){
		String[] values =  line.split( " " );
		CallEntry callEntry = mapFromValuesToCallEntry( values );
		if(!entries.containsKey( callEntry.getId() ))
			entries.put( callEntry.getId(), new LogLinkedList(callEntry.getId()) );
		LogLinkedList logLinkedList = entries.get( callEntry.getId());
		logLinkedList.addCall( callEntry );
		if(logLinkedList.isRootFound()) {
			logger.info( "Terminou " + logLinkedList.getId() );
			logLinkedList.print();
			entries.remove( logLinkedList.getId() );
		}

	}

	private CallEntry mapFromValuesToCallEntry(String[] values){
		int index = 0;
		CallEntry callEntry = new CallEntry();
		callEntry.setStart( ConverterService.convertFromStringToTimestamp( values[index++] ) );
		callEntry.setEnd( ConverterService.convertFromStringToTimestamp( values[index++] ) );
		callEntry.setId( values[index++] );
		callEntry.setService( values[index++] );
		String span = values[index++];
		String[] spanValues = span.split( "->" );
		callEntry.setCaller(spanValues[0]);
		callEntry.setCallee( spanValues[1] );
		return callEntry;
	}
}
