package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {
	private static final String SPLIT_CHAR = ",";

	@Override
	public String convertToDatabaseColumn(Set<String> stringSet) {
		return String.join(SPLIT_CHAR, stringSet);
	}

	@Override
	public Set<String> convertToEntityAttribute(String string) {
		if (string == null) {
			return null;
		}
		String[] strArray = string.split(SPLIT_CHAR);
		return new HashSet<>(Arrays.asList(strArray));
	}

}
