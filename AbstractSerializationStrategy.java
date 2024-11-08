package org.investment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSerializationStrategy implements ISerializationStrategy {
    protected ObjectMapper objectMapper;

    public AbstractSerializationStrategy() {
        this.objectMapper = createObjectMapper();
    }

    @Override
    public List<Security> readFromFile(String filename) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Security.class);
                return objectMapper.readValue(file, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void writeToFile(String filename, List<Security> securities) {
        try {
            objectMapper.writeValue(new File(filename), securities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract ObjectMapper createObjectMapper();
}
