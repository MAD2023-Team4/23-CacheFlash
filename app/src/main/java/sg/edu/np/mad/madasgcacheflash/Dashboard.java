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
    int score;
    int total;
    Flashcard flashcard;
    List<Integer> scoreList = new ArrayList<>();
    List<Float> percentageList = new ArrayList<>();
    List<Flashcard> flashcardList = new ArrayList<>();

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        username = intent.getStringExtra("Username"); //get username

        if (username != null) {
            queryFlashcardsData(username);
        } else {
            // Handle the case when the username is null
            Log.e("QuoteApp", "Username is null");
        }

        /*
        if (intent.hasExtra("Score") && intent.hasExtra("Total")) {
            // The intent contains the "score" key
            flashcard = intent.getParcelableExtra("flashcard");
            score = intent.getIntExtra("Score", 0); // Retrieve the value of "score" key, use 0 as default value if not found
            total = intent.getIntExtra("Total",0);
        }
        */

        /*
        RecyclerView recyclerView;
        FlashcardAdapter fcAdapter = new FlashcardAdapter(flashcardList);
        LinearLayoutManager mLayoutManager;
        int spacingInPixels;


        for (Flashcard flashcard : flashcardList) {
            // Access the current flashcard
            // You can perform operations or access its properties here
            String title = flashcard.getTitle();

            recyclerView = findViewById(R.id.recyclerView3);
            fcAdapter = new FlashcardAdapter(flashcardList);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
            spacingInPixels = 4;
            recyclerView.addItemDecoration(new SpaceItemDeco(spacingInPixels));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fcAdapter);
        }


         */

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


    private void queryFlashcardsData(String username) {
        DatabaseReference flashcardsRef;
        flashcardsRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(username).child("flashcards");

        flashcardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot flashcardSnapshot : dataSnapshot.getChildren()) {
                    String flashcardKey = flashcardSnapshot.getKey();

                    if (flashcardSnapshot.hasChild("score") && flashcardSnapshot.hasChild("percentage")) {
                        int score = flashcardSnapshot.child("score").getValue(Integer.class);
                        float percentage = flashcardSnapshot.child("percentage").getValue(Float.class);

                        List<String> questions = new ArrayList<>();
                        List<String> answers = new ArrayList<>();

                        for (DataSnapshot questionSnapshot : flashcardSnapshot.child("questions").getChildren()) {
                            String question = questionSnapshot.child("question").getValue(String.class);
                            String answer = questionSnapshot.child("answer").getValue(String.class);

                            questions.add(question);
                            answers.add(answer);
                        }
                        scoreList.add(score);
                        percentageList.add(percentage);

                        // Instantiate the Flashcard object with the retrieved data
                        Flashcard flashcard = new Flashcard(flashcardKey, questions, answers, null);
                        flashcardList.add(flashcard);

                        // Do something with the instantiated flashcard object
                        for (int s : scoreList){
                            Log.v("Score:",String.valueOf(s));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QuoteApp", "Error querying flashcards data", databaseError.toException());
            }
        });
    }





}