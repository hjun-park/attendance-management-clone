package com.hjun.timereport.global.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class GradeConverter implements AttributeConverter<String, String>{

    @Override
    public String convertToDatabaseColumn(String s) {
        if ("USER".equals(s)) {
            return "1";
        } else if ("MANAGER".equals(s)) {
            return "2";
        } else if ("ADMIN".equals(s)) {
        	return "3";
        }
        return "1";
    }

    @Override
    public String convertToEntityAttribute(String grade) {
        if (grade.equals("1")) {
            return "USER";
        } else if (grade.equals("2")) {
            return "MANAGER";
        } else if (grade.equals("3")) {
        	return "ADMIN";
        }
        return "USER";
    }
}
