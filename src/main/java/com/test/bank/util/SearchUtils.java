package com.test.bank.util;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.test.bank.filter.SearchCriteria;
import com.test.bank.filter.SearchOperation;

public class SearchUtils {

	private static final String PATTERN_QUERY_STRING = "((\\w|\\.)+?)(:|<|>)((\\w|(-|\\\"|:| ))+?);";

	/**
	 * Converts the search string into a list of search criterias
	 * 
	 * @param search
	 * @param entityClass
	 * @return list of search criterias
	 */
	public static List<SearchCriteria> queryToSearchCriterias(String search, Class<?> entityClass) {
		List<SearchCriteria> searchCriterias = new ArrayList<>();

		Pattern pattern = Pattern.compile(PATTERN_QUERY_STRING);
		Matcher matcher = pattern.matcher(search + ";");
		while (matcher.find()) {
			String name = matcher.group(1);
			SearchOperation operation = SearchOperation.fromString(matcher.group(3));
			String value = matcher.group(4);
			if (value.startsWith("\"") && value.endsWith("\"")) {
				value = value.replace("\"", "");
			}
			Class<?> clazz = SearchUtils.getFieldClass(name, entityClass);
			if (clazz != null) {
				Object obValue = SearchUtils.convertValue(value, clazz);
				if (obValue != null) {
					searchCriterias.add(new SearchCriteria(name, operation, obValue));
				}
			}
		}

		return searchCriterias;
	}

	@SuppressWarnings("unchecked")
	private static Object convertValue(String value, @SuppressWarnings("rawtypes") Class clazz) {
		if (clazz == null) {
			return null;
		}
		if (clazz.equals(String.class)) {
			return value;
		}
		if (clazz.equals(Integer.class)) {
			return Integer.valueOf(value);
		}
		if (clazz.equals(Double.class)) {
			return Double.valueOf(value);
		}
		if (clazz.equals(LocalDateTime.class)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			return LocalDateTime.parse(value, formatter);
		}
		if (clazz.isEnum()) {
			return Enum.valueOf(clazz, value);
		}
		return null;
	}

	private static Class<?> getFieldClass(String fieldName, Class<?> clazz) {
		String[] fieldNames = fieldName.split("\\.");
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getName().equals(fieldNames[0])) {
				if (fieldNames.length > 1) {
					return getFieldClass(fieldNames[1], field.getType());
				}
				return field.getType();
			}
		}
		return null;
	}

}
