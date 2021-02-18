package com.cctpl.hospoclear.UserRegister.Model;

public class TimeSlotData extends TimeSlotId{
   String TimeSlot;
   Long TimeStamp;
   String DoctorId;
   String Flag;

    public TimeSlotData() {
    }

    public TimeSlotData(String timeSlot, Long timeStamp, String doctorId, String flag) {
        TimeSlot = timeSlot;
        TimeStamp = timeStamp;
        DoctorId = doctorId;
        Flag = flag;
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

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }
}
