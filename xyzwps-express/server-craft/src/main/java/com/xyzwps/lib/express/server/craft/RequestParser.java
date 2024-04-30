package com.xyzwps.lib.express.server.craft;

import java.io.IOException;
import java.io.InputStream;

interface RequestParser {

    RawRequest parse(InputStream in) throws IOException;
}
