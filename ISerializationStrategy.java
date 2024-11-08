package org.investment;

import java.util.List;

public interface ISerializationStrategy {
    List<Security> readFromFile(String filename);
    void writeToFile(String filename, List<Security> securities);
}
