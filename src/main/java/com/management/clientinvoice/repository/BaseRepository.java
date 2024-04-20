package com.biz4solutions.clientinvoice.repository;



import com.biz4solutions.clientinvoice.domain.BaseEntity;
import com.biz4solutions.clientinvoice.util.CommonUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {

    @Transactional
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isActive = false WHERE e = :entity")
    void softDelete(T entity);

    @Transactional
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isActive = false WHERE e.id = ?1")
    void softDeleteById(UUID id);

    @Transactional
    default void softDelete(Iterable<? extends T> entities) {
    }

    @Transactional
    default void softDeleteByStringId(Iterable<String> strIds) {
        if (!IterableUtils.isEmpty(strIds)) {
            strIds.forEach(id -> {
                UUID uuid = CommonUtil.getUUIDIfValidElseNull(id);
                if (null != uuid) {
                    softDeleteById(uuid);
                }
            });
        }
    }

    @Transactional
    default void softDeleteById(Iterable<UUID> ids) {
        if (!IterableUtils.isEmpty(ids)) {
            ids.forEach(this::softDeleteById);
        }
    }

    @Transactional
    default void softDeleteByEntity(Iterable<T> ids) {
        if (!IterableUtils.isEmpty(ids)) {
            ids.forEach(this::softDelete);
        }
    }

    @Transactional
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isActive = false")
    void softDeleteAll();

    @Transactional
    default void saveAll(Map<?, T> entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            saveAll(entities.values());
        }
    }

}

