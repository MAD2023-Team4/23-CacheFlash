package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import com.ekn.gruzer.gaugelibrary.FullGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Dashboard extends AppCompatActivity {
    private String username;
    List<Flashcard> allFlashcards = new ArrayList<>();

    private List<Flashcard> filteredFlashcards = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private DashboardAdapter fcAdapter;
    private String selectedDifficulty = "Easy";

    private Flashcard bestPerformingFlashcard;
    private  Flashcard lowestPerformingFlashcard;

    private TextView bestPerformingTextView;
    private  TextView lowestPerformingTextView;

    private FullGauge bestPerformingGaugeChart;
    private FullGauge lowestPerformingGaugeChart;

    private int bestPercentage;
    private int lowestPercentage;
    Flashcard flashcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        username = intent.getStringExtra("Username"); //get username
        Log.v("Dashboard class",username);

        queryFlashCards();

        /*
        Integer percentage = null; // Default value is null
        Integer total = null; // Default value is null
        // Find the Spinner and set the adapter for the difficulty levels
        Spinner difficultySpinner = findViewById(R.id.difficultySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        // Set the OnItemSelectedListener to update the selected difficulty level
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDifficulty = parent.getItemAtPosition(position).toString();
                // Update the RecyclerView to display flashcards based on the selected difficulty
                updateFlashcardList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Find the TextView elements for displaying best performing and lowest performing flashcards
         bestPerformingTextView = findViewById(R.id.bestPerformingTextView);
        lowestPerformingTextView = findViewById(R.id.lowestPerformingTextView);
// Find the gauge charts by their IDs
         bestPerformingGaugeChart = findViewById(R.id.bestperformingGaugeChart);
         lowestPerformingGaugeChart = findViewById(R.id.lowestPerformingGaugeChart);


         */


        recyclerView = findViewById(R.id.recyclerViewDashboard);
        fcAdapter = new DashboardAdapter(allFlashcards,selectedDifficulty);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fcAdapter);

        //Bottom Nav View
        //It is a constraint layout, to allow transitions from one page to another, using if else statements.
        //_________________________________________________________________________________________
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item != null) {
                    int id = item.getItemId();

                    if (id == R.id.dashboard) {
                        return true;
                    }
                    else if (id == R.id.search) {
                        Intent intent = new Intent(getApplicationContext(), Search.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    else if (id == R.id.home) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    else if (id == R.id.leaderboard) {
                        Intent intent = new Intent(getApplicationContext(), Leaderboard.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    else if (id == R.id.about){
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        });


    }
    private void queryFlashCards(){
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(username)
                .child("categories");

        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot flashcardSnapshot : categorySnapshot.getChildren()) {
                        Flashcard flashcard = flashcardSnapshot.getValue(Flashcard.class);
                        if (flashcard != null) {
                            allFlashcards.add(flashcard);
                        }
                    }
                }
                fcAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur while fetching the data (optional).
            }
        });
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void updateFlashcardList() {
        filteredFlashcards.clear(); // Clear the list before populating it again

        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(username)
                .child("categories");

        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AtomicInteger totalFlashcards = new AtomicInteger(0);
                AtomicInteger processedFlashcards = new AtomicInteger(0);

                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    for (DataSnapshot flashcardSnapshot : categorySnapshot.getChildren()) {
                        Flashcard flashcard = flashcardSnapshot.getValue(Flashcard.class);
                        if (flashcard != null) {
                            String flashcardTitle = flashcard.getTitle();
                            DatabaseReference percentageRef = flashcardSnapshot.child("percentage").getRef();
                            DatabaseReference attemptsRef = flashcardSnapshot.child("attempts").getRef();
                            percentageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // Check if the "percentage" node exists for the flashcard
                                    if (snapshot.hasChild(selectedDifficulty.toLowerCase())) {
                                        Double selectedPercentage = snapshot.child(selectedDifficulty.toLowerCase()).getValue(Double.class);

                                        // Get the number of attempts for the selected difficulty
                                        attemptsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.hasChild(selectedDifficulty.toLowerCase())) {
                                                    Integer attempts = snapshot.child(selectedDifficulty.toLowerCase()).getValue(Integer.class);

                                                    // Update the flashcard object with the selected percentage and attempts
                                                    flashcard.getPercentage().put(selectedDifficulty.toLowerCase(), selectedPercentage);
                                                    flashcard.getAttempts().put(selectedDifficulty.toLowerCase(), attempts);

                                                    // Compare the flashcard percentage with the selected percentage
                                                    Double flashcardPercentage = flashcard.getPercentage().get(selectedDifficulty.toLowerCase());

                                                    // Add log messages to check values
                                                    Log.d("Dashboard", "Flashcard: " + flashcardTitle + ", Flashcard Percentage: " + flashcardPercentage + ", Selected Percentage: " + selectedPercentage);

                                                    if (flashcardPercentage != null && flashcardPercentage >= selectedPercentage) {
                                                        filteredFlashcards.add(flashcard);
                                                        Log.d("Dashboard", "Flashcard added to filtered list: " + flashcardTitle);
                                                    }

                                                    processedFlashcards.incrementAndGet();

                                                    // Check if all flashcards have been processed
                                                    if (processedFlashcards.get() == totalFlashcards.get()) {
                                                        // Calculate best and lowest performing flashcards after the list is populated
                                                        bestPerformingFlashcard = findBestPerformingFlashcard();
                                                        lowestPerformingFlashcard = findLowestPerformingFlashcard();

                                                        Log.d("Dashboard", "Filtered Flashcards Size: " + filteredFlashcards.size());
                                                        // Set the text for the TextView elements
                                                        if (bestPerformingFlashcard != null) {
                                                             bestPercentage = bestPerformingFlashcard.getPercentage().get(selectedDifficulty.toLowerCase()).intValue();
                                                            bestPerformingTextView.setText("Wow, you're doing well for " + bestPerformingFlashcard.getTitle() + "! Keep it up!");
                                                        } else {
                                                            bestPerformingTextView.setText("");
                                                        }

                                                        if (lowestPerformingFlashcard != null) {
                                                             lowestPercentage = lowestPerformingFlashcard.getPercentage().get(selectedDifficulty.toLowerCase()).intValue();
                                                            lowestPerformingTextView.setText("Looks like you need to work on " + lowestPerformingFlashcard.getTitle() + ".");
                                                        } else {
                                                            lowestPerformingTextView.setText("");
                                                        }
                                                        setGaugeChartValues(bestPerformingGaugeChart, bestPercentage);
                                                        setGaugeChartValues(lowestPerformingGaugeChart, lowestPercentage);

                                                        // Update the adapter with the filtered flashcards
                                                        fcAdapter.setFlashcards(filteredFlashcards);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle any errors that occur while fetching the data (optional).
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle any errors that occur while fetching the data (optional).
                                }
                            });

                            totalFlashcards.incrementAndGet(); // Increment the total number of flashcards
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur while fetching the data (optional).
            }
        });
    }



    private Flashcard findBestPerformingFlashcard() {
        Flashcard bestPerformingFlashcard = null;
        double highestAveragePercentage = -1; // Initialize to a very low value

        for (Flashcard flashcard : filteredFlashcards) {
            // Get the average percentage for the selected difficulty level
            double averagePercentage = flashcard.getPercentage().get(selectedDifficulty.toLowerCase());
            Log.d("Dashboard", "Flashcard Title: " + flashcard.getTitle() + ", Average Percentage: " + averagePercentage);

            if (averagePercentage > highestAveragePercentage) {
                highestAveragePercentage = averagePercentage;
                bestPerformingFlashcard = flashcard;
            }
        }

        if (bestPerformingFlashcard != null) {
            Log.d("Dashboard", "Best Performing Flashcard: " + bestPerformingFlashcard.getTitle());
        } else {
            Log.d("Dashboard", "No Best Performing Flashcard Found.");
        }

        return bestPerformingFlashcard;
    }

    private Flashcard findLowestPerformingFlashcard() {
        Flashcard lowestPerformingFlashcard = null;
        double lowestAveragePercentage = Double.MAX_VALUE;

        for (Flashcard flashcard : filteredFlashcards) {
            // Get the average percentage for the selected difficulty level
            double averagePercentage = flashcard.getPercentage().get(selectedDifficulty.toLowerCase());
            if (averagePercentage < lowestAveragePercentage) {
                lowestAveragePercentage = averagePercentage;
                lowestPerformingFlashcard = flashcard;
            }
        }

        return lowestPerformingFlashcard;
    }
    private void setGaugeChartValues(FullGauge gaugeChart, int percentage) {
        // Set the percentage value for the gauge chart
        gaugeChart.setValue(percentage);

        // Set the color ranges for the gauge chart
        Range range1 = new Range();
        range1.setColor(Color.parseColor("#ce0000")); // Set the color to red
        range1.setFrom(0.0);
        range1.setTo(50.0);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#E3E500")); // Set the color to yellow
        range2.setFrom(50.0);
        range2.setTo(75.0);

        Range range3 = new Range();
        range3.setColor(Color.parseColor("#00b20b")); // Set the color to green
        range3.setFrom(75.0);
        range3.setTo(100.0);

        // Add the color ranges to the gauge chart
        gaugeChart.addRange(range1);
        gaugeChart.addRange(range2);
        gaugeChart.addRange(range3);

        // Set the min, max, and current value for the gauge chart
        gaugeChart.setMinValue(0.0);
        gaugeChart.setMaxValue(100.0);
        gaugeChart.setValue(percentage);
    }

}

