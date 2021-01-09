package com.silentcodder.newhospital.UserRegister.Model;

public class AppointmentData extends AppointmentId{
    String HospitalId;
    String DoctorId;
    String UserId;
    String PatientName;
    String Relationship;
    String Problem;
    String AppointmentDate;
    String Status;
    String ImgUrl;

    public AppointmentData() {
    }

    public AppointmentData(String hospitalId, String doctorId, String userId, String patientName, String relationship,
                           String problem, String appointmentDate ,String status,String imgUrl) {
        HospitalId = hospitalId;
        DoctorId = doctorId;
        UserId = userId;
        Status = status;
        PatientName = patientName;
        Relationship = relationship;
        Problem = problem;
        AppointmentDate = appointmentDate;
        ImgUrl = imgUrl;
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
