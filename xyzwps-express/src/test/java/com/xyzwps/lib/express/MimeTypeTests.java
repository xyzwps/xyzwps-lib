package com.xyzwps.lib.express;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see <a href="https://github.com/jsdom/whatwg-mimetype/blob/main/test/api.js">jsdom | whatwg-mimetype/test/api.js</a>
 */
class MimeTypeTests {

    @Nested
    class Example {

        static MimeType mimeType;

        @BeforeEach
        void beforeEach() {
            mimeType = MimeType.parse("Text/HTML;Charset=\"utf-8\"");
        }

        @Test
        void serializesCorrectly() {
            assertEquals(mimeType.toString(), "text/html;charset=utf-8");
        }

        @Test
        void has_the_correct_type_subtype_and_essence() {
            assertEquals(mimeType.type, "text");
            assertEquals(mimeType.subtype, "html");
            assertEquals(mimeType.essence, "text/html");
        }

        @Test
        void has_the_correct_parameters() {
            assertEquals(mimeType.parameters.size(), 1);
            assertTrue(mimeType.parameters.has("charset"));
            assertEquals(mimeType.parameters.get("charset").orElseThrow(), "utf-8");
        }

        @Test
        void responds_to_parameter_setting() {
            mimeType.parameters.set("charset", "windows-1252");
            assertEquals(mimeType.parameters.get("charset").orElseThrow(), "windows-1252");
            assertEquals(mimeType.toString(), "text/html;charset=windows-1252");
        }
    }

    @Nested
    class MIMETypeParametersObject {
        MimeType mimeType;

        @BeforeEach
        void beforeEach() {
            mimeType = MimeType.parse("Text/HTML;Charset=\"utf-8\";foo=\"bar\"");
        }

        @Test
        void rejects_setting_only_HTTP_token_code_points_in_the_name() {
            assertThrows(HttpException.class, () -> mimeType.parameters.set("@", "a"));
        }

        @Test
        void rejects_setting_only_HTTP_quoted_string_token_code_points_in_the_value() {
            assertThrows(HttpException.class, () -> mimeType.parameters.set("a", "\u0019"));
        }

        @Test
        void has_the_correct_keys_values_and_entries() {
            assertEquals(2, mimeType.parameters.size());
            assertTrue(mimeType.parameters.has("charset"));
            assertTrue(mimeType.parameters.has("foo"));
            assertEquals("utf-8", mimeType.parameters.get("charset").orElseThrow());
            assertEquals("bar", mimeType.parameters.get("foo").orElseThrow());
        }

        @Test
        void can_be_clear_ed() {
            mimeType.parameters.clear();
            assertEquals(mimeType.parameters.size(), 0);
            assertFalse(mimeType.parameters.has("charset"));
            assertFalse(mimeType.parameters.has("foo"));
        }

        @Test
        void can_have_a_parameter_deleted_including_by_a_non_canonical_casing() {
            mimeType.parameters.delete("chArset");
            assertEquals(mimeType.parameters.size(), 1);
            assertTrue(mimeType.parameters.has("foo"));
            assertEquals("bar", mimeType.parameters.get("foo").orElseThrow());
        }
    }

    @Nested
    class ConstructorBehavior {
        @Test
        void converts_incoming_arguments_into_strings() {
            var arg = "text/HTML";
            var mimeType = MimeType.parse(arg);

            assertNotNull(mimeType);
            assertEquals(mimeType.toString(), "text/html");
        }

        @Test
        void throws_on_unparseable_MIME_types() {
            assertThrows(HttpException.class, () -> MimeType.parse("asdf"));
            assertThrows(HttpException.class, () -> MimeType.parse("text/html™"));
        }
    }
}
