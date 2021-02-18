package com.cctpl.hospoclear.UserRegister.Model;

public class EveningTimeSlotData extends EveningTimeSlotId{
   String TimeSlot;
   Long TimeStamp;
   String DoctorId;
    public EveningTimeSlotData() {
    }

    public EveningTimeSlotData(String timeSlot, Long timeStamp, String doctorId) {
        TimeSlot = timeSlot;
        TimeStamp = timeStamp;
        DoctorId = doctorId;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }
}
