package sg.edu.np.mad.madasgcacheflash;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StreakUpdateService extends Service {
    private DatabaseReference streakStatus;
    private DatabaseReference totalDaysRef;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Retrieve the username from the intent
        String username = intent.getStringExtra("Username");
        Log.d("StreakUpdateService", username);

        // Get a reference to the streak status and total days in Firebase
        streakStatus = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("Streak Status");
        totalDaysRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("Total Days");

        // Check if the user has a previous streak recorded
        streakStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Previous streak exists
                    int currentStreak = dataSnapshot.child("currentStreak").getValue(Integer.class);
                    Log.d("StreakUpdateService", "Current Streak: " + currentStreak);

                    // Retrieve the start date from Firebase
                    long startDateTimestamp = dataSnapshot.child("startDate").getValue(Long.class);
                    Date startDate = new Date(startDateTimestamp);
                    Calendar calendar = Calendar.getInstance(); // This automatically takes the device's timezone
                    Date currentDate = calendar.getTime();

                    Log.d("StreakUpdateService", "Current Date: " + currentDate);

                    // Check if the current date is consecutive
                    long daysDifference = calculateDaysDifference(startDate, currentDate);
                    Log.d("Consecutive:", String.valueOf(daysDifference));

                    if (daysDifference == 1) {
                        // Increment the current streak
                        currentStreak++;
                    } else if (daysDifference == 0) {
                        Log.v("Day Difference:","No change in streak");
                        Log.v("Current Streak", String.valueOf(currentStreak));

                    } else {
                        // Reset the streak to 1
                        currentStreak = 1;
                    }

                    // Calculate the total number of days using the original startDate value
                    long totalDays = calculateTotalDays(startDate, currentDate);

                    // Update the streak data in the Realtime Database
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("currentStreak", currentStreak);
                    updateData.put("startDate", currentDate.getTime());

                    streakStatus.updateChildren(updateData);

                    // Retrieve the current total days from the Realtime Database
                    totalDaysRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot totalDaysSnapshot) {
                            // Retrieve the value as a Long
                            Long currentTotalDays = totalDaysSnapshot.getValue(Long.class);

                            if (currentTotalDays == null) {
                                // If the value is null, set it to 1 as there was no previous streak
                                totalDaysRef.setValue(1);
                            } else {
                                // If the value is not null, add the totalDays to the currentTotalDays
                                currentTotalDays += totalDays;
                                totalDaysRef.setValue(currentTotalDays);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Error retrieving total days data from the Realtime Database
                            Log.e("Streak", "Error getting total days data: " + databaseError.getMessage());
                        }
                    });

                } else {
                    // No previous streak recorded, create a new streak entry
                    Map<String, Object> streakData = new HashMap<>();
                    streakData.put("startDate", ServerValue.TIMESTAMP); // Store the timestamp using ServerValue.TIMESTAMP
                    streakData.put("currentStreak", 1);

                    streakStatus.updateChildren(streakData);

                    // Calculate the total number of days (it will be 1 as there was no previous streak)
                    totalDaysRef.setValue(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error retrieving streak data from the Realtime Database
                Log.e("Streak", "Error getting streak data: " + databaseError.getMessage());
            }
        });
        // Samuel: changed from start sticky to start not sticky
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private long calculateDaysDifference(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        long differenceMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMillis);

        return differenceDays;
    }

    private long calculateTotalDays(Date startDate, Date endDate) {
        if (startDate == null) {
            return 1;
        }

        // Create Calendar instances for startDate and endDate
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        // Check if the dates are different and add 1 day if they are
        long totalDays = startCalendar.equals(endCalendar) ? 0 : 1;
        Log.d("Date", String.valueOf(startDate));
        Log.d("Date", String.valueOf(endDate));
        Log.d("Total days", String.valueOf(totalDays));

        return totalDays;
    }
}
