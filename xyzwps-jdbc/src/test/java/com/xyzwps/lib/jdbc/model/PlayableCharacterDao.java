package com.xyzwps.lib.jdbc.model;

import com.xyzwps.lib.jdbc.Query;

import java.util.List;

public interface PlayableCharacterDao {

    @Query(sql = "SELECT * FROM users ORDER BY uid ASC")
    List<PlayableCharacter> findAll();
}
