package com.xyzwps.lib.openapi;

import com.xyzwps.lib.bedrock.Args;

import java.util.Map;
import java.util.TreeMap;

public sealed interface Schema extends OASElement {


    @Override
    default void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }

    record RefSchema(String $ref) implements Schema {
    }

    final class ObjectSchema implements Schema {

        private boolean nullable;

        private final Map<String, Schema> properties = new TreeMap<>();

        public boolean nullable() {
            return nullable;
        }

        public ObjectSchema nullable(boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        public Map<String, Schema> properties() {
            return properties;
        }

        public ObjectSchema addProperty(String name, Schema schema) {
            properties.put(name, Args.notNull(schema, "schema cannot be null"));
            return this;
        }

        public String type() {
            return "object";
        }
    }

    final class ArraySchema implements Schema {

        private boolean nullable;
        private final Schema items;

        public ArraySchema(Schema items) {
            this.items = Args.notNull(items, "items cannot be null");
        }

        public Schema items() {
            return items;
        }

        public boolean nullable() {
            return nullable;
        }

        public ArraySchema nullable(boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        public String type() {
            return "array";
        }
    }

    final class EnumSchema implements Schema {

        private boolean nullable;

        public boolean nullable() {
            return nullable;
        }

        public String type() {
            return "enum";
        }
    }

    final class StringSchema implements Schema {

        private boolean nullable;

        public boolean nullable() {
            return nullable;
        }

        public String type() {
            return "string";
        }
    }

    final class BooleanSchema implements Schema {
        private boolean nullable;

        public boolean nullable() {
            return nullable;
        }


        public String type() {
            return "boolean";
        }
    }

    final class IntegerSchema implements Schema {
        private boolean nullable;

        public boolean nullable() {
            return nullable;
        }


        public String type() {
            return "number";
        }
    }

}
