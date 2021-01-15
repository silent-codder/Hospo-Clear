package com.silentcodder.newhospital.HospitalRegister.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.silentcodder.newhospital.R;

public class BillFragment extends Fragment {

    Button btnAddBill;

    RelativeLayout field_2,field_3,field_4,field_5;

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

        //FindViewById Function
        findIds(view);
        AddBill();
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

                    AddBill();

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

                    AddBill();

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

                    AddBill();

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

                    AddBill();

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

    private void AddBill() {
        btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Toast.makeText(getContext(), "1." + txt_BillDescription_1 + " " + txt_BillAmount_1 +
                        "\n2." + txt_BillDescription_2 + " " + txt_BillAmount_2 +
                        "\n3." + txt_BillDescription_3 + " " + txt_BillAmount_3 +
                        "\n4." + txt_BillDescription_4 + " " + txt_BillAmount_4 +
                        "\n5." + txt_BillDescription_5 + " " + txt_BillAmount_5, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findIds(View view) {

        btnAddBill = view.findViewById(R.id.btnAddBill);

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