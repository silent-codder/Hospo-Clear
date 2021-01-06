package com.silentcodder.newhospital.UserRegister.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.silentcodder.newhospital.R;
import com.squareup.picasso.Picasso;

public class SeePrescriptionFragment extends Fragment {

    String ImageUrl;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_see_prescription, container, false);
        progressBar = view.findViewById(R.id.loader);
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            ImageUrl = bundle.getString("ImageUrl");
            PhotoView photoView = view.findViewById(R.id.photoView);
            Picasso.get().load(ImageUrl).into(photoView);
        }

        return view;
    }
}