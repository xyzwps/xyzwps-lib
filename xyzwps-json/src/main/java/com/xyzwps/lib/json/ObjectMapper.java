package com.xyzwps.lib.json;

import com.xyzwps.lib.json.element.ElementParser;
import com.xyzwps.lib.json.element.SimpleParser;
import com.xyzwps.lib.json.mapper.Mapper;
import com.xyzwps.lib.json.mapper.Mappers;

public class ObjectMapper {

    private final ElementParser parser = new SimpleParser();
    private final Mappers mappers = new Mappers();

    public ObjectMapper add(Mapper<?, ?> mapper) {
        this.mappers.add(mapper);
        return this;
    }

    public <T> T parse(String text, Class<T> tClass) {
        var element = parser.parse(new StringCharGenerator(text));
        // TODO: 处理 record
        throw new RuntimeException("TODO:");
    }

    public static void main(String[] args) {
    }
}
