package com.silentcodder.newhospital.HospitalRegister.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;

import java.util.HashMap;
import java.util.Objects;

public class BillFragment extends Fragment {

    Button btnAddBill;
    TextView totalBill;
    ProgressBar progressBar;

    String AppointmentId;

    RelativeLayout field_2,field_3,field_4,field_5;

    FirebaseFirestore firebaseFirestore;

    ImageView addField_1,addField_2,addField_3,addField_4;
    ImageView minusField_1,minusField_2,minusField_3,minusField_4;

    EditText billDescription_1,billDescription_2,billDescription_3,billDescription_4,billDescription_5;
    EditText billAmount_1,billAmount_2,billAmount_3,billAmount_4,billAmount_5;

    String txt_BillDescription_1,txt_BillDescription_2,txt_BillDescription_3,txt_BillDescription_4,txt_BillDescription_5;
    String txt_BillAmount_1,txt_BillAmount_2,txt_BillAmount_3,txt_BillAmount_4,txt_BillAmount_5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            AppointmentId = bundle.getString("AppointmentId");
        }

        //FindViewById Function
        findIds(view);
        AddBill(AppointmentId);

        firebaseFirestore.collection("Appointments").document(AppointmentId).collection("Bill")
                .document(AppointmentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String Bill_Descri_1 = task.getResult().getString("BillDescription_1");
                    String Bill_Descri_2 = task.getResult().getString("BillDescription_2");
                    String Bill_Descri_3 = task.getResult().getString("BillDescription_3");
                    String Bill_Descri_4 = task.getResult().getString("BillDescription_4");
                    String Bill_Descri_5 = task.getResult().getString("BillDescription_5");
                    String BillAmount = task.getResult().getString("TotalBill");

                    String Amount_1 = task.getResult().getString("BillAmount_1");
                    String Amount_2 = task.getResult().getString("BillAmount_2");
                    String Amount_3 = task.getResult().getString("BillAmount_3");
                    String Amount_4 = task.getResult().getString("BillAmount_4");
                    String Amount_5 = task.getResult().getString("BillAmount_5");

                    billDescription_1.setText(Bill_Descri_1);
                    billDescription_2.setText(Bill_Descri_2);
                    billDescription_3.setText(Bill_Descri_3);
                    billDescription_4.setText(Bill_Descri_4);
                    billDescription_5.setText(Bill_Descri_5);
                    billAmount_1.setText(Amount_1);
                    billAmount_2.setText(Amount_2);
                    billAmount_3.setText(Amount_3);
                    billAmount_4.setText(Amount_4);
                    billAmount_5.setText(Amount_5);
                    totalBill.setText("Total Bill : " + BillAmount);
                }
            }
        });
        //Add field
        addField_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_BillDescription_1 = billDescription_1.getText().toString();
                txt_BillAmount_1 = billAmount_1.getText().toString();

                if (TextUtils.isEmpty(txt_BillDescription_1)){
                    billDescription_1.setError("Empty field");
                }else if (TextUtils.isEmpty(txt_BillAmount_1)){
                    billAmount_1.setError("Empty field");
                }else {
                    field_2.setVisibility(View.VISIBLE);
                    addField_1.setVisibility(View.INVISIBLE);
                    minusField_1.setVisibility(View.VISIBLE);

                    AddBill(AppointmentId);

                    //close field 2 btn
                    minusField_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            field_2.setVisibility(View.INVISIBLE);
                            field_3.setVisibility(View.INVISIBLE);
                            field_4.setVisibility(View.INVISIBLE);
                            field_5.setVisibility(View.INVISIBLE);
                            addField_1.setVisibility(View.VISIBLE);
                            minusField_1.setVisibility(View.INVISIBLE);

                            billDescription_2.setText("");
                            billAmount_2.setText("");

                            billDescription_3.setText("");
                            billAmount_3.setText("");

                            billDescription_4.setText("");
                            billAmount_4.setText("");

                            billDescription_5.setText("");
                            billAmount_5.setText("");
                        }
                    });
                }
            }
        });
        addField_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_BillDescription_2 = billDescription_2.getText().toString();
                txt_BillAmount_2 = billAmount_2.getText().toString();

                if (TextUtils.isEmpty(txt_BillDescription_2)){
                    billDescription_2.setError("Empty field");
                }else if (TextUtils.isEmpty(txt_BillAmount_2)){
                    billAmount_2.setError("Empty field");
                }else {
                    field_3.setVisibility(View.VISIBLE);
                    addField_2.setVisibility(View.INVISIBLE);
                    minusField_2.setVisibility(View.VISIBLE);

                    AddBill(AppointmentId);

                    //close field 2 btn
                    minusField_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            field_3.setVisibility(View.INVISIBLE);
                            field_4.setVisibility(View.INVISIBLE);
                            field_5.setVisibility(View.INVISIBLE);
                            addField_2.setVisibility(View.VISIBLE);
                            minusField_2.setVisibility(View.INVISIBLE);
                            billDescription_3.setText("");
                            billAmount_3.setText("");

                            billDescription_4.setText("");
                            billAmount_4.setText("");

                            billDescription_5.setText("");
                            billAmount_5.setText("");
                        }
                    });
                }
            }
        });
        addField_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_BillDescription_3 = billDescription_3.getText().toString();
                txt_BillAmount_3 = billAmount_3.getText().toString();

                if (TextUtils.isEmpty(txt_BillDescription_3)){
                    billDescription_3.setError("Empty field");
                }else if (TextUtils.isEmpty(txt_BillAmount_3)){
                    billAmount_3.setError("Empty field");
                }else {
                    field_4.setVisibility(View.VISIBLE);
                    addField_3.setVisibility(View.INVISIBLE);
                    minusField_3.setVisibility(View.VISIBLE);

                    AddBill(AppointmentId);

                    //close field 3 btn
                    minusField_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            field_4.setVisibility(View.INVISIBLE);
                            field_5.setVisibility(View.INVISIBLE);
                            addField_3.setVisibility(View.VISIBLE);
                            minusField_3.setVisibility(View.INVISIBLE);

                            billDescription_4.setText("");
                            billAmount_4.setText("");

                            billDescription_5.setText("");
                            billAmount_5.setText("");
                        }
                    });
                }
            }
        });
        addField_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_BillDescription_4 = billDescription_4.getText().toString();
                txt_BillAmount_4 = billAmount_4.getText().toString();

                if (TextUtils.isEmpty(txt_BillDescription_4)){
                    billDescription_4.setError("Empty field");
                }else if (TextUtils.isEmpty(txt_BillAmount_4)){
                    billAmount_4.setError("Empty field");
                }else {
                    field_5.setVisibility(View.VISIBLE);
                    addField_4.setVisibility(View.INVISIBLE);
                    minusField_4.setVisibility(View.VISIBLE);

                    AddBill(AppointmentId);

                    //close field 4 btn
                    minusField_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            field_5.setVisibility(View.INVISIBLE);
                            addField_4.setVisibility(View.VISIBLE);
                            minusField_4.setVisibility(View.INVISIBLE);
                            billDescription_5.setText("");
                            billAmount_5.setText("");
                        }
                    });
                }
            }
        });
        return view;
    }

    private void AddBill(String appointmentId) {

        btnAddBill.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnAddBill.setVisibility(View.INVISIBLE);
                txt_BillDescription_1 = billDescription_1.getText().toString();
                txt_BillAmount_1 = billAmount_1.getText().toString();

                txt_BillDescription_2 = billDescription_2.getText().toString();
                txt_BillAmount_2 = billAmount_2.getText().toString();

                txt_BillDescription_3 = billDescription_3.getText().toString();
                txt_BillAmount_3 = billAmount_3.getText().toString();

                txt_BillDescription_4 = billDescription_4.getText().toString();
                txt_BillAmount_4 = billAmount_4.getText().toString();

                txt_BillDescription_5 = billDescription_5.getText().toString();
                txt_BillAmount_5 = billAmount_5.getText().toString();

//                Toast.makeText(getContext(), "1." + txt_BillDescription_1 + " " + txt_BillAmount_1 +
//                        "\n2." + txt_BillDescription_2 + " " + txt_BillAmount_2 +
//                        "\n3." + txt_BillDescription_3 + " " + txt_BillAmount_3 +
//                        "\n4." + txt_BillDescription_4 + " " + txt_BillAmount_4 +
//                        "\n5." + txt_BillDescription_5 + " " + txt_BillAmount_5, Toast.LENGTH_SHORT).show();

                int totalAmount=0,a=0,b=0,c=0,d=0,e=0;

                if (!txt_BillAmount_1.equals("")){
                    a = Integer.parseInt(txt_BillAmount_1);
                }
                if (!txt_BillAmount_2.equals("")){
                    b = Integer.parseInt(txt_BillAmount_2);
                }
                if (!txt_BillAmount_3.equals("")){
                    c = Integer.parseInt(txt_BillAmount_3);
                }
                if (!txt_BillAmount_4.equals("")){
                    d = Integer.parseInt(txt_BillAmount_4);
                }
                if (!txt_BillAmount_5.equals("")){
                    e = Integer.parseInt(txt_BillAmount_5);
                }

                totalAmount = a + b + c + d + e;
                totalBill.setText("Total Amount : ₹ " + totalAmount);

                HashMap<String,Object> map = new HashMap<>();
                map.put("BillDescription_1",txt_BillDescription_1);
                map.put("BillDescription_2",txt_BillDescription_2);
                map.put("BillDescription_3",txt_BillDescription_3);
                map.put("BillDescription_4",txt_BillDescription_4);
                map.put("BillDescription_5",txt_BillDescription_5);
                map.put("BillAmount_1",txt_BillAmount_1);
                map.put("BillAmount_2",txt_BillAmount_2);
                map.put("BillAmount_3",txt_BillAmount_3);
                map.put("BillAmount_4",txt_BillAmount_4);
                map.put("BillAmount_5",txt_BillAmount_5);
                map.put("TotalBill","₹ "+totalAmount);

                firebaseFirestore.collection("Appointments").document(AppointmentId).collection("Bill")
                        .document(AppointmentId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnAddBill.setVisibility(View.VISIBLE);
                        try {
                            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                            Snackbar.make(getView(),"Upload Successfully !!",Snackbar.LENGTH_LONG).show();
                        }catch (Exception ignored){

                        }
                    }
                });
            }
        });
    }

    private void findIds(View view) {

        btnAddBill = view.findViewById(R.id.btnAddBill);
        totalBill = view.findViewById(R.id.totalBill);
        progressBar = view.findViewById(R.id.loader);

        field_2 = view.findViewById(R.id.field_2);
        field_3 = view.findViewById(R.id.field_3);
        field_4 = view.findViewById(R.id.field_4);
        field_5 = view.findViewById(R.id.field_5);

        billDescription_1 = view.findViewById(R.id.bill_description_1);
        billAmount_1 = view.findViewById(R.id.bill_amount_1);

        billDescription_2 = view.findViewById(R.id.bill_description_2);
        billAmount_2 = view.findViewById(R.id.bill_amount_2);

        billDescription_3 = view.findViewById(R.id.bill_description_3);
        billAmount_3 = view.findViewById(R.id.bill_amount_3);

        billDescription_4 = view.findViewById(R.id.bill_description_4);
        billAmount_4 = view.findViewById(R.id.bill_amount_4);

        billDescription_5 = view.findViewById(R.id.bill_description_5);
        billAmount_5 = view.findViewById(R.id.bill_amount_5);

        addField_1 = view.findViewById(R.id.addField_1);
        addField_2 = view.findViewById(R.id.addField_2);
        addField_3 = view.findViewById(R.id.addField_3);
        addField_4 = view.findViewById(R.id.addField_4);

        minusField_1 = view.findViewById(R.id.minusField_1);
        minusField_2 = view.findViewById(R.id.minusField_2);
        minusField_3 = view.findViewById(R.id.minusField_3);
        minusField_4 = view.findViewById(R.id.minusField_4);
    }
}