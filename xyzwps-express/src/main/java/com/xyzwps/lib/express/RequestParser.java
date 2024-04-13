package com.xyzwps.lib.express;

import java.io.IOException;
import java.io.InputStream;

public interface RequestParser {

    RawRequest parse(InputStream in) throws IOException;
}
