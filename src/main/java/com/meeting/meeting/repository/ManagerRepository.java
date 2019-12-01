package com.meeting.meeting.repository;


import com.meeting.meeting.model.dbo.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author jie
* @date 2019-11-03
*/
public interface ManagerRepository extends JpaRepository<Manager, Integer>, JpaSpecificationExecutor {
    Manager getManagerByAccount(String userName);

    Manager getManagerByCode(String code);
}
