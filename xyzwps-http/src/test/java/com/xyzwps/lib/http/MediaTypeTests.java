package com.xyzwps.lib.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see <a href="https://github.com/jsdom/whatwg-mimetype/blob/main/test/api.js">jsdom | whatwg-mimetype/test/api.js</a>
 */
class MediaTypeTests {

    @Nested
    class Example {

        static MediaType mediaType;

        @BeforeEach
        void beforeEach() {
            mediaType = MediaType.parse("Text/HTML;Charset=\"utf-8\"");
        }

        @Test
        void serializesCorrectly() {
            assertEquals(mediaType.toString(), "text/html;charset=utf-8");
        }

        @Test
        void has_the_correct_type_subtype_and_essence() {
            assertEquals(mediaType.type, "text");
            assertEquals(mediaType.subtype, "html");
            assertEquals(mediaType.essence, "text/html");
        }

        @Test
        void has_the_correct_parameters() {
            assertEquals(mediaType.parameters.size(), 1);
            assertTrue(mediaType.parameters.has("charset"));
            assertEquals(mediaType.parameters.get("charset").orElseThrow(), "utf-8");
        }

        @Test
        void responds_to_parameter_setting() {
            mediaType.parameters.set("charset", "windows-1252");
            assertEquals(mediaType.parameters.get("charset").orElseThrow(), "windows-1252");
            assertEquals(mediaType.toString(), "text/html;charset=windows-1252");
        }
    }

    @Nested
    class MIMETypeParametersObject {
        MediaType mediaType;

        @BeforeEach
        void beforeEach() {
            mediaType = MediaType.parse("Text/HTML;Charset=\"utf-8\";foo=\"bar\"");
        }

        @Test
        void rejects_setting_only_HTTP_token_code_points_in_the_name() {
            assertThrows(IllegalArgumentException.class, () -> mediaType.parameters.set("@", "a"));
        }

        @Test
        void rejects_setting_only_HTTP_quoted_string_token_code_points_in_the_value() {
            assertThrows(IllegalArgumentException.class, () -> mediaType.parameters.set("a", "\u0019"));
        }

        @Test
        void has_the_correct_keys_values_and_entries() {
            assertEquals(2, mediaType.parameters.size());
            assertTrue(mediaType.parameters.has("charset"));
            assertTrue(mediaType.parameters.has("foo"));
            assertEquals("utf-8", mediaType.parameters.get("charset").orElseThrow());
            assertEquals("bar", mediaType.parameters.get("foo").orElseThrow());
        }

        @Test
        void can_be_clear_ed() {
            mediaType.parameters.clear();
            assertEquals(mediaType.parameters.size(), 0);
            assertFalse(mediaType.parameters.has("charset"));
            assertFalse(mediaType.parameters.has("foo"));
        }

        @Test
        void can_have_a_parameter_deleted_including_by_a_non_canonical_casing() {
            mediaType.parameters.delete("chArset");
            assertEquals(mediaType.parameters.size(), 1);
            assertTrue(mediaType.parameters.has("foo"));
            assertEquals("bar", mediaType.parameters.get("foo").orElseThrow());
        }
    }

    @Nested
    class ConstructorBehavior {
        @Test
        void converts_incoming_arguments_into_strings() {
            var arg = "text/HTML";
            var mimeType = MediaType.parse(arg);

            assertNotNull(mimeType);
            assertEquals(mimeType.toString(), "text/html");
        }

        @Test
        void throws_on_unparseable_MIME_types() {
            assertThrows(IllegalArgumentException.class, () -> MediaType.parse("asdf"));
            assertThrows(IllegalArgumentException.class, () -> MediaType.parse("text/html™"));
        }
    }
}
