package com.management.clientinvoice.util;


public class StringUtils {


    private StringUtils() {
    }

    public static String toSentenceCase(final String string) {
        String str = string;
        if (!(str == null || str.isEmpty())) {
            if (str.length() == 1) {
                return str.toUpperCase();
            } else {
                str = str.replaceAll("_", " ");
                return (str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase());
            }
        }
        return str;
    }

    public static final String replaceAllPlus(final String strWithPlus, final String str) {
        return strWithPlus.replaceAll("\\+", str);
    }

    public static final String sqlEscapeAllSpecialChars(final String str) {
        return sqlEscapeAllSingleQuotes(sqlEscapeAllBackSlashes(str));
    }

    public static final String sqlEscapeAllBackSlashes(final String str) {
        return str.replaceAll("\\\\", "\\\\\\\\");
    }

    public static final String sqlEscapeAllSingleQuotes(final String str) {
        return str.replaceAll("'", "\\\\'");
    }

    public static final String toSqlUppercaseWithEscape(final String str) {
        return " UPPER(E'%" + str + "%') ";
    }

    public static final String sqlCastAsVarchar(final String str) {
        return " CAST(" + str + " AS VARCHAR) ";
    }

    public static final String toSqlLowercaseWithEscape(final String str) {
        return " LOWER(E'%" + str + "%') ";
    }

    public static final String wrapFieldToSqlLower(final String fieldName) {
        return " LOWER(" + fieldName + ") ";
    }

    public static final String appendQuotes(Object obj) {
        return "\"" + obj + "\"";
    }

    public static final String getNullIfEmpty(String str) {
        return ((null != str) ? ((str.trim().isEmpty()) ? null : str.trim()) : str);
    }

    /**
     * This method splits the {@code fullName} into First Name & Last Name based on {@code Space} character.
     * This method assumes that the last word is the Last Name.
     *
     * @param fullName
     * @return {@code String[]} where {@code String[0]} is the First Name & {@code String[1]} is the Last Name
     */
    public static final String[] splitFullName(String fullName) {
        String[] fullNameArr = fullName.split(" ");
        String firstName = "", lastName = "";
        if (fullNameArr.length > 1) {
            for (int i = 0; i < fullNameArr.length - 1; i++) {
                firstName += (" " + fullNameArr[i]);
            }
            lastName = fullNameArr[fullNameArr.length - 1];
        } else {
            firstName = fullNameArr[0];
        }
        firstName = firstName.trim();
        lastName = lastName.trim();
        return new String[]{firstName, lastName};
    }
}

