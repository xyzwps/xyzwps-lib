package com.xyzwps.website.modules.test;

import com.xyzwps.lib.ap.Dao;
import com.xyzwps.lib.jdbc.Query;

@Dao
public interface TestDao {

    @Query("select 1")
    int count();
}
