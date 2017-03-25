package com.training.android.selficheck;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static com.training.android.selficheck.R.id.btnAttendance;

public class Subject_details extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button mbtnTakeAttendance;
    private TextView mTvSubjName, mTvSubjSched;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        mbtnTakeAttendance = (Button) findViewById(btnAttendance);
        mTvSubjName = (TextView) findViewById(R.id.tvSubjNameDetail);
        mTvSubjSched = (TextView) findViewById(R.id.tvSubjSchedDetail);

        Intent i = getIntent();
        Bundle data = i.getExtras();
        mTvSubjName.setText(data.getString("CourseName"));
        mTvSubjSched.setText(data.getString("CourseSchedule"));

        mbtnTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                        uri = taskSnapshot.getDownloadUrl();

                    }
                });

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }
}
