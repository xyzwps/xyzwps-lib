package com.xyzwps.lib.json.token;

import java.math.BigInteger;

public record IntegerToken(BigInteger value) implements JsonToken {
}
