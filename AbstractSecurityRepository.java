package org.investment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSecurityRepository implements ISecurityRepository {
    protected List<Security> securities;
    protected final String filename;
    protected ISerializationStrategy serializationStrategy;

    public AbstractSecurityRepository(String filename, ISerializationStrategy strategy) {
        this.filename = filename;
        this.serializationStrategy = strategy;
        this.securities = new ArrayList<>();
        readFromFile();
    }

    public void readFromFile() {
        this.securities = serializationStrategy.readFromFile(filename);
    }

    public void writeToFile() {
        serializationStrategy.writeToFile(filename, securities);
    }

    @Override
    public Security getById(int id) {
        readFromFile();
        return securities.stream()
                .filter(security -> security.getSecurityId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BriefSecurity> get_k_n_short_list(int k, int n, String sortField) {
        readFromFile();
        return securities.stream()
                .sorted(getComparator(sortField))
                .skip((long) (k - 1) * n)
                .limit(n)
                .map(security -> new BriefSecurity(
                        security.getSecurityId(),
                        security.getName(),
                        security.getType(),
                        security.getCurrentPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public void addSecurity(Security security) {
        readFromFile();
        int newId = generateNewId();
        Security newSecurity = new Security.Builder()
                .securityId(newId)
                .name(security.getName())
                .type(security.getType())
                .currentPrice(security.getCurrentPrice())
                .expectedReturn(security.getExpectedReturn())
                .build();
        securities.add(newSecurity);
        writeToFile();
    }

    @Override
    public void replaceSecurity(int id, Security newSecurity) {
        readFromFile();
        for (int i = 0; i < securities.size(); i++) {
            if (securities.get(i).getSecurityId() == id) {
                securities.set(i, newSecurity);
                writeToFile();
                return;
            }
        }
        throw new IllegalArgumentException("Security with id " + id + " not found");
    }

    @Override
    public void deleteSecurity(int id) {
        readFromFile();
        boolean removed = securities.removeIf(security -> security.getSecurityId() == id);
        if (removed) {
            writeToFile();
        } else {
            throw new IllegalArgumentException("Security with id " + id + " not found");
        }
    }

    @Override
    public int get_count() {
        readFromFile();
        return securities.size();
    }

    @Override
    public void sort_by_field(String field) {
        readFromFile();
        securities.sort(getComparator(field));
        writeToFile();
    }

    protected int generateNewId() {
        return securities.stream()
                .mapToInt(Security::getSecurityId)
                .max()
                .orElse(0) + 1;
    }

    private Comparator<Security> getComparator(String sortField) {
        return switch (sortField) {
            case "name" -> Comparator.comparing(Security::getName);
            case "type" -> Comparator.comparing(Security::getType);
            case "currentPrice" -> Comparator.comparing(Security::getCurrentPrice);
            case "expectedReturn" -> Comparator.comparing(Security::getExpectedReturn);
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortField);
        };
    }
}