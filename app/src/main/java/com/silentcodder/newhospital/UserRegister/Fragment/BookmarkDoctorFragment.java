package com.silentcodder.newhospital.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.BookmarkDoctorAdapter;
import com.silentcodder.newhospital.UserRegister.Adapter.BookmarkHospitalAdapter;
import com.silentcodder.newhospital.UserRegister.Model.BookmarkDoctorData;
import com.silentcodder.newhospital.UserRegister.Model.BookmarkHospitalData;

import java.util.ArrayList;
import java.util.List;

public class BookmarkDoctorFragment extends Fragment {

    RecyclerView recyclerView;
    List<BookmarkDoctorData> bookmarkDoctorData;
    BookmarkDoctorAdapter bookmarkDoctorAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark_doctor, container, false);
        recyclerView = view.findViewById(R.id.favDoctorRecycleView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        bookmarkDoctorData = new ArrayList<>();
        bookmarkDoctorAdapter = new BookmarkDoctorAdapter(bookmarkDoctorData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookmarkDoctorAdapter);

        CollectionReference reference = firebaseFirestore.collection("Bookmark-Doctor");
        Query query = reference.whereEqualTo("UserId",UserId);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        BookmarkDoctorData mDoctorData = doc.getDocument().toObject(BookmarkDoctorData.class);
                        bookmarkDoctorData.add(mDoctorData);
                        bookmarkDoctorAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }
}