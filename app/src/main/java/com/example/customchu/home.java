
package com.example.customchu;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class home extends AppCompatActivity {

    ImageButton homeBtn, toScanQR, toMap, notificationBtn, profileBtn;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView greetings, txtCounter;
    DatabaseReference databaseFacility;
    Button incrementBtn;
    int libRoom1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        homeBtn = findViewById(R.id.homebtn);
        greetings = findViewById(R.id.userGreet);
        toScanQR = findViewById(R.id.toScanQR);
        toMap = findViewById(R.id.toMap);
        notificationBtn = findViewById(R.id.notificationbtn);
        profileBtn = findViewById(R.id.profilebtn);


        String PREF_NAME = "MyPreferences";
        String DARK_MODE_KEY = "darkMode";

        Switch darkMS;
        darkMS = findViewById(R.id.darkModeSwitch);

        // Restore the state of the switch from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean(DARK_MODE_KEY, false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }




        // Restore the state of the switch from SharedPreferences SSS
        darkMS.setChecked(isDarkMode);

        darkMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Save the state of the switch to SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(DARK_MODE_KEY, isChecked);
                editor.apply();

                // Set the night mode based on the switch state
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkMS.setText("Dark Mode");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkMS.setText("Light Mode");
                }

                // Recreate the activity to apply the night mode immediately
                recreate();
            }
        });









        toScanQR.setOnClickListener(view -> {
            Intent intent = new Intent(home.this, QRActivity.class);
            startActivity(intent);
        });

        notificationBtn.setOnClickListener(view -> {
            Intent intent = new Intent(home.this, notifActivity.class);
            startActivity(intent);
        });

        profileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(home.this, profileActivity.class);
            startActivity(intent);
        });

        toMap.setOnClickListener(view -> {
            Intent intent = new Intent(home.this, updatedlibrary.class); //mapActivity or updatedlibrary
            startActivity(intent);
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);

        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(home.this, home.class);
            startActivity(intent);
        });

//        incrementBtn = findViewById(R.id.incrementBtn);
//        txtCounter = findViewById(R.id.txtCounter);

        //getRoom1();
//        databaseFacility = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference room1 = databaseFacility.child("Room1").child("Current");
//
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                txtCounter.setText(dataSnapshot.getValue() + "");
//                libRoom1 = Integer.parseInt(dataSnapshot.getValue() + "");
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//            }
//
//        };
//        room1.addValueEventListener(postListener);
//
//
//        incrementBtn.setOnClickListener(view -> {
//            //getRoom1();
//            room1.setValue(libRoom1 + 1);
//        });

        updateUsername();
    }








    private void updateUsername() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String name = account.getGivenName();
            greetings.setText(name);
            Toast.makeText(this, "Login Success! Welcome " + name, Toast.LENGTH_SHORT).show();
        }
    }

    private void getRoom1(){
        databaseFacility = FirebaseDatabase.getInstance().getReference();
        DatabaseReference room1 = databaseFacility.child("Room1").child("Current");
        incrementBtn.setEnabled(false);

        room1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    txtCounter.setText(task.getResult().getValue() +"");
                    libRoom1 = Integer.parseInt(task.getResult().getValue() + "");
                    incrementBtn.setEnabled(true);
                }
            }
        });

    }

    private void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}
