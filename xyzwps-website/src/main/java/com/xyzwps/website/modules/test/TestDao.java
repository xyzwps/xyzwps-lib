package com.xyzwps.website.modules.test;

import com.xyzwps.lib.jdbc.Query;

public interface TestDao {

    @Query(sql = "select 1")
    int count();
}
