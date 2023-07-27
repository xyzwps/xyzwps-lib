package com.xyzwps.lib.json.token;

import com.xyzwps.lib.json.util.CharGenerator;

public interface TokenizerFactory {

    Tokenizer create(CharGenerator generator);

    TokenizerFactory DEFAULT = new TokenizerFactory() {
        @Override
        public Tokenizer create(CharGenerator generator) {
            return new SimpleTokenizer(generator);
        }
    };
}
