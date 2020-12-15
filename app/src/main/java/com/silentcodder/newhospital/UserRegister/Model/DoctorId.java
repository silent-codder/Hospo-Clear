package com.silentcodder.newhospital.UserRegister.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class DoctorId {
    @Exclude
    public String DoctorId;
    public <T extends DoctorId> T withId(@NonNull final String id){
        this.DoctorId = id;
        return (T)this;
    }
}
