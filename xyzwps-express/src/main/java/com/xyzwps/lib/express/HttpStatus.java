package com.xyzwps.lib.express;

public final class HttpStatus {

    public final int code;
    public final Category category;

    private HttpStatus(int code) {
        this.code = Category.validCode(code);
        this.category = Category.toCategory(code);
        append(this);
    }

    private static final HttpStatus[] map = new HttpStatus[600];

    private static void append(HttpStatus status) {
        map[status.code] = status;
    }

    public static HttpStatus of(int code) {
        var it = map[code];
        return it == null ? new HttpStatus(code) : it;
    }

    public static final HttpStatus CONTINUE = new HttpStatus(100);
    public static final HttpStatus SWITCHING_PROTOCOLS = new HttpStatus(101);
    public static final HttpStatus PROCESSING = new HttpStatus(102);
    public static final HttpStatus EARLY_HINTS = new HttpStatus(103);

    public static final HttpStatus OK = new HttpStatus(200);
    public static final HttpStatus CREATED = new HttpStatus(201);
    public static final HttpStatus ACCEPTED = new HttpStatus(202);
    public static final HttpStatus NON_AUTHORITATIVE_INFORMATION = new HttpStatus(203);
    public static final HttpStatus NO_CONTENT = new HttpStatus(204);
    public static final HttpStatus RESET_CONTENT = new HttpStatus(205);
    public static final HttpStatus PARTIAL_CONTENT = new HttpStatus(206);
    public static final HttpStatus MULTI_STATUS = new HttpStatus(207);
    public static final HttpStatus ALREADY_REPORTED = new HttpStatus(208);
    public static final HttpStatus IM_USED = new HttpStatus(226);

    public static final HttpStatus MULTIPLE_CHOICES = new HttpStatus(300);
    public static final HttpStatus MOVED_PERMANENTLY = new HttpStatus(301);
    public static final HttpStatus FOUND = new HttpStatus(302);
    public static final HttpStatus SEE_OTHER = new HttpStatus(303);
    public static final HttpStatus NOT_MODIFIED = new HttpStatus(304);
    public static final HttpStatus USE_PROXY_DEPRECATED = new HttpStatus(305);
    public static final HttpStatus UNUSED = new HttpStatus(306);
    public static final HttpStatus TEMPORARY_REDIRECT = new HttpStatus(307);
    public static final HttpStatus PERMANENT_REDIRECT = new HttpStatus(308);

    public static final HttpStatus BAD_REQUEST = new HttpStatus(400);
    public static final HttpStatus UNAUTHORIZED = new HttpStatus(401);
    public static final HttpStatus PAYMENT_REQUIRED_EXPERIMENTAL = new HttpStatus(402);
    public static final HttpStatus FORBIDDEN = new HttpStatus(403);
    public static final HttpStatus NOT_FOUND = new HttpStatus(404);
    public static final HttpStatus METHOD_NOT_ALLOWED = new HttpStatus(405);
    public static final HttpStatus NOT_ACCEPTABLE = new HttpStatus(406);
    public static final HttpStatus PROXY_AUTHENTICATION_REQUIRED = new HttpStatus(407);
    public static final HttpStatus REQUEST_TIMEOUT = new HttpStatus(408);
    public static final HttpStatus CONFLICT = new HttpStatus(409);
    public static final HttpStatus GONE = new HttpStatus(410);
    public static final HttpStatus LENGTH_REQUIRED = new HttpStatus(411);
    public static final HttpStatus PRECONDITION_FAILED = new HttpStatus(412);
    public static final HttpStatus PAYLOAD_TOO_LARGE = new HttpStatus(413);
    public static final HttpStatus URI_TOO_LONG = new HttpStatus(414);
    public static final HttpStatus UNSUPPORTED_MEDIA_TYPE = new HttpStatus(415);
    public static final HttpStatus RANGE_NOT_SATISFIABLE = new HttpStatus(416);
    public static final HttpStatus EXPECTATION_FAILED = new HttpStatus(417);
    public static final HttpStatus IM_A_TEAPOT = new HttpStatus(418);
    public static final HttpStatus MISDIRECTED_REQUEST = new HttpStatus(421);
    public static final HttpStatus UNPROCESSABLE_CONTENT = new HttpStatus(422);
    public static final HttpStatus LOCKED = new HttpStatus(423);
    public static final HttpStatus FAILED_DEPENDENCY = new HttpStatus(424);
    public static final HttpStatus TOO_EARLY_EXPERIMENTAL = new HttpStatus(425);
    public static final HttpStatus UPGRADE_REQUIRED = new HttpStatus(426);
    public static final HttpStatus PRECONDITION_REQUIRED = new HttpStatus(428);
    public static final HttpStatus TOO_MANY_REQUESTS = new HttpStatus(429);
    public static final HttpStatus REQUEST_HEADER_FIELDS_TOO_LARGE = new HttpStatus(431);
    public static final HttpStatus UNAVAILABLE_FOR_LEGAL_REASONS = new HttpStatus(451);

    public static final HttpStatus INTERNAL_SERVER_ERROR = new HttpStatus(500);
    public static final HttpStatus NOT_IMPLEMENTED = new HttpStatus(501);
    public static final HttpStatus BAD_GATEWAY = new HttpStatus(502);
    public static final HttpStatus SERVICE_UNAVAILABLE = new HttpStatus(503);
    public static final HttpStatus GATEWAY_TIMEOUT = new HttpStatus(504);
    public static final HttpStatus HTTP_VERSION_NOT_SUPPORTED = new HttpStatus(505);
    public static final HttpStatus VARIANT_ALSO_NEGOTIATES = new HttpStatus(506);
    public static final HttpStatus INSUFFICIENT_STORAGE = new HttpStatus(507);
    public static final HttpStatus LOOP_DETECTED = new HttpStatus(508);
    public static final HttpStatus NOT_EXTENDED = new HttpStatus(510);
    public static final HttpStatus NETWORK_AUTHENTICATION_REQUIRED = new HttpStatus(511);

    public enum Category {
        /**
         * 1xx
         */
        INFORMATIONAL,
        /**
         * 2xx
         */
        SUCCESSFUL,
        /**
         * 3xx
         */
        REDIRECTION,
        /**
         * 4xx
         */
        CLIENT_ERROR,
        /**
         * 5xx
         */
        SERVER_ERROR;

        private static int validCode(int code) {
            if (code >= 100 && code < 600) {
                return code;
            }
            throw new IllegalArgumentException("Invalid http status code");
        }

        private static Category toCategory(int code) {
            if (code >= 100 && code < 200) return INFORMATIONAL;
            if (code >= 200 && code < 300) return SUCCESSFUL;
            if (code >= 300 && code < 400) return REDIRECTION;
            if (code >= 400 && code < 500) return CLIENT_ERROR;
            if (code >= 500 && code < 600) return SUCCESSFUL;
            throw new IllegalArgumentException("Invalid http status code");
        }
    }

}
