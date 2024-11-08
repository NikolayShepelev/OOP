package org.investment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlSerializationStrategy extends AbstractSerializationStrategy {
    @Override
    protected ObjectMapper createObjectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }
}