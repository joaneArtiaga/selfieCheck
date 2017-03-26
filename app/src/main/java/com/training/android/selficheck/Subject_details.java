package com.training.android.selficheck;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.training.android.selficheck.Datas.AttendanceData;
import com.training.android.selficheck.Datas.StudentsAttendanceClass;
import com.training.android.selficheck.Datas.Subj_StudentsData;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static com.training.android.selficheck.R.id.btnAttendance;

public class Subject_details extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    DateFormat df = new SimpleDateFormat("HH:mm");
    DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSubjStudReference, mAttendanceReference, mTakeAttendanceReference, SampleReference;
    private Button mbtnTakeAttendance;
    private TextView mTvSubjName, mTvSubjSched, mTvDate, mTvCurrentTime;
    private String uri;
    private String time = df.format(Calendar.getInstance().getTime()), key;
    private String date = df1.format(Calendar.getInstance().getTime()), AttendancePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        //initializations
        mbtnTakeAttendance = (Button) findViewById(btnAttendance);
        mTvSubjName = (TextView) findViewById(R.id.tvSubjNameDetail);
        mTvSubjSched = (TextView) findViewById(R.id.tvSubjSchedDetail);
        mTvDate = (TextView) findViewById(R.id.tvDate);
        mTvCurrentTime = (TextView) findViewById(R.id.tvCurrentTIme);

        //Database
        //Database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSubjStudReference = mFirebaseDatabase.getReference().child("Subj_students");
        SampleReference = mFirebaseDatabase.getReference().child("Sample");

        //Call Attendance function to access Database
        getSubj();

        //Set Datas
        Intent i = getIntent();
        Bundle data = i.getExtras();
        key = data.getString("CourseCode");
        mTvSubjName.setText(data.getString("CourseName"));
        mTvSubjSched.setText(data.getString("CourseSchedule"));
        mTvDate.setText(date);
        mTvCurrentTime.setText(time);

        mbtnTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAttendance();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                String path = "Attendance/" + UUID.randomUUID() + ".png";
                StorageReference attendancReference = storage.getReference(path);

                UploadTask uploadTask = attendancReference.putBytes(byteArray);
                uploadTask.addOnSuccessListener(Subject_details.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Subject_details.this, "Upload Successful! Wait for confirmation", Toast.LENGTH_SHORT).show();

                        uri = String.valueOf(taskSnapshot.getDownloadUrl());

                        pushAttendance();

                    }
                });

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    public void getSubj() {

        mSubjStudReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Subj_StudentsData subj_studentsData = postSnapshot.getValue(Subj_StudentsData.class);

                    if (subj_studentsData.getCourseCode().equals(key)) {
                        mAttendanceReference = mSubjStudReference.child(postSnapshot.getKey()).child("Attendance");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getAttendance() {

        mAttendanceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AttendanceData attendanceData = postSnapshot.getValue(AttendanceData.class);


                    if (attendanceData.getDate().equals(date)) {
                        AttendancePassword = attendanceData.getAttendancePassword();
                        mTakeAttendanceReference = mAttendanceReference.child(date).child("StudentsAttendance");

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pushAttendance() {

        StudentsAttendanceClass sac = new StudentsAttendanceClass("2014006791", time, uri);
        mTakeAttendanceReference.push().setValue(sac);
    }


}
