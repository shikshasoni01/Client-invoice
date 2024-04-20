package com.biz4solutions.clientinvoice.repository;


import com.biz4solutions.clientinvoice.domain.AppConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigRepository extends BaseRepository<AppConfig> {

    Page<AppConfig> findByKey(String key, Pageable pageable);

    AppConfig findOneByKey(String key);
}

