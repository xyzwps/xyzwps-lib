package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.Args;

import java.util.regex.Pattern;

/**
 * @see <a href="https://github.com/jsdom/whatwg-mimetype/blob/main/lib/serializer.js">jsdom | whatwg-mimetype/lib/serializer.js</a>
 */
public final class MimeType {

    public final String type;
    public final String subtype;
    public final MimeTypeParameters parameters = new MimeTypeParameters();
    public final String essence;

    private MimeType(String type, String subtype) {
        {
            type = type.toLowerCase();
            if (type.isEmpty()) {
                throw HttpException.badRequest("Invalid subtype: must be a non-empty string");
            }
            if (!solelyContainsHTTPTokenCodePoints(type)) {
                throw HttpException.badRequest("Invalid type %s: must contain only HTTP token code points", type);
            }
            this.type = type;
        }
        {
            subtype = subtype.toLowerCase();
            if (subtype.isEmpty()) {
                throw HttpException.badRequest("Invalid subtype: must be a non-empty string");
            }
            if (!solelyContainsHTTPTokenCodePoints(subtype)) {
                throw HttpException.badRequest("Invalid type %s: must contain only HTTP token code points", subtype);
            }
            this.subtype = subtype;
        }

        this.essence = this.type + '/' + this.subtype;
    }

    public boolean isApplicationJson() {
        return "application/json".equals(essence);
    }


    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(type).append('/').append(subtype);
        if (parameters.size() == 0) {
            return sb.toString();
        }

        parameters.forEach((name, value) -> {
            sb.append(';').append(name).append('=');

            if (!solelyContainsHTTPTokenCodePoints(value) || value.isEmpty()) {
                value = value.replaceAll("([\"\\\\])", "\\$1");
                sb.append('"').append(value).append('"');
            } else {
                sb.append(value);
            }
        });
        return sb.toString();
    }

    public static MimeType parse(String input) {
        Args.notNull(input, "Cannot parse null to " + MimeType.class.getSimpleName());

        input = removeLeadingAndTrailingHTTPWhitespace(input);

        int position = 0;
        var typeBuilder = new StringBuilder();
        while (position < input.length() && input.charAt(position) != '/') {
            typeBuilder.append(input.charAt(position));
            position++;
        }

        var type = typeBuilder.toString();
        if (type.isEmpty() || !solelyContainsHTTPTokenCodePoints(type)) {
            return null;
        }

        if (position >= input.length()) {
            throw HttpException.badRequest("Invalid mime type \"" + type + '"');
        }

        // Skips past "/"
        position++;

        var subTypeBuilder = new StringBuilder();
        while (position < input.length() && input.charAt(position) != ';') {
            subTypeBuilder.append(input.charAt(position));
            position++;
        }

        var subType = removeTrailingHTTPWhitespace(subTypeBuilder.toString());
        if (subType.isEmpty() || !solelyContainsHTTPTokenCodePoints(subType)) {
            throw HttpException.badRequest("Invalid mime subtype \"" + subType + '"');
        }

        var mime = new MimeType(type, subType);

        while (position < input.length()) {
            // Skip past ";"
            position++;

            if (isHTTPWhiteSpaceChar(input.charAt(position))) {
                position++;
            }

            var paramNameBuilder = new StringBuilder();
            while (position < input.length() && input.charAt(position) != ';' && input.charAt(position) != '=') {
                paramNameBuilder.append(input.charAt(position));
                position++;
            }
            var paramName = paramNameBuilder.toString().toLowerCase();

            if (position < input.length()) {
                if (input.charAt(position) == ';') {
                    continue;
                }

                // Skip past "="
                ++position;
            }

            String paramValue;
            if (input.charAt(position) == '\"') {
                var result = collectAnHTTPQuotedString(input, position);
                paramValue = result.value;
                position = result.position;

                while (position < input.length() && input.charAt(position) != ';') {
                    ++position;
                }
            } else {
                var paramValueBuilder = new StringBuilder();
                while (position < input.length() && input.charAt(position) != ';') {
                    paramValueBuilder.append(input.charAt(position));
                    ++position;
                }

                paramValue = removeTrailingHTTPWhitespace(paramValueBuilder.toString());

                if (paramValue.isEmpty()) {
                    continue;
                }
            }

            if (!paramName.isEmpty() &&
                solelyContainsHTTPTokenCodePoints(paramName) &&
                soleyContainsHTTPQuotedStringTokenCodePoints(paramValue) &&
                !mime.parameters.has(paramName)) {
                mime.parameters.set(paramName, paramValue);
            }
        }
        return mime;
    }

    static String removeLeadingAndTrailingHTTPWhitespace(String str) {
        return str.replaceAll("^[ \t\n\r]+", "").replaceAll("[ \t\n\r]+$", "");
    }

    static String removeTrailingHTTPWhitespace(String str) {
        return str.replaceAll("[ \t\n\r]+$", "");
    }

    private static final Pattern HTTP_TOKEN_CODE_POINTS = Pattern.compile("^[-!#$%&'*+.^_`|~A-Za-z0-9]*$");

    static boolean solelyContainsHTTPTokenCodePoints(CharSequence str) {
        return HTTP_TOKEN_CODE_POINTS.matcher(str).matches();
    }

    private static final Pattern HTTP_QUOTED_STRING_TOKEN_CODE_POINTS = Pattern.compile("^[\t\u0020-\u007E\u0080-\u00FF]*$");

    static boolean soleyContainsHTTPQuotedStringTokenCodePoints(String str) {
        return HTTP_QUOTED_STRING_TOKEN_CODE_POINTS.matcher(str).matches();
    }

    static boolean isHTTPWhiteSpaceChar(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    // This variant only implements it with the extract-value flag set.
    private static CollectResult collectAnHTTPQuotedString(String input, int position) {
        var valueBuilder = new StringBuilder();
        position++;

        while (true) {
            while (position < input.length() && input.charAt(position) != '\"' && input.charAt(position) != '\\') {
                valueBuilder.append(input.charAt(position));
                ++position;
            }

            if (position >= input.length()) {
                break;
            }

            var quoteOrBackslash = input.charAt(position);
            ++position;

            if (quoteOrBackslash == '\\') {
                if (position >= input.length()) {
                    valueBuilder.append('\\');
                    break;
                }

                valueBuilder.append(input.charAt(position));
                ++position;
            } else {
                break;
            }
        }

        return new CollectResult(valueBuilder.toString(), position);
    }

    private record CollectResult(String value, int position) {
    }

}
