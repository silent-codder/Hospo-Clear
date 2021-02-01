package com.cctpl.hospoclear.UserRegister.Model;

public class UserData {
    String UserName;
    String City;
    String MobileNumber;
    String State;
    String UserId;

    public UserData() {
    }

    public UserData(String userName, String city, String mobileNumber, String state, String userId) {
        UserName = userName;
        City = city;
        MobileNumber = mobileNumber;
        State = state;
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
