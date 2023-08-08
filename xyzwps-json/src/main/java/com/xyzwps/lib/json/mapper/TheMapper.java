package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class TheMapper {

    private final Map<Class<?>, MapperOne<?>> mappers = new HashMap<>();

    public TheMapper() {
        this.placeholder()

                .addJsonDecimalMapper(new WithJsonDecimal.AndBigDecimal())
                .addJsonDecimalMapper(new WithJsonDecimal.AndDouble())
                .addJsonDecimalMapper(new WithJsonDecimal.AndFloat())

                .addJsonIntegerMapper(new WithJsonInteger.AndBigDecimal())
                .addJsonIntegerMapper(new WithJsonInteger.AndBigInteger())
                .addJsonIntegerMapper(new WithJsonInteger.AndDouble())
                .addJsonIntegerMapper(new WithJsonInteger.AndFloat())
                .addJsonIntegerMapper(new WithJsonInteger.AndBigInteger())
                .addJsonIntegerMapper(new WithJsonInteger.AndLong())
                .addJsonIntegerMapper(new WithJsonInteger.AndShort())

                .addJsonStringMapper(new WithJsonString.AndString())

                .placeholder();
    }

    public <T> JsonObject toJsonObject(T t) {
        return getJsonObjectMapper(t.getClass()).map(it -> ((Mapper<T, JsonObject>)it).toElement(t, this)).get();
    }

    public <T> T parse(JsonElement element, Class<T> tClass) {
        // TODO: 处理 primitive
        Supplier<NoSuchMapperException> errorThrow = () -> new NoSuchMapperException(element.getClass(), tClass);
        return switch (element) {
            case JsonArray json -> this.getJsonArrayMapper(tClass).orElseThrow(errorThrow).toValue(json, this);
            case JsonBoolean json -> this.getJsonBooleanMapper(tClass).orElseThrow(errorThrow).toValue(json, this);
            case JsonDecimal json -> this.getJsonDecimalMapper(tClass).orElseThrow(errorThrow).toValue(json, this);
            case JsonInteger json -> this.getJsonIntegerMapper(tClass).orElseThrow(errorThrow).toValue(json, this);
            case JsonNull json -> this.getJsonNullMapper(tClass).map(it -> it.toValue(json, this)).orElse(null);
            case JsonObject json -> this.getJsonObjectMapper(tClass).orElseThrow(errorThrow).toValue(json, this);
            case JsonString json -> this.getJsonStringMapper(tClass).orElseThrow(errorThrow).toValue(json, this);
        };
    }

    public <T> TheMapper addJsonArrayMapper(Mapper<T, JsonArray> mapper) {
        getOrCreateMap(mapper.getValueType()).withArray(mapper);
        return this;
    }

    public <T> TheMapper addJsonBooleanMapper(Mapper<T, JsonBoolean> mapper) {
        getOrCreateMap(mapper.getValueType()).withBoolean(mapper);
        return this;
    }

    public <T> TheMapper addJsonDecimalMapper(Mapper<T, JsonDecimal> mapper) {
        getOrCreateMap(mapper.getValueType()).withDecimal(mapper);
        return this;
    }

    public <T> TheMapper addJsonIntegerMapper(Mapper<T, JsonInteger> mapper) {
        getOrCreateMap(mapper.getValueType()).withInteger(mapper);
        return this;
    }

    public <T> TheMapper addJsonNullMapper(Mapper<T, JsonNull> mapper) {
        getOrCreateMap(mapper.getValueType()).withNull(mapper);
        return this;
    }

    public <T> TheMapper addJsonObjectMapper(Mapper<T, JsonObject> mapper) {
        getOrCreateMap(mapper.getValueType()).withObject(mapper);
        return this;
    }

    public <T> TheMapper addJsonStringMapper(Mapper<T, JsonString> mapper) {
        getOrCreateMap(mapper.getValueType()).withString(mapper);
        return this;
    }

    private <E> MapperOne<E> getOrCreateMap(Class<E> valueType) {
        var map = mappers.get(valueType);
        if (map == null) {
            var mo = new MapperOne<>(valueType);
            mappers.put(valueType, mo);
            return mo;
        } else {
            //noinspection unchecked
            return (MapperOne<E>) map;
        }
    }

    private <T> Optional<Mapper<T, JsonArray>> getJsonArrayMapper(Class<T> tClass) {
        //noinspection unchecked
        return Optional.ofNullable((MapperOne<T>) mappers.get(tClass))
                .map(MapperOne::getForArray);
    }

    private <T> Optional<Mapper<T, JsonBoolean>> getJsonBooleanMapper(Class<T> tClass) {
        //noinspection unchecked
        return Optional.ofNullable((MapperOne<T>) mappers.get(tClass))
                .map(MapperOne::getForBoolean);
    }

    private <T> Optional<Mapper<T, JsonDecimal>> getJsonDecimalMapper(Class<T> tClass) {
        //noinspection unchecked
        return Optional.ofNullable((MapperOne<T>) mappers.get(tClass))
                .map(MapperOne::getForDecimal);
    }

    private <T> Optional<Mapper<T, JsonInteger>> getJsonIntegerMapper(Class<T> tClass) {
        //noinspection unchecked
        return Optional.ofNullable((MapperOne<T>) mappers.get(tClass))
                .map(MapperOne::getForInteger);
    }

    private <T> Optional<Mapper<T, JsonNull>> getJsonNullMapper(Class<T> tClass) {
        //noinspection unchecked
        return Optional.ofNullable((MapperOne<T>) mappers.get(tClass))
                .map(MapperOne::getForNull);
    }

    private <T> Optional<Mapper<T, JsonObject>> getJsonObjectMapper(Class<T> tClass) {
        //noinspection unchecked
        return Optional.ofNullable((MapperOne<T>) mappers.get(tClass))
                .map(MapperOne::getForObject);
    }

    private <T> Optional<Mapper<T, JsonString>> getJsonStringMapper(Class<T> tClass) {
        //noinspection unchecked
        return Optional.ofNullable((MapperOne<T>) mappers.get(tClass))
                .map(MapperOne::getForString);
    }


    private TheMapper placeholder() {
        return this;
    }

    public static void main(String[] args) {
        var theMapper = new TheMapper()
                .addJsonObjectMapper(new PersonMapper());

        var person = new Person();
        person.setId("id");

        var json = theMapper.toJsonObject(person);
        System.out.println("JSON: " + json);

        var person2 = theMapper.parse(json, Person.class);
        System.out.println("Person:" + person2);
    }

    public static class PersonMapper implements Mapper<Person, JsonObject> {

        @Override
        public Person toValue(JsonObject element, TheMapper m) {
            var person = new Person();
            person.setName(m.parse(element.get("name"), String.class));
            person.setId(m.parse(element.get("id"), String.class));
            return person;
        }

        @Override
        public JsonObject toElement(Person person, TheMapper m) {
            return new JsonObject()
                    .put("name", person.getName())
                    .put("id", person.getId());
        }

        @Override
        public Class<Person> getValueType() {
            return Person.class;
        }

        @Override
        public Class<JsonObject> getElementType() {
            return JsonObject.class;
        }
    }

    public static class Person {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
