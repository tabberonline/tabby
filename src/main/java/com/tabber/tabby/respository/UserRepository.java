package com.tabber.tabby.respository;

import com.tabber.tabby.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    @Query(value = "select * from users where user_id=?1 limit 1",nativeQuery = true)
    UserEntity getTopByUserId(Long userId);

    @Query(value = "select * from users where email=?1 limit 1",nativeQuery = true)
    UserEntity getTopByEmailId(String email);
}
