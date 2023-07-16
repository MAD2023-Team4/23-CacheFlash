package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private String username;
    List<Flashcard> flashcardList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        username = intent.getStringExtra("Username"); //get username

        queryFlashcardsByCategory(username);

        List<FlashcardPair> flashcardPairList = new ArrayList<>();
        // Assuming flashcardList contains the individual Flashcard objects
        for (int i = 0; i < flashcardList.size(); i += 2) {
            Flashcard firstFlashcard = flashcardList.get(i);
            Flashcard secondFlashcard = null;

            if (i + 1 < flashcardList.size()) {
                secondFlashcard = flashcardList.get(i + 1);
            }

            FlashcardPair flashcardPair = new FlashcardPair(firstFlashcard, secondFlashcard);
            flashcardPairList.add(flashcardPair);
            for (FlashcardPair f : flashcardPairList) {
                Log.v("FlashcardPair", f.getFirstFlashcard().getTitle());
            }
        }

        for (Flashcard f : flashcardList) {
            Log.v("FlashcardPair", f.toString());
        }

        Log.v("FlashcardPair", "FInished");
        // Set up RecyclerView object
        RecyclerView recyclerView = findViewById(R.id.recyclerViewDashboard);
        FlashcardPairAdapter fcPairAdapter = new FlashcardPairAdapter(flashcardPairList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        int spacingInPixels = 4;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDeco(spacingInPixels));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fcPairAdapter);



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
    public void queryFlashcardsByCategory(String username) {
        String capitalizedUsername = username.substring(0, 1).toUpperCase() + username.substring(1);
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = usersRef.child(capitalizedUsername).child("categories");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot flashcardSnapshot : categorySnapshot.getChildren()) {
                        Flashcard flashcard = flashcardSnapshot.getValue(Flashcard.class);
                        Log.v("danger",flashcard.getTitle());
                        flashcardList.add(flashcard);

                        for (Flashcard f : flashcardList){
                            Log.v("lol",f.getTitle());
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });
    }






}