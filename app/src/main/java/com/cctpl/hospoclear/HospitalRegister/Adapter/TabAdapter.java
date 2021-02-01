package com.cctpl.hospoclear.HospitalRegister.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cctpl.hospoclear.HospitalRegister.Fragment.AppointmentFragment;
import com.cctpl.hospoclear.HospitalRegister.Fragment.CompleteAppointmentFragment;
import com.cctpl.hospoclear.HospitalRegister.Fragment.RequestAppointmentFragment;


public class TabAdapter extends FragmentPagerAdapter {
    int totalTabs;

    public TabAdapter(@NonNull FragmentManager fm, int behavior, int tabCount) {
        super(fm, behavior);
        this.totalTabs = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 :
                return new RequestAppointmentFragment();
            case 1 :
                return new AppointmentFragment();
            case 2 :
                return new CompleteAppointmentFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
