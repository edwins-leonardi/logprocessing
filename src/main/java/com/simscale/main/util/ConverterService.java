package com.simscale.main.util;

import java.time.Instant;

public class ConverterService {
	public static Instant convertFromStringToTimestamp(String value){
		return Instant.parse( value );
	}
}
