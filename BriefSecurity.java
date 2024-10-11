package org.investment;

import java.math.BigDecimal;

public class BriefSecurity implements ISecurity {
    protected int securityId;
    protected String name;
    protected String type;
    protected BigDecimal currentPrice;

    protected BriefSecurity() {}

    public BriefSecurity(int securityId, String name, String type, BigDecimal currentPrice) {
        setSecurityId(securityId);
        setName(name);
        setType(type);
        setCurrentPrice(currentPrice);
    }

    @Override
    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        SecurityValidator.validateSecurityId(securityId);
        this.securityId = securityId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        SecurityValidator.validateName(name);
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        SecurityValidator.validateType(type);
        this.type = type;
    }

    @Override
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        SecurityValidator.validatePrice(currentPrice);
        this.currentPrice = currentPrice;
    }

    @Override
    public String toString() {
        return "BriefSecurity{" +
                "securityId=" + securityId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", currentPrice=" + currentPrice +
                '}';
    }
}