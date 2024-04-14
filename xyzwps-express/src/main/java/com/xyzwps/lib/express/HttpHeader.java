package com.xyzwps.lib.express;

import java.util.List;

public record HttpHeader(String name, List<String> values) {
}
