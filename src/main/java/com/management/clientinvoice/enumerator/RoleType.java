package com.biz4solutions.clientinvoice.enumerator;


public enum RoleType {

    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    ACCOUNT("ACCOUNT"),
    USER("USER");


    private final String roleType;

    private RoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleType() {
        return roleType;
    }

    /**
     * Use this method instead of valueOf.
     * @param strEnum Enum String
     * @return the corresponding enum to the String
     */
    public static RoleType getEnum(String strEnum) {
        return RoleType.valueOf(strEnum.toUpperCase().replaceAll(" ", "_"));
    }

}

