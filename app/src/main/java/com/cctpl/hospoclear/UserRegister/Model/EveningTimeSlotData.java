package com.cctpl.hospoclear.UserRegister.Model;

public class EveningTimeSlotData extends EveningTimeSlotId{
   String TimeSlot;
   Long TimeStamp;
   String DoctorId;
   String Flag;
   int Hr;

    public EveningTimeSlotData() {
    }

    public EveningTimeSlotData(String timeSlot, Long timeStamp, String doctorId, String flag,int hr) {
        TimeSlot = timeSlot;
        TimeStamp = timeStamp;
        DoctorId = doctorId;
        Flag = flag;
        Hr = hr;
    }

    public int getHr() {
        return Hr;
    }

    public void setHr(int hr) {
        Hr = hr;
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
