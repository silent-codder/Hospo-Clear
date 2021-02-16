package com.cctpl.hospoclear.UserRegister.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class TimeSlotId {
    @Exclude
    public String TimeSlotId;
    public <T extends TimeSlotId> T withId(@NonNull final String id){
        this.TimeSlotId = id;
        return (T)this;
    }
}
