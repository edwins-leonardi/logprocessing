package com.simscale.main;

import java.util.logging.Logger;

import com.simscale.main.log.LogProcessor;

public class LogProcessingApp {
	private static Logger logger = Logger.getLogger( "LogProcessingApp" );
	public static void main(String[] args) {
		if(args.length < 1) {
			logger.info( "This program should be called with at least one file name argument" );
			System.exit( 1 );
		}
		logger.info("Starting LogProcessing App >>>");
		LogProcessor processor = new LogProcessor();
		processor.processLogFile( args[0] );
	}
}
