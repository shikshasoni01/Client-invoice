package com.biz4solutions.clientinvoice.requestWrapper;


import java.util.List;

public class AddUserRequestWrapper extends UpdateUserProfileRequestWrapper{

    private List<String> roleType;

    public List<String> getRoleType() {
        return roleType;
    }


    @Override
    public String toString() {
        return "AddUserRequestWrapper [roleType=" + roleType + ", email=" + email
                + ", firstName=" + firstName + ", lastName=" + lastName + ", addressLine1=" + addressLine1
                + ", addressLine2=" + addressLine2 + ", city=" + city + ", state=" + state + ", country=" + country
                + ", zip=" + zip + ", phoneNumber=" + phoneNumber + ", countryCode=" + countryCode + ", dob=" + dob
                + ", gender=" + gender +  "]";
    }
}

