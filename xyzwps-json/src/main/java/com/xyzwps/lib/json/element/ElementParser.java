package com.xyzwps.lib.json.element;

import com.xyzwps.lib.json.util.CharGenerator;

public interface ElementParser {

    JsonElement parse(CharGenerator chars);
}
