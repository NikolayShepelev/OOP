package org.investment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class Security extends BriefSecurity {

    @JsonProperty("expectedReturn")
    private BigDecimal expectedReturn;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Security(Builder builder) {
        super(builder.securityId.orElse(0), builder.name, builder.type, builder.currentPrice);
        this.expectedReturn = builder.expectedReturn;
    }

    public BigDecimal getExpectedReturn() {
        return expectedReturn;
    }

    public static Security createNewSecurity(String name, String type, BigDecimal currentPrice, BigDecimal expectedReturn) {
        return new Builder()
                .name(name)
                .type(type)
                .currentPrice(currentPrice)
                .expectedReturn(expectedReturn)
                .build();
    }

    public static Security updateExistingSecurity(int securityId, String name, String type, BigDecimal currentPrice, BigDecimal expectedReturn) {
        return new Builder()
                .securityId(securityId)
                .name(name)
                .type(type)
                .currentPrice(currentPrice)
                .expectedReturn(expectedReturn)
                .build();
    }

    public static Security createFromString(String securityString) {
        String[] parts = securityString.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Неверный формат строки ценной бумаги. Ожидается 5 значений, разделенных запятыми.");
        }
        try {
            return new Builder()
                    .securityId(Integer.parseInt(parts[0].trim()))
                    .name(parts[1].trim())
                    .type(parts[2].trim())
                    .currentPrice(new BigDecimal(parts[3].trim()))
                    .expectedReturn(new BigDecimal(parts[4].trim()))
                    .build();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат числа в строке ценной бумаги.", e);
        }
    }

    public static Security createFromJson(String json) throws IOException {
        return objectMapper.readValue(json, Security.class);
    }

    public String toJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    public static class Builder {
        private Optional<Integer> securityId = Optional.empty();
        private String name;
        private String type;
        private BigDecimal currentPrice;
        private BigDecimal expectedReturn;

        public Builder securityId(int securityId) {
            this.securityId = Optional.of(securityId);
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder currentPrice(BigDecimal currentPrice) {
            this.currentPrice = currentPrice;
            return this;
        }

        public Builder expectedReturn(BigDecimal expectedReturn) {
            this.expectedReturn = expectedReturn;
            return this;
        }

        public Security build() {
            Security security = new Security(this);
            SecurityValidator.validateSecurity(security);
            return security;
        }
    }

    @Override
    public String toString() {
        return "Security{" +
                "securityId=" + securityId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", currentPrice=" + currentPrice +
                ", expectedReturn=" + expectedReturn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Security)) return false;
        if (!super.equals(o)) return false;
        Security security = (Security) o;
        return Objects.equals(expectedReturn, security.expectedReturn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expectedReturn);
    }

    public boolean isSameBriefSecurity(BriefSecurity other) {
        return super.equals(other);
    }
}