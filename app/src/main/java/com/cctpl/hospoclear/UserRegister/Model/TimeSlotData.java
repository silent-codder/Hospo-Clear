package com.cctpl.hospoclear.UserRegister.Model;

public class TimeSlotData extends TimeSlotId{
   String TimeSlot;

    public TimeSlotData() {
    }

    public TimeSlotData(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }
}
