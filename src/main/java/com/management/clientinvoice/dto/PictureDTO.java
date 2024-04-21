package com.management.clientinvoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PictureDTO {

    String id;

    String picURL;

    String userId;

    Boolean isDefaultImage;
}