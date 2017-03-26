package com.training.android.selficheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.training.android.selficheck.Adapters.SubjectsAdapter;
import com.training.android.selficheck.Datas.Subj_StudentsData;
import com.training.android.selficheck.Datas.SubjectsData;
import com.training.android.selficheck.Datas.UserData;

import java.util.ArrayList;
import java.util.Arrays;

public class StudentMenu extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    RecyclerView mrvSubjList;
    private SubjectsAdapter mAdapter;
    private ArrayList<SubjectsData> mData = new ArrayList<>();
    private ArrayList<Subj_StudentsData> mSubjStudents = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSubjReference, mSubjStudReference, mAccReference;
    private static int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Bundle bundle;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        this.bundle = savedInstanceState;
        mrvSubjList = (RecyclerView) findViewById(R.id.rvSubjList);

        email = "";

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user!=null){
                    Toast.makeText(StudentMenu.this, "email: "+user.getEmail(), Toast.LENGTH_SHORT).show();
                    email = user.getEmail();
                }else{
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(), RC_SIGN_IN);
                }
            }
        };
        //Firebase DATABASE
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSubjStudReference = mFirebaseDatabase.getReference().child("Subj_students");
        mSubjReference = mFirebaseDatabase.getReference().child("Subjects");
        mAccReference = mFirebaseDatabase.getInstance().getReference().child("Users");

        mAccReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    UserData modelUser = postSnapshot.getValue(UserData.class);
                        Log.d("current email", email);
                    if(modelUser.getEmail()==email){
                        if(modelUser.getRole().equals("Student")){
                            Log.d("the same email","student");
                        }else if(modelUser.getRole().equals("Teacher")){
                            Log.d("the same email","teacher");
                        }
                    }

                    Log.d("Model User", "Email: "+modelUser.getEmail()+", Name:"+modelUser.getName()+", Role:"+modelUser.getRole());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentMenu.this, "The read failed: "+databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        //Retrieve the Subject with list of students and password for enrollment
        mSubjStudReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Subj_StudentsData subj_studentsData = postSnapshot.getValue(Subj_StudentsData.class);
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
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Signed in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(StudentMenu.this, "Connection Failed", Toast.LENGTH_SHORT).show();
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

            case R.id.signOut:
                AuthUI.getInstance().signOut(this);
                Toast.makeText(StudentMenu.this, "Sign Out", Toast.LENGTH_SHORT).show();
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }


}