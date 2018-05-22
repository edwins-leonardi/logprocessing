package com.simscale.main.log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtils {
	public static BufferedReader readFile(String fileName) {
		try {
			return new BufferedReader( new InputStreamReader(
					new FileInputStream( fileName ), "UTF-8" ) );
		} catch (Exception e) {
			throw new RuntimeException( fileName + " not found" );
		}
	}
}
