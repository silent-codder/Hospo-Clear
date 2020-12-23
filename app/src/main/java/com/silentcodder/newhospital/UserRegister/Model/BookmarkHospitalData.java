package com.silentcodder.newhospital.UserRegister.Model;

public class BookmarkHospitalData {
    String City;
    String ContactNumber;
    String HospitalId;
    String HospitalName;
    String State;
    String UserId;

    public BookmarkHospitalData() {
    }

    public BookmarkHospitalData(String city, String contactNumber, String hospitalId, String hospitalName, String state, String userId) {
        City = city;
        ContactNumber = contactNumber;
        HospitalId = hospitalId;
        HospitalName = hospitalName;
        State = state;
        UserId = userId;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getHospitalId() {
        return HospitalId;
    }

    public void setHospitalId(String hospitalId) {
        HospitalId = hospitalId;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
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
