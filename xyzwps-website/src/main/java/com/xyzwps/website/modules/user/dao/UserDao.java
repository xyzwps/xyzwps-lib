package com.xyzwps.website.modules.user.dao;

import com.xyzwps.lib.bedrock.BeanParam;
import com.xyzwps.lib.jdbc.Execute;
import com.xyzwps.lib.jdbc.GeneratedKeys;
import com.xyzwps.lib.jdbc.Table;
import com.xyzwps.website.modules.user.entity.User;

@Table("users")
public interface UserDao {

    User findByPhone(String phone);

    User findById(long id);

    @GeneratedKeys
    @Execute("""
            insert into users (phone, display_name, avatar, password, email, status, create_time, update_time)
            values (:u.phone, :u.displayName, :u.avatar, :u.password, :u.email, :u.status, :u.createTime, :u.updateTime)
            """)
    long insert(@BeanParam("u") User user);

}
