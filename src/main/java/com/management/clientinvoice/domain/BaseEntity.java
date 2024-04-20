package com.biz4solutions.clientinvoice.domain;


import com.biz4solutions.clientinvoice.constant.DBConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -7417690295897760431L;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @JsonIgnore
    @Version
    @Column(name = "version_no")
    private Long versionNo = 0L;

    @Audited
    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Date createdAt;

    @Audited
    @JsonIgnore
    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Audited
    @JsonIgnore
    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;                                                    //user identity id of an user who created it

    @Audited
    @JsonIgnore
    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;													//user identity id of an user who updated it

    @Audited
    @JsonIgnore
    @Column(name = "is_active")
    private Boolean isActive = DBConstants.STATE_ACTIVE;

    @Audited
    @JsonIgnore
    @Column(name = "is_delete")
    private Boolean isDelete = DBConstants.STATE_INACTIVE;

    @Audited
    @JsonIgnore
    @Column(name = "deleted_at", insertable = false)
    private Date deletedAt;

    @Audited
    @JsonIgnore
    @Column(name = "deleted_by")
    private String deletedBy;													//user identity id of an user who deleted it i.e made it isActive false


	@PrePersist
	protected void onCreate() {
		updatedAt = createdAt = new Date();
		versionNo = 0L;
//		createdBy = (null != userAccount) ? String.valueOf(userAccount.getId()) : "";
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Date();
		versionNo++;
//		updatedBy = (null != userAccount) ? String.valueOf(userAccount.getId()) : "";
	}

    public  Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

}

