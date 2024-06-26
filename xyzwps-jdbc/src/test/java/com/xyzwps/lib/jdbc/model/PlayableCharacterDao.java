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

    @Execute(sql = """
            INSERT INTO users (name, region, age, use_sword, gender, remark, created_at)
            VALUES (:c.name, :c.region, :c.age, :c.useSword, :c.gender, :c.remark, :c.createdAt)
            """, returnGeneratedKeys = true)
    long insert(@BeanParam("c") PlayableCharacter character);

    @Execute(sql = "update users set remark = :remark where uid = :id")
    int updateRemark(@Param("id") long id, @Param("remark") String remark);

}
