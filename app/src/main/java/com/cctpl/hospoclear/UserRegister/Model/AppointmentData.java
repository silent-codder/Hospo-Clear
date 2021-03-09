package com.cctpl.hospoclear.UserRegister.Model;

import java.sql.Timestamp;

public class AppointmentData extends AppointmentId{
    String HospitalId;
    String DoctorId;
    String UserId;
    String PatientName;
    String Relationship;
    String Problem;
    String AppointmentDate;
    String AppointmentTime;
    String Status;
    String ImgUrl;
    String Prescription;
    String SlotId;
    String Section;
    Long TimeStamp;
    String AppointmentTimeStamp;
    String Description;

    public AppointmentData() {
    }

    public AppointmentData(String hospitalId, String doctorId, String userId, String patientName, String relationship,
                           String problem, String appointmentDate ,String status,String imgUrl,String prescription,String appointmentTime,String slotId,
                           String section,Long timeStamp,String appointmentTimeStamp,String description) {
        HospitalId = hospitalId;
        DoctorId = doctorId;
        UserId = userId;
        Status = status;
        PatientName = patientName;
        Relationship = relationship;
        Problem = problem;
        AppointmentDate = appointmentDate;
        AppointmentTime = appointmentTime;
        ImgUrl = imgUrl;
        SlotId = slotId;
        Prescription = prescription;
        Section = section;
        TimeStamp = timeStamp;
        AppointmentTimeStamp = appointmentTimeStamp;
        Description = description;
    }

    public String getAppointmentTimeStamp() {
        return AppointmentTimeStamp;
    }

    public void setAppointmentTimeStamp(String appointmentTimeStamp) {
        AppointmentTimeStamp = appointmentTimeStamp;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getSlotId() {
        return SlotId;
    }

    public void setSlotId(String slotId) {
        SlotId = slotId;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        AppointmentTime = appointmentTime;
    }

    public String getPrescription() {
        return Prescription;
    }

    public void setPrescription(String prescription) {
        Prescription = prescription;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getHospitalId() {
        return HospitalId;
    }

    public void setHospitalId(String hospitalId) {
        HospitalId = hospitalId;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getRelationship() {
        return Relationship;
    }

    public void setRelationship(String relationship) {
        Relationship = relationship;
    }

    public String getProblem() {
        return Problem;
    }

    public void setProblem(String problem) {
        Problem = problem;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }
}
