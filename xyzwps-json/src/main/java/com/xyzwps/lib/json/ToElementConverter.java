package com.xyzwps.lib.json;

import com.xyzwps.lib.json.element.*;


public interface ToElementConverter<V> {

    /**
     * @param v never be null
     */
    JsonElement convert(V v);


}
