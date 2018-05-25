package com.simscale.main;

import java.util.logging.Logger;

import com.simscale.main.log.LogProcessor;

public class LogProcessingApp {
	private static Logger logger = Logger.getLogger( "LogProcessingApp" );
	public static void main(String[] args) {
		if(args.length != 1) {
			logger.info( "This program should be called with one argument, the input file!" );
			System.exit( 1 );
		}
		logger.info("Starting LogProcessing App >>>");
		processLogs( args );
	}

	private static void processLogs(String args[]){
		if(args.length == 1)
			processLogsFromFile( args[0] );
		else
			processLogsFromStandarInput();
	}

	private static void processLogsFromFile(String fileName){
		LogProcessor processor = new LogProcessor();
		processor.processLogFile( fileName );
	}

	private static void processLogsFromStandarInput(){
		System.out.println("TODO");
	}

}
