package com.xyzwps.lib.json.token;

public sealed interface JsonToken
        permits ArrayCloseToken, ArrayOpenToken, BooleanToken, CommaToken, DecimalToken,
        IntegerToken, NullToken, ObjectCloseToken, ObjectOpenToken, SemiToken, StringToken {
}
