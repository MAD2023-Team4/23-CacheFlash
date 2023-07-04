package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Profile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private static final String CHANNEL_ID = "my_channel_id";

    private TextView usernameTextView;
    // Firebase Database reference
    private DatabaseReference notificationStatusRef;

    private static final int PERMISSION_REQUEST_CODE = 2001;
    private static final int NOTIFICATION_REQUEST_CODE = 1001;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        username = intent.getStringExtra("Username"); //get username
        TextView textViewPasswordReset;
        TextView textViewPreference;
        TextView textViewNotifications;
        TextView textViewStreak;
        Switch switch_notification = findViewById(R.id.switch1);
        textViewPasswordReset = findViewById(R.id.password_reset);
        textViewPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setTitle("Change Password");
                builder.setMessage("Do you want to change your password?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Profile.this, ChangePassword.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        notificationStatusRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("notificationStatus");

        // Retrieve notification status from Firebase and set the switch accordingly
        notificationStatusRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Boolean notificationEnabled = task.getResult().getValue(Boolean.class);
                switch_notification.setChecked(notificationEnabled != null && notificationEnabled);
            }
        });
        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scheduleNotification();
                    Toast.makeText(Profile.this, "Notification enabled", Toast.LENGTH_SHORT).show();
                } else {
                    cancelNotification();
                    Toast.makeText(Profile.this, "Notification disabled", Toast.LENGTH_SHORT).show();
                }
                notificationStatusRef.setValue(isChecked);
            }
        });

        Log.d("ProfileActivity", "Received username: " + username);
        usernameTextView = findViewById(R.id.textView8);
        usernameTextView.setText(username); //display username

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.about);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item != null) {
                    int id = item.getItemId();

                    if (id == R.id.dashboard) {
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    } else if (id == R.id.home) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    } else if (id == R.id.about) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel for API level 26 and above
            CharSequence channelName = "Flashcard";
            String channelDescription = "Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        try {
            Intent intent = new Intent(this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

            NotificationCompat.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            } else {
                builder = new NotificationCompat.Builder(this);
            }

            builder.setSmallIcon(R.drawable.notification)
                    .setContentTitle("Notification Title")
                    .setContentText("This is the notification text")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFICATION_REQUEST_CODE, builder.build());
            Log.d("ProfileActivity", "Notification sent");
        } catch (SecurityException e) {
            Toast.makeText(this, "Failed to send notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("ProfileActivity", "Failed to send notification: " + e.getMessage());
        }
    }


    private void scheduleNotification() {
        // Create a Calendar instance and set the desired timezone
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Singapore");
        calendar.setTimeZone(timeZone);

        // Set the desired time for the notification (10:45 PM)
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 56);

        // Check if the desired time has already passed
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            // If it has passed, add one day to the calendar to schedule the notification for the next day
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Convert the scheduled time to a human-readable format for logging
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(timeZone);
        String scheduledTime = sdf.format(new Date(calendar.getTimeInMillis()));
        Log.d("Notification", "Scheduled time: " + scheduledTime);

        // Create an intent to trigger the BroadcastReceiver
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        // Get the AlarmManager service and schedule the notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    private void cancelNotification() {
        // Create the same intent used for scheduling and cancel the notification
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        // Get the AlarmManager service and cancel the notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static class NotificationReceiver extends BroadcastReceiver {
        private static final int NOTIFICATION_ID = 123; // Unique ID for the notification
        private static final String CHANNEL_ID = "flashcard_channel"; // Unique ID for the notification channel

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("NotificationReceiver", "Notification received");
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to open the app when the notification is clicked
                Intent appIntent = new Intent(context, Profile.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Create the notification channel (required for API 26 and above)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence channelName = "Flashcard Channel";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                // Create the notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("Flashcard Notification")
                        .setContentText("It's time to study!")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_REMINDER)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                // Show the notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } else {
                // Handle the case where the required permission is not granted
                Log.e("NotificationReceiver", "Vibration permission not granted");
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
    }
}