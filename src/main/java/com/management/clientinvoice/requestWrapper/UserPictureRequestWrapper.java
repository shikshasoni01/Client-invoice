package com.biz4solutions.clientinvoice.requestWrapper;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPictureRequestWrapper {
    String id;
    String picURL;
    String bucketName;
    String keyString;
}

