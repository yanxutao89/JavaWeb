package com.yxt.crud.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternUtils {
	
	private static final Pattern COMMA_RIGHT_CURLY_BRACKET = Pattern.compile("(,}){1}");
	private static final Pattern COMMA_RIGHT_SQUARE_BRACKET = Pattern.compile("(,]){1}");
	private static final Pattern AT_LEAST_ONE_SPACE_OR_TAB_OR_RETURN_OR_NEWLINE =  Pattern.compile("\\s{1,}|\t{1,}|\r{1,}|\n{1,}");
	private static final Pattern AT_LEAST_ONE_SPACE =  Pattern.compile("\\s{1,}");
	
	public static String commaRightCurlyBracket(String message, String replacement) {
		
		if (StringUtils.isEmpty(message)) {
			return "";
		}
		
		return COMMA_RIGHT_CURLY_BRACKET.matcher(message).replaceAll(replacement);
	}
	
	public static String commaRightSquareBracket(String message, String replacement) {
		
		if (StringUtils.isEmpty(message)) {
			return "";
		}
		
		return COMMA_RIGHT_SQUARE_BRACKET.matcher(message).replaceAll(replacement);
	}

	public static String atLeastOneSpace(String msg){

		if (msg == null) {
			return "";
		}

		msg = AT_LEAST_ONE_SPACE_OR_TAB_OR_RETURN_OR_NEWLINE.matcher(msg).replaceAll(" ");
		return AT_LEAST_ONE_SPACE.matcher(msg).replaceAll(" ");
	}

	public static List<String> getPossibleTableNames(String formattedSql, String type) {
		List<String> tableNames = new ArrayList<>(7);
		formattedSql = atLeastOneSpace(formattedSql).toLowerCase();
		int leftIndex, rightIndex;
		if ("select".equals(type)) {

			leftIndex = formattedSql.indexOf(" from ");
			while (leftIndex != -1) {
				rightIndex = formattedSql.indexOf(" ", leftIndex + 6);
				if (rightIndex != -1) {
					String tableName = formattedSql.substring(leftIndex + 6, rightIndex).trim();
					if (!"(".equals(tableName)) {
						tableNames.add(tableName);
					}
				}
				leftIndex = formattedSql.indexOf(" from ", rightIndex);
			}
		} else if ("insert".equals(type)) {

			leftIndex = formattedSql.indexOf("insert into ");
			if (leftIndex != -1) {
				rightIndex = formattedSql.indexOf(" ", leftIndex + 12);
				if (rightIndex != -1) {
					String tableName = formattedSql.substring(leftIndex + 12, rightIndex).trim();
					if (!"(".equals(tableName)) {
						tableNames.add(tableName);
					}
				}
			}
		} else if ("update".equals(type)) {

			leftIndex = formattedSql.indexOf("update ");
			if (leftIndex != -1) {
				rightIndex = formattedSql.indexOf(" ", leftIndex + 7);
				if (rightIndex != -1) {
					String tableName = formattedSql.substring(leftIndex + 7, rightIndex).trim();
					if (!"(".equals(tableName)) {
						tableNames.add(tableName);
					}
				}
			}
		} else if ("delete".equals(type)) {

			leftIndex = formattedSql.indexOf("delete from ");
			if (leftIndex != -1) {
				rightIndex = formattedSql.indexOf(" ", leftIndex + 12);
				if (rightIndex != -1) {
					String tableName = formattedSql.substring(leftIndex + 12, rightIndex).trim();
					if (!"(".equals(tableName)) {
						tableNames.add(tableName);
					}
				}
			}
		}

		return tableNames;
	}

}
