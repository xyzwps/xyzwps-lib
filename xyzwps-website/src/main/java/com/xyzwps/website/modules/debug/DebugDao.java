package com.xyzwps.website.modules.debug;

import com.xyzwps.lib.jdbc.Query;

public interface DebugDao {

    @Query(sql = "select 1")
    int count();
}
