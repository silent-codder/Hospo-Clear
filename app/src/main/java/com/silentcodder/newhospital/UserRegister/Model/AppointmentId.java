package com.silentcodder.newhospital.UserRegister.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class AppointmentId {
    @Exclude
    public String AppointmentId;
    public <T extends AppointmentId> T withId(@NonNull final String id){
        this.AppointmentId = id;
        return (T)this;
    }
}
