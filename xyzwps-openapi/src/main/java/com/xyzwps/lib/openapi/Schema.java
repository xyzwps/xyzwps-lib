package com.xyzwps.lib.openapi;

public sealed interface Schema extends OASElement {

    @Override
    default void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }

    final class ObjectSchema implements Schema {
    }

    final class ArraySchema implements Schema {
    }

    final class EnumSchema implements Schema {
    }

    final class StringSchema implements Schema {
    }

    final class BooleanSchema implements Schema {
    }

    final class IntegerSchema implements Schema {
    }

}
