package com.xyzwps.lib.jdbc.model;

import com.xyzwps.lib.bedrock.BeanParam;
import com.xyzwps.lib.bedrock.Param;
import com.xyzwps.lib.jdbc.Execute;
import com.xyzwps.lib.jdbc.Query;

import java.util.List;
import java.util.Optional;

public interface PlayableCharacterDao {

    @Query(sql = "SELECT * FROM playable_characters ORDER BY uid ASC")
    List<PlayableCharacter> findAll();

    @Query(sql = "SELECT * FROM playable_characters WHERE uid = ?")
    PlayableCharacter findById(long id);

    @Query(sql = "SELECT * FROM playable_characters WHERE uid = ?")
    Optional<PlayableCharacter> findOptionalById(long id);

    @Query(sql = "SELECT COUNT(*) FROM playable_characters")
    int count();

    @Query(sql = "SELECT COUNT(*) FROM playable_characters where region = :region and gender = :gender")
    int countByRegionAndGender(@Param("region") String region, @Param("gender") Gender gender);

    @Execute(sql = """
            INSERT INTO playable_characters (name, region, age, use_sword, gender, remark, created_at)
            VALUES (:c.name, :c.region, :c.age, :c.useSword, :c.gender, :c.remark, :c.createdAt)
            """, returnGeneratedKeys = true)
    long insert(@BeanParam("c") PlayableCharacter character);

    @Execute(sql = """
            INSERT INTO playable_characters (name, region, age, use_sword, gender, remark, created_at)
            VALUES (:name, :region, :age, :useSword, :gender, :remark, :createdAt)
            """, batch = true)
    void batchInsert(List<PlayableCharacter> characters);

    @Execute(sql = "update playable_characters set remark = :remark where uid = :id")
    int updateRemark(@Param("id") long id, @Param("remark") String remark);

}
