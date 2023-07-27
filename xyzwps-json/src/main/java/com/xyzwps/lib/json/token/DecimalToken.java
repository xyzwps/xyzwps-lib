package com.xyzwps.lib.json.token;

import java.math.BigDecimal;

public record DecimalToken(BigDecimal value) implements JsonToken {
}
