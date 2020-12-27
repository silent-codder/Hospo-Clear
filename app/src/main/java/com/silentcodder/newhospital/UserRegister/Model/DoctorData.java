package com.silentcodder.newhospital.UserRegister.Model;

public class DoctorData extends DoctorId {
    String DoctorName;
    String Experience;
    String Qualification;
    String Speciality;
    String HospitalId;

    public String getDoctorBio() {
        return DoctorBio;
    }

    public String setDoctorBio(String doctorBio) {
        DoctorBio = doctorBio;
        return doctorBio;
    }

    String DoctorBio;

    public String getProfileImgUrl() {
        return ProfileImgUrl;
    }

    public String setProfileImgUrl(String profileImgUrl) {
        ProfileImgUrl = profileImgUrl;
        return profileImgUrl;
    }

    public DoctorData(String profileImgUrl) {
        ProfileImgUrl = profileImgUrl;
    }

    String ProfileImgUrl;
    public DoctorData() {
    }

    public DoctorData(String doctorName, String experience, String qualification, String speciality, String hospitalId) {
        DoctorName = doctorName;
        Experience = experience;
        Qualification = qualification;
        Speciality = speciality;
        HospitalId = hospitalId;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public String setDoctorName(String doctorName) {
        DoctorName = doctorName;
        return doctorName;
    }

    public String getExperience() {
        return Experience;
    }

    public String setExperience(String experience) {
        Experience = experience;
        return experience;
    }

    public String getQualification() {
        return Qualification;
    }

    public String setQualification(String qualification) {
        Qualification = qualification;
        return qualification;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public String setSpeciality(String speciality) {
        Speciality = speciality;
        return speciality;
    }

    public String getHospitalId() {
        return HospitalId;
    }

    public String setHospitalId(String hospitalId) {
        HospitalId = hospitalId;
        return hospitalId;
    }
}
