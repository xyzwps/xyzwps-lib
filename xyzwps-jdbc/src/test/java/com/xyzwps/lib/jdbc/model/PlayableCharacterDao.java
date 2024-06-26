package com.xyzwps.lib.jdbc.model;

import com.xyzwps.lib.bedrock.BeanParam;
import com.xyzwps.lib.bedrock.Param;
import com.xyzwps.lib.jdbc.Execute;
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

    @Query(sql = "SELECT COUNT(*) FROM users")
    int count();

    @Query(sql = "SELECT COUNT(*) FROM users where region = :region and gender = :gender")
    int countByRegionAndGender(@Param("region") String region, @Param("gender") Gender gender);

    @Execute(sql = "insert into ")
    long insert(@BeanParam("c") PlayableCharacter character);
}
