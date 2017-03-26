package com.training.android.selficheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.training.android.selficheck.Datas.UserData;

public class NewAcc extends AppCompatActivity {

    Button btnCreate;
    EditText mId, mName, mEmail;
    private String mRole;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_acc);

        mRole="";
        mId = (EditText) findViewById(R.id.etId);
        mName = (EditText) findViewById(R.id.etName);
        mEmail = (EditText) findViewById(R.id.etEmail);

        btnCreate = (Button) findViewById(R.id.btnCreate);



        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("Users");

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Teacher", "Student"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Position", ""+position);
                if(position==0){
                    mRole = "Teacher";
                }else{
                    mRole = "Student";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData modelUser = new UserData(mEmail.getText().toString(), mName.getText().toString(), mRole, Long.parseLong(mId.getText().toString()));
                mReference.push().setValue(modelUser);
                Intent intent = new Intent(NewAcc.this, StudentMenu.class);
                startActivity(intent);
            }
        });

    }
}
