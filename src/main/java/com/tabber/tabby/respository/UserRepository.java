package com.tabber.tabby.respository;

import com.tabber.tabby.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    @Query(value = "select * from users where user_id=?1 limit 1")
    UserEntity getTopByUserId(Long userId);

    @Query(value = "select * from users where email=?1 limit 1")
    UserEntity getTopByEmailId(String email);
}
