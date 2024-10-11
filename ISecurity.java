package org.investment;

import java.math.BigDecimal;

public interface ISecurity {
    int getSecurityId();
    String getName();
    String getType();
    BigDecimal getCurrentPrice();
}