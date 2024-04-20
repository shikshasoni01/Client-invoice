package com.biz4solutions.clientinvoice.repository;

import com.biz4solutions.clientinvoice.domain.MasterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MasterDataRepository extends JpaRepository<MasterData,String> {

    @Query(value = "select md.value from master_data md where md.id = :id",nativeQuery = true)
    String findValueByMasterDataId(Long id);
}
