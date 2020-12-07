package com.tabber.tabby.respository;

import com.tabber.tabby.entity.WebsiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteRepository extends JpaRepository<WebsiteEntity,Integer> {
    @Query(value = "select * from websites where id=?1 limit 1",nativeQuery = true)
    WebsiteEntity getTopById(Integer id);
}
