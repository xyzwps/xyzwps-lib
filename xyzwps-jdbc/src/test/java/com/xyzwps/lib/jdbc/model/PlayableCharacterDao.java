package com.xyzwps.lib.jdbc.model;

import com.xyzwps.lib.bedrock.BeanParam;
import com.xyzwps.lib.bedrock.Param;
import com.xyzwps.lib.jdbc.*;

import java.util.List;
import java.util.Optional;

@Table("playable_characters")
public interface PlayableCharacterDao {

    List<PlayableCharacter> findByName(String name);

    List<PlayableCharacter> get();

    List<PlayableCharacter> getAll();

    List<PlayableCharacter> find();

    int countAll();

    List<PlayableCharacter> findOrderByAge();

    List<PlayableCharacter> findByRegionOrderByAgeDesc(String region);

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

    int countByRegion(String region);

    @GeneratedKeys
    @Execute("""
            INSERT INTO playable_characters (name, region, age, use_sword, gender, remark, created_at)
            VALUES (:c.name, :c.region, :c.age, :c.useSword, :c.gender, :c.remark, :c.createdAt)
            """)
    long insert(@BeanParam("c") PlayableCharacter character);

    @Batch
    @Execute("""
            INSERT INTO playable_characters (name, region, age, use_sword, gender, remark, created_at)
            VALUES (:name, :region, :age, :useSword, :gender, :remark, :createdAt)
            """)
    void batchInsert(List<PlayableCharacter> characters);

    @Execute("update playable_characters set remark = :remark where uid = :id")
    int updateRemark(@Param("id") long id, @Param("remark") String remark);

    // TODO: 这是面向数据表的操作，想办法改成面向实体类的操作
    void updateSetRemarkByUid(String remark, long id);

    void deleteByUid(long id);
}
