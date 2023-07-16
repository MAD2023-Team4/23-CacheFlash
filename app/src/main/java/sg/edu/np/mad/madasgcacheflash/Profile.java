package sg.edu.np.mad.madasgcacheflash;

import static android.content.ContentValues.TAG;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jakewharton.threetenabp.AndroidThreeTen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalTime;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;


public class Profile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private static final String CHANNEL_ID = "my_channel_id";
    private boolean isTimeCheckRunning = false;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String WORK_TAG = "notification_work";
    private String selectedTime;
    private DatabaseReference categoryRef;
    private TextView usernameTextView;
    private SharedPreferences sharedPreferences;
    private DatabaseReference desiredTimeRef;
    // Firebase Database reference
    private DatabaseReference notificationStatusRef;
    private static final int NOTIFICATION_ID = 1;

    private static final int PERMISSION_REQUEST_CODE = 2001;

    private String username;
    private static final String NOTIFICATION_STATUS_KEY = "notification_status";
    private MyFirebaseMessagingService firebaseMessagingService;
    String[] categoryOptions = {"Option 1", "Option 2", "Option 3"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_profile);


        TextView textViewPasswordReset;
        TextView textViewPreference;
        TextView textViewStreak;

        Intent intent = getIntent();
        username = intent.getStringExtra("Username"); //get username

        firebaseMessagingService = new MyFirebaseMessagingService();
        AndroidThreeTen.init(this);
        Switch switch_notification = findViewById(R.id.switch1);

        textViewPasswordReset = findViewById(R.id.password_reset);
        textViewPreference = findViewById(R.id.study_preference);
        textViewStreak = findViewById(R.id.study_streak);
        // Add the code snippet here
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(Profile.this, msg, Toast.LENGTH_SHORT).show();
                    }

                });

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


        // Get the notification status from SharedPreferences
        // Initialize Firebase Database references
        notificationStatusRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("notificationStatus");
        desiredTimeRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("desiredTime");
        categoryRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("category");
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isNotificationEnabled = sharedPreferences.getBoolean(NOTIFICATION_STATUS_KEY, false);
        notificationStatusRef.setValue(false);


        // Set the initial state of the switch
        switch_notification.setChecked(isNotificationEnabled);

        // Set switch listener
        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(Profile.this, "Notification enabled", Toast.LENGTH_SHORT).show();

                    // Update the notification status in SharedPreferences
                    sharedPreferences.edit().putBoolean(NOTIFICATION_STATUS_KEY, true).apply();
                    notificationStatusRef.setValue(true);

                } else {
                    Toast.makeText(Profile.this, "Notification disabled", Toast.LENGTH_SHORT).show();

                    // Update the notification status in SharedPreferences
                    sharedPreferences.edit().putBoolean(NOTIFICATION_STATUS_KEY, false).apply();
                    notificationStatusRef.setValue(false);
                    // Cancel the scheduled notification

                }
            }
        });

        textViewPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setTitle("Choose an option")
                        .setItems(new String[]{"Favorite Category", "Study Time"}, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    // Show Spinner for selecting favorite category
                                    showFavoriteCategorySpinner();
                                }
                                else if (which == 1) {
                                    showTimePickerDialog();
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        textViewStreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent streakIntent = new Intent(Profile.this,StudyStreakPage.class);
                Log.d("Profile to next",username);
                streakIntent.putExtra("Username",username);
                startActivity(streakIntent);
            }
        });

        notificationStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean notificationStatus = snapshot.getValue(Boolean.class);
                if (notificationStatus == true)
                {
                    Log.v("Notification","True");
                    createNotificationChannel();
                    startPeriodicTimeCheck();

                }
                else {
                    Log.v("Notification","False");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void showFavoriteCategorySpinner() {
        // Create a Spinner component
        Spinner spinner = new Spinner(Profile.this);

        // Create an ArrayAdapter to populate the Spinner with category options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Profile.this, android.R.layout.simple_spinner_item, categoryOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Create a dialog to display the Spinner
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Select Favorite Category")
                .setView(spinner)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retrieve the selected category from the Spinner
                        String selectedCategory = (String) spinner.getSelectedItem();
                        // Use the selected category as needed
                        Toast.makeText(Profile.this, "Selected Category: " + selectedCategory, Toast.LENGTH_SHORT).show();
                       // categoryRef.setValue(selectedCategory);
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showTimePickerDialog() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        // Inflate the custom layout for the TimePicker
        View dialogView = getLayoutInflater().inflate(R.layout.custom_time_picker_title, null);
        TextView titleTextView = dialogView.findViewById(R.id.titleTextView);
        titleTextView.setText("Choose your study time");

        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the selected time from the TimePicker
                int hourOfDay = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                // Handle the selected time here
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                Log.d("SelectedTime", selectedTime); // Log the selected time

                // Update the selected time in Firebase Realtime Database
                desiredTimeRef.setValue(selectedTime);
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog timePickerDialog = builder.create();
        timePickerDialog.show();
    }




    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showHeadsUpNotification() {
        // Check if the app is in the foreground
        boolean isAppInForeground = isAppInForeground(Profile.this);
        if (!isAppInForeground) {
            // Create a custom heads-up notification layout
            RemoteViews customLayout = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
            customLayout.setTextViewText(R.id.notification_title, "Study Reminder");
            customLayout.setTextViewText(R.id.notification_message, "It is time to study!");

            // Create a pending intent for the notification
            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction("NOTIFICATION_CLICKED");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("Username", username);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            // Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                    .setSmallIcon(R.drawable.cacheflash)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(customLayout)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent);

            // Show the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request the permission from the user
                if (this instanceof Activity) {
                    ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
                }
            } else {
                // Permission granted, show the notification
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        }
    }



    private void startPeriodicTimeCheck() {
        if (isTimeCheckRunning) {
            // TimerTask is already running, no need to start another instance
            return;
        }

        // Create a TimerTask to periodically check the time
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // Set the flag to indicate that the TimerTask is running
                isTimeCheckRunning = true;

                // Get the current time in the desired time zone
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Singapore");
                Calendar currentTime = Calendar.getInstance(timeZone);
                int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                int currentMinute = currentTime.get(Calendar.MINUTE);

                // Check if the app is in the foreground
                boolean isAppInForeground = isAppInForeground(Profile.this);

                // Retrieve the user's selected time from Firebase Realtime Database
                desiredTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String selectedTime = dataSnapshot.getValue(String.class);
                        if (selectedTime != null && !isAppInForeground) {
                            // Parse the selected time to get the hour and minute
                            String[] timeParts = selectedTime.split(":");
                            int selectedHour = Integer.parseInt(timeParts[0]);
                            int selectedMinute = Integer.parseInt(timeParts[1]);
                            Log.d("PeriodicTime", "Current Time: " + currentHour + ":" + currentMinute);
                            Log.d("PeriodicTime", "Selected Time: " + selectedHour + ":" + selectedMinute);

                            // Compare the current time with the user's selected time
                            if (currentHour == selectedHour && currentMinute == selectedMinute) {
                                Log.d("Notification", "Sending notification...");
                                // Call a method to send the push notification
                                showHeadsUpNotification();
                            }
                        }

                        // Set the flag to indicate that the TimerTask has finished running
                        isTimeCheckRunning = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error

                        // Set the flag to indicate that the TimerTask has finished running
                        isTimeCheckRunning = false;
                    }
                });
            }
        };

        // Schedule the TimerTask to run every minute
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 60000); // Run every minute
    }


    private boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null && appProcesses.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName()) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }







    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
    }
}