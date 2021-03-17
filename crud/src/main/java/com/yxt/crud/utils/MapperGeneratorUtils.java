package com.yxt.crud.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class MapperGeneratorUtils {

    private static final Map<String, String> MYSQL_JDBC_TYPE = new HashMap();

    static {
        MYSQL_JDBC_TYPE.put("java.lang.Boolean", "BIT");
        MYSQL_JDBC_TYPE.put("byte[]", "BIT");
        MYSQL_JDBC_TYPE.put("java.lang.Integer", "INTEGER");
        MYSQL_JDBC_TYPE.put("java.lang.Long", "BIGINT");
        MYSQL_JDBC_TYPE.put("java.lang.Float", "FLOAT");
        MYSQL_JDBC_TYPE.put("java.lang.Double", "DOUBLE");
        MYSQL_JDBC_TYPE.put("java.math.BigDecimal", "DECIMAL");
        MYSQL_JDBC_TYPE.put("java.sql.Date", "DATE");
        MYSQL_JDBC_TYPE.put("java.sql.Timestamp", "TIMESTAMP");
        MYSQL_JDBC_TYPE.put("java.sql.Time", "TIME");
        MYSQL_JDBC_TYPE.put("java.lang.String", "VARCHAR");
    }

    private static final String HEADER_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">";
    private static final String ENTITY = "KeyValue";
    private static final String FILENAME = ENTITY + "Mapper";
    private static final String NAMESPACE = "com.yxt.crud.mapper." + FILENAME;
    private static final String ENTITY_TYPE = "com.yxt.curd.bean." + ENTITY;
//    private static final String URL = "jdbc:mysql://127.0.0.1:3306/java_web?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://192.168.159.128:3306/java_web?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "yxt123";
    private static final String TABLE_NAME = "key_value";
	private static final List<List<String>> ENTITY_FIELDS = new ArrayList<>();

    public static void main(String[] args){

        MapperGeneratorUtils mapperGenerator = new MapperGeneratorUtils();

        List<List<String>> columnList = mapperGenerator.connect(URL, USERNAME, PASSWORD, TABLE_NAME);

        List<String> list = new LinkedList<>();
        list.add(HEADER_CONTENT);

        // nameSpace
        String nameSpace = new StringBuffer("<mapper namespace=\"" + NAMESPACE + "\">").toString();
        list.add(nameSpace);
        System.out.println("nameSpace is done");

        // resultMap
        String resultMap = mapperGenerator.generateBaseResultMap(columnList);
        list.add(resultMap);
        System.err.println("BaseResultMap is done");

        // BaseColumnList
        String baseColumnList = mapperGenerator.generateBaseColumnList(columnList);
        list.add(baseColumnList);
        System.out.println("BaseColumnList is done");

        // select
        String select = mapperGenerator.generateSelect(columnList);
        list.add(select);
        System.err.println("select is done");

        // insert
        String insert = mapperGenerator.generateInsert(columnList);
        list.add(insert);
        System.out.println("insert is done");

        // insertSelective
        String insertSelective = mapperGenerator.generateInsertSelective(columnList);
        list.add(insertSelective);
        System.err.println("insertSelective is done");

        // insertBatch
        String insertBatch = mapperGenerator.generateInsertBatch(columnList);
        list.add(insertBatch);
        System.out.println("insertBatch is done");

        // update
        String update = mapperGenerator.generateUpdate(columnList);
        list.add(update);
        System.err.println("update is done");

        // updateSelective
        String updateSelective = mapperGenerator.generateUpdateSelective(columnList);
        list.add(updateSelective);
        System.out.println("updateSelective is done");

        // updateBatch
        String updateBatch = mapperGenerator.generateUpdateBatch(columnList, columnList.get(0).get(0));
        list.add(updateBatch);
        System.err.println("updateBatch is done");

        // updateBatchSelective
        String updateBatchSelective = mapperGenerator.generateUpdateBatchSelective(columnList, columnList.get(0).get(0));
        list.add(updateBatchSelective);
        System.err.println("updateBatchSelective is done");

        list.add("</mapper>");

        String filePath = FILENAME + ".xml";
        File mapper = new File(filePath);
        FileWriter fileWriter = null;

        try {

            fileWriter = new FileWriter(mapper);
            for (int i = 0; i < list.size(); i++) {
                fileWriter.write(list.get(i) + "\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mapperGenerator.generateEntity();
    }

    public List<List<String>> connect(String url, String username, String password, String TABLE_NAME) {

        List<List<String>> columns = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, username, password);
			String sql = "select * from " + TABLE_NAME + " limit 0, 1";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			ResultSetMetaData metaData = statement.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i + 1);
                String columnClassName = metaData.getColumnClassName(i + 1);
                List<String> column = new ArrayList<>(2);
				column.add(0, columnName);
				column.add(1, MYSQL_JDBC_TYPE.get(columnClassName));
				columns.add(column);
				List<String> field = new ArrayList<>(2);
				field.add(0, underScore2Camel(columnName));
				field.add(1, columnClassName.substring(columnClassName.lastIndexOf(".") + 1));
				ENTITY_FIELDS.add(field);
			}
			conn.close();
			rs.close();
			statement.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return columns;

	}

    private String generateBaseResultMap(List<List<String>> columnList) {

        StringBuffer sb = new StringBuffer("\t<resultMap id=\"BaseResultMap\" type=\"" + ENTITY_TYPE + "\">\n");

        for (int i = 0; i < columnList.size(); i++) {
        	if (i == 0) {
        		sb.append("\t\t<id property=\"" + underScore2Camel(columnList.get(i).get(0)));
        	} else {
        		sb.append("\t\t<result property=\"" + underScore2Camel(columnList.get(i).get(0)));
			}

            sb.append("\" column=\"" + columnList.get(i).get(0) + "\" jdbcType=\""+ columnList.get(i).get(1) +"\"/>\n");
        }

        sb.append("\t</resultMap>");

        return sb.toString();

    }

    private String generateBaseColumnList(List<List<String>> columnList) {

        StringBuffer sb = new StringBuffer("\t<sql id=\"BaseColumnList\">\n");

        for (int i = 0; i < columnList.size(); i++) {
            if (i == columnList.size() - 1) {
                sb.append("\t\t" + columnList.get(i).get(0) + "\n");
            } else {
                sb.append("\t\t" + columnList.get(i).get(0) + ",\n");
            }
        }

        sb.append("\t</sql>");

        return sb.toString();

    }

    private String generateSelect(List<List<String>> columnList) {

        StringBuffer sb = new StringBuffer("\t<select id=\"select" + ENTITY + "\" parameterType=\"java.util.Map\" resultMap=\"BaseResultMap\">\n");
        sb.append("\t\tSELECT\n");
        for (int i = 0; i < columnList.size(); i++) {
            if (i == columnList.size() - 1) {
                sb.append("\t\t" + columnList.get(i).get(0) + "\n");
            } else {
                sb.append("\t\t" + columnList.get(i).get(0) + ",\n");
            }
        }
        sb.append("\t\tFROM " + TABLE_NAME + "\n")
            .append("\t\tWHERE 1 = 1\n")
            .append("\t\t" + toColumnIf( columnList.get(0).get(0), "and " +  columnList.get(0).get(0) + " = ", "", columnList.get(0).get(1)).replace("},,", "}") + "\n")
            .append("\t</select>");

        return sb.toString();

    }

    private String generateInsert(List<List<String>> columnList) {

        StringBuffer sb = new StringBuffer("\t<insert id=\"insert" + ENTITY + "\">\n" +
                "\t\tINSERT INTO "+ TABLE_NAME + "\n" +
                "\t\t(\n");

        for (int i = 0; i < columnList.size(); i++) {
            if (i == columnList.size() - 1) {
                sb.append("\t\t\t" + columnList.get(i).get(0) + "\n");
            } else {
                sb.append("\t\t\t" + columnList.get(i).get(0) + ",\n");
            }
        }

        sb.append("\t\t)\n")
                .append("\t\tVALUES\n")
                .append("\t\t(\n");

        for (int i = 0; i < columnList.size(); i++) {

            String value = columnToValue(columnList.get(i).get(0), columnList.get(i).get(1));
            if (i == columnList.size() - 1) {
                sb.append("\t\t\t" + value.replace("},", "}") + "\n");
            } else {
                sb.append("\t\t\t" + value + "\n");
            }
        }

        sb.append("\t\t" + ")" + "\n").append("\t</insert>");

        return sb.toString();

    }

    private String generateInsertSelective(List<List<String>> columnList) {

        StringBuffer sb = new StringBuffer("\t<insert id=\"insert" + ENTITY + "Selective\">\n" +
                "\t\tINSERT INTO " + TABLE_NAME + "\n" +
                "\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\n");

        for (int i = 0; i < columnList.size(); i++) {
            sb.append("\t\t\t" + toColumnIf(columnList.get(i).get(0), columnList.get(i).get(1)) + "\n");
        }

        sb.append("\t\t</trim>\n");

        sb.append("\t\t<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\" >\n");

        for (int i = 0; i < columnList.size(); i++) {
            sb.append("\t\t\t" + toValueIf(columnList.get(i).get(0), columnList.get(i).get(1)) + "\n");
        }

        sb.append("\t\t</trim>\n");
        sb.append("\t</insert>");

        return sb.toString();

    }

    private String generateInsertBatch(List<List<String>> columnList) {

        StringBuffer sb = new StringBuffer("\t<insert id=\"insert" + ENTITY + "Batch\" parameterType=\"java.util.List\">\n" +
                "\t\tINSERT INTO " + TABLE_NAME + "\n");

        sb.append("\t\t(\n");
        for (int i = 0; i < columnList.size(); i++) {
            if (i == columnList.size() - 1) {
                sb.append("\t\t\t" + columnList.get(i).get(0) + "\n");
            } else {
                sb.append("\t\t\t" + columnList.get(i).get(0) + ",\n");
            }
        }
        sb.append("\t\t)\n");
        sb.append("\t\tVALUES\n");
        sb.append("\t\t<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n");
        sb.append("\t\t(\n");

        for (int i = 0; i < columnList.size(); i++) {
            String value = columnToValue(columnList.get(i).get(0), columnList.get(i).get(1));
            if (i == columnList.size() - 1) {
                sb.append("\t\t\t" + value.replace("},", "}") + "\n");
            }else {
                sb.append("\t\t\t" + value + "\n");
            }
        }

        sb.append("\t\t)\n");
        sb.append("\t\t</foreach>\n");
        sb.append("\t</insert>");

        return sb.toString();

    }

    private String generateUpdate(List<List<String>> columnList) {

        StringBuffer sb = new StringBuffer("\t<update id=\"update" + ENTITY + "\">\n" +
                "\t\tUPDATE " + TABLE_NAME + "\n" +
                "\t\tSET\n");

        for (int i = 0; i < columnList.size(); i++) {
            if (i == 0) {
                continue;
            }
            sb.append("\t\t\t" + columnList.get(i).get(0) + "=");
            String value = columnToValue(columnList.get(i).get(0), columnList.get(i).get(1));

            if (i == columnList.size() - 1) {
                sb.append(value.replace("},", "}") + "\n");
            } else {
                sb.append(value + "\n");
            }
        }

        sb.append("\t\tWHERE\n");
        sb.append("\t\t\t"+ columnList.get(0).get(0) + "=" + columnToValue(columnList.get(0).get(0), columnList.get(0).get(1)).replace("},", "}") + "\n");
        sb.append("\t</update>");

        return sb.toString();

    }

    private String generateUpdateSelective(List<List<String>> columnList) {

        StringBuffer sb = new StringBuffer("\t<update id=\"update" + ENTITY + "Selective\">\n" +
                "\t\tUPDATE " + TABLE_NAME + "\n" +
                "\t\t<set>\n");
        for (int i = 0; i < columnList.size(); i++) {
            if (i == 0) {
                continue;
            }
            sb.append("\t\t\t" + toColumnIf(columnList.get(i).get(0), "", columnToValue(columnList.get(i).get(0), "=", columnList.get(i).get(1)), columnList.get(i).get(1)).replace(",,", ",").replace("#{=", "=#{") + "\n");
        }

        sb.append("\t\t</set>\n");
        sb.append("\t\tWHERE\n");
        sb.append("\t\t\t" + columnList.get(0).get(0) + "=" + columnToValue(columnList.get(0).get(0), columnList.get(0).get(1)).replace("},", "}") + "\n");
        sb.append("\t</update>");

        return sb.toString();

    }

    private String generateUpdateBatch(List<List<String>> columnList, String id) {

        StringBuffer sb = new StringBuffer("\t<update id=\"update" + ENTITY + "Batch\" parameterType=\"java.util.List\">\n" +
                "\t\tUPDATE " + TABLE_NAME + " SET\n");

        for (int i = 0; i < columnList.size(); i++) {
            if (i == 0) {
                continue;
            }
            if (i == columnList.size() - 1) {
                sb.append("\t\t" + toForeachO2O(columnList.get(i).get(0), columnList.get(i).get(1), id, "", columnList.get(i).get(0), "END").replace("END,", "END") + "");
            } else {
                sb.append("\t\t" + toForeachO2O(columnList.get(i).get(0), columnList.get(i).get(1), id, "", columnList.get(i).get(0), "END") + "");
            }
        }

        sb.append("\t\tWHERE " + id + " IN\n");
        sb.append("\t\t" + toForeachO2O(id, columnList.get(0).get(1), id, ",", "(", ")").replace("close=\"),\"", "close=\")\"").replace("WHEN #{item.id} THEN ", ""));
        sb.append("\t</update>");

        return sb.toString();

    }

    private String generateUpdateBatchSelective(List<List<String>> columnList, String id) {

    	StringBuffer sb = new StringBuffer("\t<update id=\"update" + ENTITY + "BatchSelective\" parameterType=\"java.util.List\">\n" +
    			"\t\tUPDATE " + TABLE_NAME + "\n");
    	sb.append("\t\t<trim prefix=\"SET\" suffixOverrides=\",\">\n");

    	for (int i = 0; i < columnList.size(); i++) {

    		if (i == 0) {
    			continue;
    		}

    		sb.append("\t\t\t<trim prefix=\"" + columnList.get(i).get(0) + " = CASE\"  suffix=\"END,\">\n");
    		sb.append("\t\t\t\t" + toForeachO2OIf(columnList.get(i).get(0), columnList.get(i).get(1), id, "", columnList.get(i).get(0), "END") +"");
    		sb.append("\t\t\t</trim>\n");
    	}

    	sb.append("\t\t</trim>\n");
    	sb.append("\t\tWHERE\n");
    	sb.append("\t\t<foreach collection=\"list\" separator=\"OR\" item=\"item\" index=\"index\" >\n");
    	sb.append("\t\t\t" + id + underScore2Camel("=#{item." + id + "}") + "\n");
    	sb.append("\t\t</foreach>\n");
    	sb.append("\t</update>");

    	return sb.toString();

    }

    private void generateEntity(){
        for (List<String> list : ENTITY_FIELDS) {
            System.out.println("private " + list.get(1) + " " + list.get(0) + ";");
        }
    }

    private void generateMapper(){

    }

    private void generateService(){

    }

    private void generateServiceImpl(){

    }

    private void generateController(){

    }

    private String underScore2Camel(String str) {

        StringBuffer sb = new StringBuffer();
        int count = 0;

        for (int i =0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '_') {
                sb.append(Character.toUpperCase(str.charAt(i + 1)));
                count++;
            } else {
                if (count == 1) {
                    count--;
                    continue;
                }else {
                    sb.append(ch);
                }
            }
        }

        return sb.toString();

    }

    private String columnToValue(String str, String type) {
        return columnToValue(str, "", type);
    }

    private String columnToValue(String str, String prefix, String type) {

        StringBuffer sb = new StringBuffer("#{");

        sb.append(underScore2Camel(prefix + str));
        sb.append(",jdbcType=" + type + "},");

        return sb.toString();

    }

    private String toColumnIf(String str, String type) {
        return toColumnIf(str, "", "", type);
    }

    private String toColumnIf(String str, String prefix, String suffix, String type) {

        StringBuffer sb = new StringBuffer("<if test=\"");

        sb.append(underScore2Camel(str));

        sb.append(" != null\" >");
        if (!prefix.equals("")) {
            sb.append(prefix + columnToValue(str, type));
        } else {
            sb.append(prefix + str + suffix);
        }
        sb.append(",</if>");

        return sb.toString();

    }

    private String toValueIf(String str, String type) {
        return toValueIf(str, "", type);
    }

    private String toValueIf(String str, String prefix, String type) {

        StringBuffer sb = new StringBuffer("<if test=\"");

        sb.append(underScore2Camel(str));

        sb.append(" != null\" >");
        sb.append(columnToValue(prefix + str, type));
        sb.append("</if>");

        return sb.toString();

    }

    private String toForeachO2O(String str, String type, String id, String separator, String open, String close) {

        StringBuffer sb = new StringBuffer();
        sb.append("<foreach collection=\"list\" item=\"item\" index=\"index\"\n");

        if (!open.endsWith("(")) {
            open = open + "=case " + id;
        }

        sb.append("\t\t\t\tseparator=\"" + separator +  "\" open=\"" + open + "\" close=\"" + close + ",\">\n");
        sb.append("\t\t\tWHEN " + "#{item." + id + "} THEN " + columnToValue(str, "item.", type).replace("},", "}") + "\n");
        sb.append("\t\t</foreach>\n");

        return sb.toString();

    }

    private String toForeachO2OIf(String str, String type, String id, String separator, String open, String close) {

    	StringBuffer sb = new StringBuffer();
    	sb.append("<foreach collection=\"list\" item=\"item\" index=\"index\">\n");

    	if (!open.endsWith("(")) {
    		open = open + "=CASE " + id;
    	}

    	sb.append("\t\t\t\t\t<if test=\"" + underScore2Camel("item." + str)  + " != null\">\n");
    	String columnToValue = columnToValue(str, "item.", type);
    	sb.append("\t\t\t\t\t\tWHEN " + id + "=" + underScore2Camel("#{item." + id) + "} THEN " + columnToValue.substring(0, columnToValue.length() - 1) + "\n");
    	sb.append("\t\t\t\t\t</if>\n");
    	sb.append("\t\t\t\t</foreach>\n");

    	return sb.toString();

    }

}
