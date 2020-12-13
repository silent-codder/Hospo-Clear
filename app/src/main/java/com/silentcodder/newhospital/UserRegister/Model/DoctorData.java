package com.silentcodder.newhospital.UserRegister.Model;

public class DoctorData {
    String DoctorName;
    String Experience;
    String Qualification;
    String Speciality;
    String HospitalId;

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
