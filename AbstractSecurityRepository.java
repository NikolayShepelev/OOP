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
    }

    protected void readFromFile() {
        this.securities = serializationStrategy.readFromFile(filename);
    }

    protected void writeToFile() {
        serializationStrategy.writeToFile(filename, securities);
    }

    @Override
    public Security getById(int id) {
        return securities.stream()
                .filter(security -> security.getSecurityId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BriefSecurity> get_k_n_short_list(int k, int n, String sortField) {
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
        boolean nameExists = securities.stream()
                .anyMatch(existingSecurity ->
                        existingSecurity.getName().equalsIgnoreCase(security.getName()));

        if (nameExists) {
            throw new DuplicateSecurityNameException(
                    "Security with name '" + security.getName() + "' already exists");
        }

        int newId = generateNewId();
        Security newSecurity = new Security.Builder()
                .securityId(newId)
                .name(security.getName())
                .type(security.getType())
                .currentPrice(security.getCurrentPrice())
                .expectedReturn(security.getExpectedReturn())
                .build();
        securities.add(newSecurity);
    }

    @Override
    public void replaceSecurity(int id, Security newSecurity) {
        boolean nameExists = securities.stream()
                .anyMatch(existingSecurity ->
                        existingSecurity.getSecurityId() != id &&
                                existingSecurity.getName().equalsIgnoreCase(newSecurity.getName()));

        if (nameExists) {
            throw new DuplicateSecurityNameException(
                    "Security with name '" + newSecurity.getName() + "' already exists");
        }

        for (int i = 0; i < securities.size(); i++) {
            if (securities.get(i).getSecurityId() == id) {
                securities.set(i, newSecurity);
                return;
            }
        }
        throw new IllegalArgumentException("Security with id " + id + " not found");
    }

    @Override
    public void deleteSecurity(int id) {
        boolean removed = securities.removeIf(security -> security.getSecurityId() == id);
        if (!removed) {
            throw new IllegalArgumentException("Security with id " + id + " not found");
        }
    }

    @Override
    public int get_count() {
        return securities.size();
    }

    @Override
    public void sort_by_field(String field) {
        securities.sort(getComparator(field));
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