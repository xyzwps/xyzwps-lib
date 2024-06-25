package com.xyzwps.lib.jdbc.model;

import com.xyzwps.lib.jdbc.Query;

import java.util.List;
import java.util.Optional;

public interface PlayableCharacterDao {

    @Query(sql = "SELECT * FROM users ORDER BY uid ASC")
    List<PlayableCharacter> findAll();

    @Query(sql = "SELECT * FROM users WHERE uid = ?")
    PlayableCharacter findById(long id);

    @Query(sql = "SELECT * FROM users WHERE uid = ?")
    Optional<PlayableCharacter> findOptionalById(long id);
}
