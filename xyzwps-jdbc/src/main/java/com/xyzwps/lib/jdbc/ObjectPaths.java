package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.beans.BeanUtils;

import java.util.Map;

class ObjectPaths {

    static Object getValue(Object obj, String path) {
        var pathSegments = path.split("\\.");
        if (pathSegments.length == 0) {
            throw new DbException("The named parameter " + path + " is not found in the object.");
        }

        return getValue(obj, pathSegments, 0);
    }

    private static Object getValue(Object obj, String[] path, int index) {
        if (obj instanceof Map<?, ?> map) {
            return getValueFromMap(map, path, index);
        } else {
            return getValueFromBean(obj, path, index);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object getValueFromBean(Object bean, String[] path, int index) {
        var key = path[index];
        if (index == path.length - 1) {
            var prop = BeanUtils.getBeanInfoFromObject(bean).getPropertyInfo(key)
                    .orElseThrow(() -> new DbException("The named parameter " + String.join(".", path) + " is not found in the map."));
            var anno = prop.getAnnotation(Column.class);
            var mapper = anno == null || ColumnPropertyMapper.None.class.equals(anno.mapper()) ? null : anno.mapper();
            if (mapper == null) {
                return BeanUtils.getProperty(bean, key).getOrThrow();
            } else {
                if (ColumnPropertyMapper.class.isAssignableFrom(mapper)) {
                    return new MappedArg(BeanUtils.getProperty(bean, key).getOrThrow(),
                            (ColumnPropertyMapper<?>) InstanceUtils.createInstanceFromDefaultConstructor(mapper));
                } else {
                    throw new DbException("The class " + mapper.getCanonicalName() + " is not a valid ColumnPropertyMapper.");
                }
            }
        }

        var value = BeanUtils.getProperty(bean, key).getOrThrow();
        return getValue(value, path, index + 1);
    }

    private static Object getValueFromMap(Map<?, ?> map, String[] path, int index) {
        var key = path[index];
        var value = map.get(key);
        if (index == path.length - 1) {
            return value;
        }

        if (value == null) {
            throw new DbException("The named parameter " + key + " is not found in the map.");
        }
        return getValue(value, path, index + 1);
    }
}
