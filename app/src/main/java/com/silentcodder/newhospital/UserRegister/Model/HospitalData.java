package com.silentcodder.newhospital.UserRegister.Model;

public class HospitalData {
    String HospitalName;
    String City;
    String State;
    String ContactNumber;
    String UserId;
    String Ambulance;

    public HospitalData() {
    }

    public HospitalData(String hospitalName, String city, String state, String contactNumber, String userId, String ambulance) {
        HospitalName = hospitalName;
        City = city;
        State = state;
        ContactNumber = contactNumber;
        UserId = userId;
        Ambulance = ambulance;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getUserId() {
        return UserId;
    }

    public String setUserId(String userId) {
        UserId = userId;
        return userId;
    }

    public String getAmbulance() {
        return Ambulance;
    }

    public void setAmbulance(String ambulance) {
        Ambulance = ambulance;
    }
}
