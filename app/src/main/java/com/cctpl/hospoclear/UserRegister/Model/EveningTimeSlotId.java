package com.cctpl.hospoclear.UserRegister.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class EveningTimeSlotId {
    @Exclude
    public String TimeSlotId;
    public <T extends EveningTimeSlotId> T withId(@NonNull final String id){
        this.TimeSlotId = id;
        return (T)this;
    }
}
