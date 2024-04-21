package com.management.clientinvoice.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppConfigDTO {

    private String id;

    private String key;

    private String value;

    public AppConfigDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public AppConfigDTO() { }

    @Override
    public String toString() {
        return "AppConfigDTO{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

