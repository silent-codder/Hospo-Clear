package com.silentcodder.newhospital.UserRegister.Model;

public class BookmarkDoctorData {
    String DoctorId;
    String DoctorName;
    String Experience;
    String Qualification;
    String Speciality;
    String UserId;

    public BookmarkDoctorData() {
    }

    public BookmarkDoctorData(String doctorId, String doctorName, String experience, String qualification, String speciality, String userId) {
        DoctorId = doctorId;
        DoctorName = doctorName;
        Experience = experience;
        Qualification = qualification;
        Speciality = speciality;
        UserId = userId;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
