package com.cashplus.callback.internal;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
@Converter
@Slf4j
public class URIConverter implements AttributeConverter<URI, String> {

    @Override
    public String convertToDatabaseColumn(URI uri) {
        // Convert URI to String representation for storing in the database
        return uri.toString();
    }

    @Override
    public URI convertToEntityAttribute(String uriString) {
        try {
            return URI.create(uriString);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}