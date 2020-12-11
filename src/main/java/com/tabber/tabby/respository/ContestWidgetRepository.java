package com.tabber.tabby.respository;

import com.tabber.tabby.entity.ContestWidgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestWidgetRepository extends JpaRepository<ContestWidgetEntity,Long> {

}