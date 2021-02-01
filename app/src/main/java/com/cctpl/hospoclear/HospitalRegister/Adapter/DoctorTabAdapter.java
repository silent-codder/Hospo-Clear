package com.cctpl.hospoclear.HospitalRegister.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cctpl.hospoclear.DoctorRegister.Fragment.DoctorActiveAppointmentFragment;
import com.cctpl.hospoclear.DoctorRegister.Fragment.DoctorCompleteAppointmentFragment;

public class DoctorTabAdapter extends FragmentPagerAdapter {
    int totalTabs;

    public DoctorTabAdapter(@NonNull FragmentManager fm, int behavior, int tabCount) {
        super(fm, behavior);
        this.totalTabs = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 :
                return new DoctorActiveAppointmentFragment();
            case 1 :
                return new DoctorCompleteAppointmentFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
