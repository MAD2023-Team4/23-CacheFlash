package sg.edu.np.mad.madasgcacheflash;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StreakUpdateService extends Service {
    private DatabaseReference streakStatus;
    private DatabaseReference totalDaysRef;
    private Date startDate;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Retrieve the username from the intent
        String username = intent.getStringExtra("Username");
        Log.d("StreakUpdateService",username);

        // Get a reference to the streak status and total days in Firebase
        streakStatus = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("Streak Status");
        totalDaysRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("Total Days");

        // Retrieve the current date
        Date currentDate = new Date();

        // Check if the user has a previous streak recorded
        streakStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Previous streak exists
                     startDate = dataSnapshot.child("startDate").getValue(Date.class);
                    int currentStreak = dataSnapshot.child("currentStreak").getValue(Integer.class);

                    // Check if the current date is consecutive
                    boolean isConsecutive = isConsecutiveDays(startDate, currentDate);

                    if (isConsecutive) {
                        // Increment the current streak
                        currentStreak++;
                    } else {
                        // Reset the streak to 1
                        currentStreak = 1;
                    }

                    // Update the streak data in the Realtime Database
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("startDate", currentDate);
                    updateData.put("currentStreak", currentStreak);

                    streakStatus.updateChildren(updateData);
                } else {
                    // No previous streak recorded, create a new streak entry
                    Map<String, Object> streakData = new HashMap<>();
                    streakData.put("startDate", currentDate);
                    streakData.put("currentStreak", 1);

                    streakStatus.updateChildren(streakData);
                }

                // Calculate the total number of days
                long totalDays = calculateTotalDays(startDate, currentDate);

                // Update the total days in the Realtime Database
                totalDaysRef.setValue(totalDays);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error retrieving streak data from the Realtime Database
                Log.e("Streak", "Error getting streak data: " + databaseError.getMessage());
            }
        });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isConsecutiveDays(Date startDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, 1);

        return calendar.getTime().compareTo(endDate) == 0;
    }

    private long calculateTotalDays(Date startDate, Date endDate) {
        if (startDate == null) {
            return 0;
        }

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        long totalDays = TimeUnit.DAYS.convert(diffTime, TimeUnit.MILLISECONDS) + 1;
        return totalDays;
    }
}

