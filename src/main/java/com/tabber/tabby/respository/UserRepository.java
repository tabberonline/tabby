package com.tabber.tabby.respository;

import com.tabber.tabby.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    @Query(value = "select * from users where user_id=?1 limit 1",nativeQuery = true)
    UserEntity getTopByUserId(Long userId);

    @Query(value = "select * from users where email=?1 limit 1",nativeQuery = true)
    UserEntity getTopByEmailId(String email);

    @Query(value = "select * from users where sub=?1 limit 1",nativeQuery = true)
    UserEntity getTopBySub(String sub);

    @Query(value = "SELECT * FROM users LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<UserEntity> getUsersFromLimitAndOffset(Integer pageSize, Integer pageLimit);

    @Query(value = "SELECT * FROM users WHERE name=?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<UserEntity> getSimilarNameUsers(String name, Integer pageSize, Integer pageLimit);

    @Query(value = "SELECT * FROM users WHERE email=?1", nativeQuery = true)
    List<UserEntity> getUserFromEmail(String name);

    @Query(value = "SELECT * FROM users WHERE plan_id=?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<UserEntity> getSimilarPlanUsers(Integer plan_id, Integer pageSize, Integer pageLimit);
}
