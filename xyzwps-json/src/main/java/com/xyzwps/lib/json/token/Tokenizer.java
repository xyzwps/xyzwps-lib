package com.xyzwps.lib.json.token;

public interface Tokenizer {

    /**
     * @return <tt>null</tt> iff EOF
     */
    JsonToken nextToken();
}
