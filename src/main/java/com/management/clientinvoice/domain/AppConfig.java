package com.management.clientinvoice.domain;


import com.management.clientinvoice.constant.DBConstants;

import com.management.clientinvoice.dto.AppConfigDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Audited
@Entity
@Getter
@Setter
@Table(name = "app_config",
        indexes = {
                @Index(columnList = "key", name = "idx_app_config_key")
        })
@Where(clause = "is_active = true")
public class AppConfig extends BaseEntity {

    private static final long serialVersionUID = -3060116655991373118L;

    @Column(name = "key", nullable = false, length = DBConstants.VARCHAR_DEFAULT_MAX_CHAR_LIMIT, unique = true)
    private String key;
    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    public AppConfigDTO toAppConfigDTO() {
        AppConfigDTO dto = new AppConfigDTO();
        dto.setId(getId().toString());
        dto.setKey(key);
        dto.setValue(value);
        return dto;
    }
}

