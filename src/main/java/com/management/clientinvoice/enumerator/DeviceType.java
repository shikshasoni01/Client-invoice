package com.biz4solutions.clientinvoice.enumerator;


public enum DeviceType {

    WEB("WEB");					// for Web;

    private final String deviceType;

    private DeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Use this method instead of valueOf.
     * @param strEnum Enum String
     * @return the corresponding enum to the String
     */
    public static DeviceType getEnum(String strEnum) {
        return DeviceType.valueOf(strEnum.toUpperCase().replaceAll(" ", "_"));
    }
}

