package com.cashplus.callback.internal;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Converter
@Slf4j

public class JSONObjectConverter implements AttributeConverter<JSONObject, String> {
    private JSONParser jsonParser = new JSONParser();
    @Override
    public String convertToDatabaseColumn(JSONObject jsonObject) {
        return jsonObject != null ? jsonObject.toString() : null;
    }

    @Override
    public JSONObject convertToEntityAttribute(String jsonString) {
        try {
            return jsonString != null ? (JSONObject) jsonParser.parse(jsonString) : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
/*
public class JSONObjectConverter implements AttributeConverter<JSONObject, String> {
    private JSONParser jsonParser = new JSONParser();

    @Override
    public String convertToDatabaseColumn(JSONObject jsonObject) {
        try {
            if (jsonObject == null) {
                return null;
            }
            return jsonObject.toJSONString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    @Override
    public JSONObject convertToEntityAttribute(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        try {
            return (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}*/

