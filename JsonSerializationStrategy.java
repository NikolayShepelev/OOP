package org.investment;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializationStrategy extends AbstractSerializationStrategy {
    @Override
    protected ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }
}