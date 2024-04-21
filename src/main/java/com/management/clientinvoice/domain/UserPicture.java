package com.management.clientinvoice.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;



@Entity
@Getter
@Setter
@Where(clause = "is_active = true")
public class UserPicture extends BaseEntity {

    private static final long serialVersionUID = -5018566307866798434L;

    private String picURL;

    private Boolean isDefaultImage;

    private String bucketName;

    private String keyString;

    @ManyToOne
    private UserIdentity userIdentity;

}