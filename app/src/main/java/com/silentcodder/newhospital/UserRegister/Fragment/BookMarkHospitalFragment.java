package com.silentcodder.newhospital.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.BookmarkHospitalAdapter;
import com.silentcodder.newhospital.UserRegister.Model.BookmarkHospitalData;
import com.silentcodder.newhospital.UserRegister.Model.HospitalData;

import java.util.ArrayList;
import java.util.List;

public class BookMarkHospitalFragment extends Fragment {

    RecyclerView recyclerView;
    List<BookmarkHospitalData> bookmarkHospitalData;
    BookmarkHospitalAdapter bookmarkHospitalAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_mark_hospital, container, false);
        recyclerView = view.findViewById(R.id.favHospitalRecycleView);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        bookmarkHospitalData = new ArrayList<>();
        bookmarkHospitalAdapter = new BookmarkHospitalAdapter(bookmarkHospitalData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookmarkHospitalAdapter);



        CollectionReference reference = firebaseFirestore.collection("Bookmark-Hospital");
        Query query = reference.whereEqualTo("UserId",UserId);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        BookmarkHospitalData mHospitalData = doc.getDocument().toObject(BookmarkHospitalData.class);
                        bookmarkHospitalData.add(mHospitalData);
                        bookmarkHospitalAdapter.notifyDataSetChanged();
                    }
                }
            }
        });



        return view;
    }
}