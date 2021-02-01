package com.cctpl.hospoclear.HospitalRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;

public class BillViewFragment extends Fragment {


    String type,appointmentId;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_view, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("fetching bill...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Bundle bundle = this.getArguments();
        if (bundle != null){
            type = bundle.getString("Type");
            appointmentId = bundle.getString("AppointmentId");
        }

        firebaseFirestore.collection("Appointments").document(appointmentId).collection("Bill").document(appointmentId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    String BillDescription_1 = task.getResult().getString("BillDescription_1");
                    String BillDescription_2 = task.getResult().getString("BillDescription_2");
                    String BillDescription_3 = task.getResult().getString("BillDescription_3");
                    String BillDescription_4 = task.getResult().getString("BillDescription_4");
                    String BillDescription_5 = task.getResult().getString("BillDescription_5");
                    String BillAmount_1 = task.getResult().getString("BillAmount_1");
                    String BillAmount_2 = task.getResult().getString("BillAmount_2");
                    String BillAmount_3 = task.getResult().getString("BillAmount_3");
                    String BillAmount_4 = task.getResult().getString("BillAmount_4");
                    String BillAmount_5 = task.getResult().getString("BillAmount_5");

                    TextView billDescription_1 = view.findViewById(R.id.bill_description_1);
                    TextView billDescription_2 = view.findViewById(R.id.bill_description_2);
                    TextView billDescription_3 = view.findViewById(R.id.bill_description_3);
                    TextView billDescription_4 = view.findViewById(R.id.bill_description_4);
                    TextView billDescription_5 = view.findViewById(R.id.bill_description_5);
                    billDescription_1.setText("1. " + BillDescription_1);
                    billDescription_2.setText("2. " + BillDescription_2);
                    billDescription_3.setText("3. " + BillDescription_3);
                    billDescription_4.setText("4. " + BillDescription_4);
                    billDescription_5.setText("5. " + BillDescription_5);

                    TextView billAmount_1 = view.findViewById(R.id.bill_amount_1);
                    TextView billAmount_2 = view.findViewById(R.id.bill_amount_2);
                    TextView billAmount_3 = view.findViewById(R.id.bill_amount_3);
                    TextView billAmount_4 = view.findViewById(R.id.bill_amount_4);
                    TextView billAmount_5 = view.findViewById(R.id.bill_amount_5);
                    billAmount_1.setText(BillAmount_1);
                    billAmount_2.setText(BillAmount_2);
                    billAmount_3.setText(BillAmount_3);
                    billAmount_4.setText(BillAmount_4);
                    billAmount_5.setText(BillAmount_5);

                    TextView totalBill = view.findViewById(R.id.totalBill);
                    String TotalBill = task.getResult().getString("TotalBill");
                    totalBill.setText("Total Bill : " +TotalBill);

                    progressDialog.dismiss();
                }
            }
        });

        return view;
    }
}