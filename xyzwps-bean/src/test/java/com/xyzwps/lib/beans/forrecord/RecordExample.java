package com.xyzwps.lib.beans.forrecord;

import com.xyzwps.lib.beans.Holder;

import java.util.List;

public record RecordExample(
        /*  1 */ byte byteValue,
        /*  2 */ short shortValue,
        /*  3 */ int intValue,
        /*  4 */ long longValue,
        /*  5 */ char charValue,
        /*  6 */ float floatValue,
        /*  7 */ double doubleValue,
        /*  8 */ boolean booleanValue,
        /*  9 */ String str,
        /* 10 */ List<String> strList,
        /* 11 */ List<?> objList,
        /* 12 */ int[] intArr,
        /* 13 */ String[] strArr,
        /* 14 */ Holder<String>[] holderArr
                 // TODO: 更复杂的反省
                 // TODO: 多维数组
) {
}
