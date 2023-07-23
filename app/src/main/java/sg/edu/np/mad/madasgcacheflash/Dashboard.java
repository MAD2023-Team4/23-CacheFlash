package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private String username;
    List<Flashcard> allFlashcards = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private DashboardAdapter fcAdapter;
    double percentage;
    int total;
    Flashcard flashcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        username = intent.getStringExtra("Username"); //get username

        Integer percentage = null; // Default value is null
        Integer total = null; // Default value is null

        if (intent.hasExtra("Percentage") && intent.hasExtra("Total")) {
            // The intent contains the "Percentage" and "Total" extras
            flashcard = intent.getParcelableExtra("Flashcard");
            percentage = intent.getIntExtra("Percentage", 0); // Retrieve the value of "Percentage" extra, use 0 as default value if not found
            total = intent.getIntExtra("Total", 0); // Retrieve the value of "Total" extra, use 0 as default value if not found
        }

        // Check if percentage and total are null or have values
        if (percentage != null && total != null) {
            // Handle the case where "Percentage" and "Total" extras were found
            // Do something with the values
            flashcard.setPercentage(percentage);
        } else {
            // Handle the case where "Percentage" and "Total" extras were not found
            // Use the default values or perform appropriate actions
        }


        queryFlashCards();
        recyclerView = findViewById(R.id.recyclerViewDashboard);
        fcAdapter = new DashboardAdapter(allFlashcards);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fcAdapter);

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
                    else if (id == R.id.home) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Username", username);
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
}
