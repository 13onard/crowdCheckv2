package com.example.customchu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class adminNotif extends AppCompatActivity {

    private ImageButton adminNotifBack;
    private Spinner spinnerReason;
    private Button btnSetNotification;

    private static final String CHANNEL_ID = "library_closure_channel";
    private static final String CHANNEL_NAME = "Library Closure Channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notif);

        adminNotifBack = findViewById(R.id.adminNotifBack);
        spinnerReason = findViewById(R.id.spinnerReason);
        btnSetNotification = findViewById(R.id.btnSetNotification);

        // Set back button click listener
        adminNotifBack.setOnClickListener(view -> {
            Intent intent = new Intent(adminNotif.this, adminActivity.class);
            startActivity(intent);
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.library_closure_reasons, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReason.setAdapter(adapter);

        btnSetNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the button click here
                String selectedReason = spinnerReason.getSelectedItem().toString();
                if (selectedReason.equals("Library Closure")) {
                    showNotification("Library Closure", "The library is closed.");
                } else {
                    showNotification("Library Closure", "The library is closed due to " + selectedReason);
                }
            }
        });
    }

    // method to show a notification
    private void showNotification(String title, String message) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icons8_notification_90)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
    }
}
