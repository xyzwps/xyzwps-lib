package com.xyzwps.lib.json.token;

import com.xyzwps.lib.json.util.CharGenerator;

public interface TokenizerFactory {

    Tokenizer create(CharGenerator generator);

    TokenizerFactory DEFAULT = SimpleTokenizer::new;
}
