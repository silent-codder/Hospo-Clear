package com.cctpl.hospoclear.UserRegister.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.AllCategoryAdapter;
import com.cctpl.hospoclear.UserRegister.Adapter.CategoryAdapter;
import com.cctpl.hospoclear.UserRegister.Model.AllCategories;
import com.cctpl.hospoclear.UserRegister.Model.MyCategories;

public class MoreCategoryFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_category, container, false);

        AllCategories[] allCategories = new AllCategories[]{
                new AllCategories("Anesthesia",R.drawable.anesthesia),
                new AllCategories("cardiology",R.drawable.cardiology),
                new AllCategories("Child \nSpecialist",R.drawable.child),
                new AllCategories("Dental \nSurgeon",R.drawable.dental),
                new AllCategories("Dermatologist",R.drawable.dermatology),
                new AllCategories("E.N.T",R.drawable.ent),
                new AllCategories("Eye \nSpecialist",R.drawable.eye),
                new AllCategories("Gastroentero \nlogist",R.drawable.gastroentero),
                new AllCategories("General \nPhysician",R.drawable.stethoscope),
                new AllCategories("Gynecologist",R.drawable.gynecologist),
                new AllCategories("Ayurveda",R.drawable.leaves),
                new AllCategories("IVF",R.drawable.ivf),
                new AllCategories("Neurologist",R.drawable.neurologist),
                new AllCategories("Orthopedics",R.drawable.orthopedics),
                new AllCategories("Psychologist",R.drawable.psychologist),
                new AllCategories("Surgeon",R.drawable.surgeon),
                new AllCategories("Urologist",R.drawable.urologist)
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        AllCategoryAdapter adapter = new AllCategoryAdapter(allCategories);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(adapter);
        return view;
    }
}