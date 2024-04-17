package com.xyzwps.lib.json.mapper;

import com.xyzwps.lib.json.element.JsonObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TheMapperTests {

    /*

    @Nested
    class FlatExample {

        @Test
        void test() {
            var theMapper = new TheMapper()
                    .addJsonObjectMapper(new PersonMapper());

            var person = new Person();
            person.setId("id");

            var json = theMapper.toJsonObject(person);
            assertTrue("{\"name\":null,\"id\":\"id\"}".equals(json.toString())
                    || "{\"id\":\"id\",\"name\":null}".equals(json.toString()));

            var person2 = theMapper.parse(json, Person.class);
            assertEquals("Person{id='id', name='null'}", person2.toString());
        }

        static class PersonMapper implements Mapper<Person, JsonObject> {

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
*/
    static class Person {
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

