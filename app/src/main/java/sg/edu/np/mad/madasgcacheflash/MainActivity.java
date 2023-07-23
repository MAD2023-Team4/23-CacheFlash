package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String title = "Main Activity";
    List<Flashcard> fList = new ArrayList<>();
    List<Category> categories = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    private String username;
    private TextView quoteTextView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Date lastUpdatedDate; // Store the last updated date



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");

        Log.d("MainActivity", "Received Username: " + username);
        FirebaseApp.initializeApp(MainActivity.this);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            TextView welcomeTxt = findViewById(R.id.welcomeText);
            String welcomeMessage = getString(R.string.welcome_message);
            String formattedMessage = String.format(welcomeMessage, username);

            welcomeTxt.setText(formattedMessage);
        } else {
            Log.i(title, "The username is null, so it is a problem");
        }

        // Set the interval for updating the streak (e.g., every 24 hours)
        long intervalMillis = 24 * 60 * 60 * 1000; // 24 hours

// Create an intent to start the StreakUpdateService
        Intent serviceIntent = new Intent(this, StreakUpdateService.class);
        serviceIntent.putExtra("Username",username);
// Start the service
        startService(serviceIntent);


        quoteTextView = findViewById(R.id.textView11);

        // Set a click listener on the text view to fetch a new quote
        // Fetch and display the quote from Firebase Firestore
        fetchQuoteFromFirestore();

        // Set up click listener for the quote text view
        // Set up click listener for the quote text view
        quoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch a new quote from the API and update in database
                fetchQuoteFromDatabase();
         }
        });

        // Create the flashcards, from the CreateDefaultFlash handler
        //_______________________________________________________________________________________
        CreateDefaultFlash createHandler = new CreateDefaultFlash();
        fList = createHandler.createFlashcards();

        createCategories();
        uploadNewFlashcards(categories, username);

        // Method to display flashcards based on selected category
        //_________________________________________________________________________________________
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);
        userRef.child("favoriteCategory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String selectedCategory = snapshot.getValue(String.class);
                    // Call the method to display flashcards based on the selected category
                    displayFlashcardsByCategory(selectedCategory);
                } else {
                    // If there is no selected category, display all flashcards
                    displayAllFlashcards();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur while fetching the data (optional).
            }
        });


        // Bottom Navigation View
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item != null) {
                    int id = item.getItemId();

                    if (id == R.id.dashboard) {
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
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
                        return true;

                    }
                    else if (id == R.id.leaderboard) {
                        Intent intent = new Intent(getApplicationContext(), Leaderboard.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    else if (id == R.id.about) {
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void fetchQuoteFromFirestore() {
        DocumentReference quoteRef = db.collection("quotes").document("quote_of_the_day");
        quoteRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        String quote = document.getString("quote");
                        String author = document.getString("author");
                        lastUpdatedDate = document.getDate("lastUpdated");

                        if (lastUpdatedDate != null && isSameDay(lastUpdatedDate, Calendar.getInstance().getTime())) {
                            // Display the stored quote if it is the same day
                            String formattedQuote = String.format(Locale.getDefault(), "%s\n- %s", quote, author);
                            quoteTextView.setText(formattedQuote);
                        } else {
                            // Fetch a new quote from the API and update in Firebase Firestore
                            fetchNewQuoteFromAPI();
                        }
                    } else {
                        // Document does not exist, fetch a new quote from the API and update in Firebase Firestore
                        fetchNewQuoteFromAPI();
                    }
                } else {
                    Log.e("QuoteApp", "Error getting quote document", task.getException());
                }
            }
        });
    }

    private void fetchNewQuoteFromAPI() {
        Log.d("QuoteApp", "fetchNewQuoteFromAPI");

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://api.api-ninjas.com/v1/quotes?category=success");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("accept", "application/json");
                    connection.setRequestProperty("X-Api-Key", "7N/Wm8b2g7xvfFYTnyr05g==HQKXwuwskx8e2Cor");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream responseStream = connection.getInputStream();
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(responseStream);

                        // Log the API response for debugging
                        Log.d("QuoteApp", "API Response: " + root.toString());

                        // Check if the response contains a quote and author
                        if (root.isArray() && root.size() > 0) {
                            JsonNode quoteNode = root.get(0).path("quote");
                            JsonNode authorNode = root.get(0).path("author");

                            if (quoteNode.isTextual() && authorNode.isTextual()) {
                                String quote = quoteNode.asText();
                                String author = authorNode.asText();

                                // Store the new quote and author in Firebase Firestore
                                Map<String, Object> quoteMap = new HashMap<>();
                                quoteMap.put("quote", quote);
                                quoteMap.put("author", author);
                                quoteMap.put("lastUpdated", Calendar.getInstance().getTime());

                                db.collection("quotes").document("quote_of_the_day")
                                        .set(quoteMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Display the new quote
                                                String formattedQuote = String.format(Locale.getDefault(), "%s\n- %s", quote, author);
                                                quoteTextView.setText(formattedQuote);
                                                Log.d("QuoteApp", "Received Quote: " + quote);
                                                Log.d("QuoteApp", "Author: " + author);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("QuoteApp", "Failed to update quote", e);
                                            }
                                        });
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("QuoteApp", "Error fetching quote from API", e);
                }

                return null;
            }
        }.execute();
    }



    private void uploadNewFlashcards(List<Category> categories, String username) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        Log.d("QuoteApp", "Number of categories: " + categories.size());
        for (Category category : categories) {
            Log.d("QuoteApp", "Category name: " + category.getName());
            String categoryName = category.getName();
            DatabaseReference categoryRef = usersRef.child(username).child("categories").child(categoryName);

            for (Flashcard flashcard : category.getFlashcards()) {
                Log.d("QuoteApp", "Flashcard title: " + flashcard.getTitle());
                String flashcardName = flashcard.getTitle();
                DatabaseReference flashcardRef = categoryRef.child(flashcardName); // Use the flashcard name as the key

                flashcardRef.setValue(flashcard)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("QuoteApp", "Flashcard uploaded with name: " + flashcardName);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("QuoteApp", "Failed to upload flashcard", e);
                            }
                        });
            }
        }
    }





    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }



    private void startShuffleCardActivity(Flashcard flashcard) {
        Intent shuffleCardIntent = new Intent(this, ShuffleCardActivity.class);
        shuffleCardIntent.putExtra("flashcard", flashcard);
        shuffleCardIntent.putExtra("Username",username);
        startActivity(shuffleCardIntent);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Check if the activity was launched from a notification click
        if (getIntent().getAction() != null && getIntent().getAction().equals("NOTIFICATION_CLICKED")) {
            // Retrieve the username from the intent's extras
            String username = getIntent().getStringExtra("Username");
            Log.v("Received username", username);

            if (username != null) {
                // Update the UI with the retrieved username
                TextView welcomeTxt = findViewById(R.id.welcomeText);
                String welcomeMessage = getString(R.string.welcome_message);
                String formattedMessage = String.format(welcomeMessage, username);
                welcomeTxt.setText(formattedMessage);
            }
        } else {
            // The activity was not launched from a notification click, handle as usual
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                TextView welcomeTxt = findViewById(R.id.welcomeText);
                // Create a StringBuilder object
                StringBuilder builder = new StringBuilder();

                // Append the welcome message
                builder.append(getString(R.string.welcome_message, username));
                builder.append("!");

                // Set the text of the welcome text view
                welcomeTxt.setText(builder.toString());
            } else {
                Log.i(title, "The username is null, so it is a problem");
            }
        }
    }

    private void createCategories() {
        // Iterate through the fList and organize flashcards into categories
        for (Flashcard flashcard : fList) {
            String categoryTitle = flashcard.getCategory();
            Log.d("DEBUG", "Processing flashcard with title: " + flashcard.getTitle() + ", category: " + categoryTitle);
            Category category = getCategoryByName(categories, categoryTitle);

            if (category == null) {
                // If the category doesn't exist yet, create a new one
                List<Flashcard> flashcardsInCategory = new ArrayList<>();
                flashcardsInCategory.add(flashcard);
                category = new Category(categoryTitle, flashcardsInCategory);
                categories.add(category);
                Log.d("DEBUG", "New category created: " + categoryTitle);
            } else {
                // Add the flashcard to the existing category
                category.getFlashcards().add(flashcard);
                Log.d("DEBUG", "Flashcard added to category: " + categoryTitle);
            }
        }
        // At this point, the flashcards are organized into categories based on their titles
    }

    // Helper method to get a category by its name
    private Category getCategoryByName(List<Category> categories, String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null; // Category not found
    }

    private void displayFlashcardsByCategory(String selectedCategory) {
        List<Flashcard> flashcardsToShow = new ArrayList<>();
        RecyclerView recyclerView;
        FlashcardAdapter fcAdapter;
        LinearLayoutManager mLayoutManager;
        int spacingInPixels;

        for (Flashcard flashcard : fList) {
            // Check if the flashcard belongs to the selected category
            if (flashcard.getCategory().equals(selectedCategory)) {
                flashcardsToShow.add(flashcard);
            }
        }


        // Set up the RecyclerView with the filtered fList
        //Learn Yourself
        recyclerView = findViewById(R.id.recyclerView1);
        fcAdapter = new FlashcardAdapter(flashcardsToShow);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        spacingInPixels = 4;
        recyclerView.addItemDecoration(new SpaceItemDeco(spacingInPixels));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fcAdapter);

        fcAdapter.setOnItemClickListener(new FlashcardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Flashcard flashcard) {
                // Start FlashCardQuestionPage activity with the selected flashcard
                Intent intent = new Intent(MainActivity.this, LearnYourself.class);
                intent.putExtra("flashcard", flashcard);
                intent.putExtra("Username", username);
                startActivity(intent);
                startShuffleCardActivity(flashcard);
            }
        });

        // Set up the RecyclerView with the filtered fList
        //Test Yourself
        recyclerView = findViewById(R.id.recyclerView2);
        fcAdapter = new FlashcardAdapter(flashcardsToShow);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        spacingInPixels = 4;
        recyclerView.addItemDecoration(new SpaceItemDeco(spacingInPixels));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fcAdapter);

        fcAdapter.setOnItemClickListener(new FlashcardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Flashcard flashcard) {
                // Start FlashCardQuestionPage activity with the selected flashcard
                Intent intent = new Intent(MainActivity.this, Testyourself.class);
                intent.putExtra("flashcard", flashcard);
                intent.putExtra("Username", username);
                startActivity(intent);
                //startShuffleCardActivity(flashcard);
            }
        });
    }
    private void displayAllFlashcards(){
        RecyclerView recyclerView;
        FlashcardAdapter fcAdapter = new FlashcardAdapter(fList);
        LinearLayoutManager mLayoutManager;
        int spacingInPixels;

        for (Flashcard flashcard : fList) {
            recyclerView = findViewById(R.id.recyclerView1);
            fcAdapter = new FlashcardAdapter(fList);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
            spacingInPixels = 4;
            recyclerView.addItemDecoration(new SpaceItemDeco(spacingInPixels));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fcAdapter);
        }

        fcAdapter.setOnItemClickListener(new FlashcardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Flashcard flashcard) {
                // Start FlashCardQuestionPage activity with the selected flashcard
                Intent intent = new Intent(MainActivity.this, LearnYourself.class);
                intent.putExtra("flashcard", flashcard);
                Log.v("Username out:",username);
                intent.putExtra("Username", username);
                startActivity(intent);
                startShuffleCardActivity(flashcard);
            }
        });

        for (Flashcard flashcard : fList) {
            // Access the current flashcard
            // You can perform operations or access its properties here
            String title = flashcard.getTitle();

            recyclerView = findViewById(R.id.recyclerView2);
            fcAdapter = new FlashcardAdapter(fList);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
            spacingInPixels = 4;
            recyclerView.addItemDecoration(new SpaceItemDeco(spacingInPixels));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fcAdapter);
        }
        fcAdapter.setOnItemClickListener(new FlashcardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Flashcard flashcard) {
                // Start FlashCardQuestionPage activity with the selected flashcard
                Intent intent = new Intent(MainActivity.this, Testyourself.class);
                intent.putExtra("flashcard", flashcard);
                startActivity(intent);
            }
        });
    }

    private void fetchQuoteFromDatabase() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://api.api-ninjas.com/v1/quotes?category=success");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("accept", "application/json");
                    connection.setRequestProperty("X-Api-Key", "7N/Wm8b2g7xvfFYTnyr05g==HQKXwuwskx8e2Cor");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream responseStream = connection.getInputStream();
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(responseStream);

                        // Log the API response for debugging
                        Log.d("QuoteApp", "API Response: " + root.toString());

                        // Check if the response contains a quote and author
                        if (root.isArray() && root.size() > 0) {
                            JsonNode quoteNode = root.get(0).path("quote");
                            JsonNode authorNode = root.get(0).path("author");

                            if (quoteNode.isTextual() && authorNode.isTextual()) {
                                String quote = quoteNode.asText();
                                String author = authorNode.asText();

                                // Store the new quote and author in Firebase Realtime Database
                                DatabaseReference quoteRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("quote");
                                Map<String, Object> quoteMap = new HashMap<>();
                                quoteMap.put("quote", quote);
                                quoteMap.put("author", author);
                                quoteRef.setValue(quoteMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Display the new quote
                                                String formattedQuote = String.format(Locale.getDefault(), "%s\n- %s", quote, author);
                                                quoteTextView.setText(formattedQuote);
                                                Log.d("QuoteApp", "Received Quote: " + quote);
                                                Log.d("QuoteApp", "Author: " + author);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("QuoteApp", "Failed to update quote", e);
                                            }
                                        });
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e("QuoteApp", "Error fetching quote from API", e);
                }
                return null;
            }
        }.execute();
    }


}


