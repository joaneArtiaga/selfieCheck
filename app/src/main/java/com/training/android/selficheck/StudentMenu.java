package com.training.android.selficheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.training.android.selficheck.Adapters.SubjectsAdapter;
import com.training.android.selficheck.Datas.Subj_StudentsData;
import com.training.android.selficheck.Datas.SubjectsData;

import java.util.ArrayList;

public class StudentMenu extends AppCompatActivity {

    RecyclerView mrvSubjList;
    private SubjectsAdapter mAdapter;
    private ArrayList<SubjectsData> mData = new ArrayList<>();
    private ArrayList<Subj_StudentsData> mSubjStudents = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSubjReference, mSubjStudReference;
    private Subj_StudentsData subj_studentsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        mrvSubjList = (RecyclerView) findViewById(R.id.rvSubjList);

        //Firebase DATABASE
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSubjStudReference = mFirebaseDatabase.getReference().child("Subj_students");
        mSubjReference = mFirebaseDatabase.getReference().child("Subjects");

        //Retrieve the Subject with list of students and password for enrollment
        mSubjStudReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    subj_studentsData = postSnapshot.getValue(Subj_StudentsData.class);
                    mSubjStudents.add(subj_studentsData);
                }

                //Retrieve and show all subjects for the current user student
                mSubjReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            SubjectsData subjectsData = postSnapshot.getValue(SubjectsData.class);

                            for (Subj_StudentsData sub : mSubjStudents) {
                                if (sub.getCourseCode().equals(subjectsData.getCourseCode())) {
                                    mData.add(subjectsData);
                                }
                            }

                        }
                        mAdapter = new SubjectsAdapter(getApplicationContext(), mData);
                        mrvSubjList.setAdapter(mAdapter);
                        mrvSubjList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_subject, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add_subjects:
                Intent i = new Intent(StudentMenu.this, Subject_details.class);
                startActivity(i);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


}