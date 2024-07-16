package com.xyzwps.lib.ap.api;

public sealed interface ApiMethodArgument {

    record SearchParam(String name, String type) implements ApiMethodArgument {
    }

    record HeaderParam(String name, String type) implements ApiMethodArgument {
    }

    record PathParam(String name, String type) implements ApiMethodArgument {
    }

    record Body(String type) implements ApiMethodArgument {
    }

    record Request() implements ApiMethodArgument {
    }

    record Response() implements ApiMethodArgument {
    }
}
