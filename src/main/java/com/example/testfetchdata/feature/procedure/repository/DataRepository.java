package com.example.testfetchdata.feature.procedure.repository;

import com.example.testfetchdata.feature.procedure.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DataRepository extends JpaRepository<DataEntity, Long>, QuerydslPredicateExecutor<DataEntity> {
}
