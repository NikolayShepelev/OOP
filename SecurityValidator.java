package org.investment;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class SecurityValidator {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9\\s\\-&.]+$");
    private static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Za-z\\s]+$");

    public static void validateSecurityId(int securityId) {
        if (securityId < 0) {
            throw new IllegalArgumentException("ID ценной бумаги должно быть неотрицательным целым числом.");
        }
    }

    public static void validateName(String name) {
        if (name == null || name.isEmpty() || !NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Название ценной бумаги должно содержать только буквы, цифры, пробелы, дефисы, амперсанды и точки, и быть непустым.");
        }
    }

    public static void validateType(String type) {
        if (type == null || type.isEmpty() || !TYPE_PATTERN.matcher(type).matches()) {
            throw new IllegalArgumentException("Тип ценной бумаги должен содержать только буквы и пробелы, и быть непустым.");
        }
    }

    public static void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цена ценной бумаги должна быть положительным числом.");
        }
    }

    public static void validateExpectedReturn(BigDecimal expectedReturn) {
        if (expectedReturn == null) {
            throw new IllegalArgumentException("Ожидаемая доходность не может быть null.");
        }
    }

    public static void validateSecurity(Security security) {
        if (security.getSecurityId() != 0) {
            validateSecurityId(security.getSecurityId());
        }
        validateName(security.getName());
        validateType(security.getType());
        validatePrice(security.getCurrentPrice());
        validateExpectedReturn(security.getExpectedReturn());
    }
}