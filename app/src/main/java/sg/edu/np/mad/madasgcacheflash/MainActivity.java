package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    List<Flashcard> flashcardList = new ArrayList<>();
    List<Category> categories = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    private String username;
    private TextView quoteTextView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Date lastUpdatedDate; // Store the last updated date
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private String flashcardTitle;
    private String flashcardCategory;
    private TextInputEditText etFlashcardTitle;
    private TextInputEditText etFlashcardCategory;
    private Boolean hasImage = false;


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
            if(user.getDisplayName()!=null)
            {username=user.getDisplayName();}
            TextView welcomeTxt = findViewById(R.id.welcomeText);
            String welcomeMessage = getString(R.string.welcome_message);
            String formattedMessage = String.format(welcomeMessage, username);
            Log.i(title, "The username is nullllll, so it is a problem"+user.getDisplayName());

            welcomeTxt.setText(formattedMessage);
        } else {
            Log.i(title, "The username is null, so it is a problem");
        }

        // Set the interval for updating the streak (e.g., every 24 hours)
        long intervalMillis = 24 * 60 * 60 * 1000; // 24 hours

// Create an intent to start the StreakUpdateService
        Intent serviceIntent = new Intent(this, StreakUpdateService.class);

        //hardcoding the username to paul
        serviceIntent.putExtra("Username",username);
        Log.v("MustSee",username);

// Start the service
        startService(serviceIntent);


        quoteTextView = findViewById(R.id.textView11);

        // Set a click listener on the text view to fetch a new quote
        // Fetch and display the quote from Firebase Firestore
        fetchQuoteFromFirestore();

        // Set up click listener for the quote text view
        quoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch a new quote from the API and update in Firebase Firestore
                fetchQuoteFromDatabase();
            }
        });

        // Create the flashcards
        CreateDefaultFc createHandler = new CreateDefaultFc();
        flashcardList = createHandler.createFlashcards();
        createCategories();
        uploadNewFlashcards(categories, username);

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

        FloatingActionButton addFlashcard = findViewById(R.id.floatingActionButton);
        addFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStep1Layout();
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
                    } else if (id == R.id.search) {
                        Intent intent = new Intent(getApplicationContext(), Search.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    } else if (id == R.id.home) {
                        return true;

                    } else if (id == R.id.leaderboard) {
                        Intent intent = new Intent(getApplicationContext(), Leaderboard.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    } else if (id == R.id.about) {
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


    private void uploadNewFlashcards(List<Category> categories, String username) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        Log.d("QuoteApp", "Number of categories: " + categories.size());
        for (Category category : categories) {
            Log.d("QuoteApp", "Category name: " + category.getName());
            String categoryName = category.getName();
            DatabaseReference categoryRef = usersRef.child(username).child("categories").child(categoryName);

            for (Flashcard flashcard : category.getFlashcards()) {
                Log.d("QuoteApp", "Flashcard title: " + flashcard.getTitle());
                String flashcardTitle = flashcard.getTitle();

                // Check if the flashcard already exists in the database
                Query query = categoryRef.orderByChild("title").equalTo(flashcardTitle);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Flashcard with the same title already exists, update its data instead
                            /*
                            for (DataSnapshot flashcardSnapshot : dataSnapshot.getChildren()) {
                                String flashcardKey = flashcardSnapshot.getKey();
                                Flashcard existingFlashcard = flashcardSnapshot.getValue(Flashcard.class);

                                // Preserve the existing percentage and attempts data if available
                                Map<String, Double> existingPercentage = existingFlashcard.getPercentage();
                                Map<String, Integer> existingAttempts = existingFlashcard.getAttempts();
                                if (existingPercentage != null) {
                                    flashcard.setPercentage(existingPercentage);
                                } else {
                                    // If no percentage data exists, initialize with default values
                                    Map<String, Double> defaultPercentage = new HashMap<>();
                                    defaultPercentage.put("easy", 0.0);
                                    defaultPercentage.put("medium", 0.0);
                                    defaultPercentage.put("hard", 0.0);
                                    flashcard.setPercentage(defaultPercentage);
                                }

                                if (existingAttempts != null) {
                                    flashcard.setAttempts(existingAttempts);
                                } else {
                                    // If no attempts data exists, initialize with default values
                                    Map<String, Integer> defaultAttempts = new HashMap<>();
                                    defaultAttempts.put("easy", 0);
                                    defaultAttempts.put("medium", 0);
                                    defaultAttempts.put("hard", 0);
                                    flashcard.setAttempts(defaultAttempts);
                                }

                                DatabaseReference flashcardRef = categoryRef.child(flashcardKey);

                                // Update the flashcard data within the reference
                                flashcardRef.setValue(flashcard);
                                Log.d("QuoteApp", "Flashcard updated with key: " + flashcardKey);
                            }

                             */

                        } else {
                            // Flashcard with the same title doesn't exist, create a new one
                            DatabaseReference flashcardRef = categoryRef.push(); // Use the unique key generated by push()

                            // Set the percentage for each difficulty level (initialize with default values)
                            Map<String, Double> defaultPercentage = new HashMap<>();
                            defaultPercentage.put("easy", 0.0);
                            defaultPercentage.put("medium", 0.0);
                            defaultPercentage.put("hard", 0.0);
                            flashcard.setPercentage(defaultPercentage);

                            // Set the attempts for each difficulty level (initialize with default values)
                            Map<String, Integer> defaultAttempts = new HashMap<>();
                            defaultAttempts.put("easy", 0);
                            defaultAttempts.put("medium", 0);
                            defaultAttempts.put("hard", 0);
                            flashcard.setAttempts(defaultAttempts);

                            // Update the flashcard data within the reference
                            flashcardRef.setValue(flashcard);
                            Log.d("QuoteApp", "Flashcard uploaded with key: " + flashcardRef.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("QuoteApp", "Database error: " + databaseError.getMessage());
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
        shuffleCardIntent.putExtra("Username", username);
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

    private void showStep1Layout() {
        // Inflate the layout
        View step1Layout = getLayoutInflater().inflate(R.layout.create_flashcard_step_1, null);

        // Find the views inside the layout
        TextInputLayout tilFlashcardTitle = step1Layout.findViewById(R.id.tilFlashcardTitle);
        TextInputLayout tilFlashcardCategory = step1Layout.findViewById(R.id.tilFlashcardCategory);
        etFlashcardTitle = step1Layout.findViewById(R.id.etFlashcardTitle);
        etFlashcardCategory = step1Layout.findViewById(R.id.etFlashcardCategory);
        Button btnNext = step1Layout.findViewById(R.id.btnNext);

        // Create an AlertDialog to show the layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(step1Layout);
        builder.setCancelable(true); // Prevent dismissing the dialog when clicking outside

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Handle the "Next" button click
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values from EditText views
                flashcardTitle = etFlashcardTitle.getText().toString().trim();
                flashcardCategory = etFlashcardCategory.getText().toString().trim();

                // Perform input validation (e.g., check if fields are not empty)
                if (flashcardTitle.isEmpty()) {
                    tilFlashcardTitle.setError("Please enter a title");
                    return;
                }

                if (flashcardCategory.isEmpty()) {
                    tilFlashcardCategory.setError("Please enter a category");
                    return;
                }

                // Proceed to Step 2 or save the data, etc.
                alertDialog.dismiss(); // Dismiss the current dialog
                showStep2Layout();
            }
        });
    }

    private void showStep2Layout() {
        // Inflate the Step 2 layout
        View step2Layout = getLayoutInflater().inflate(R.layout.create_flashcard_step_2, null);

        // Find the views inside the layout
        CheckBox chkAddImage = step2Layout.findViewById(R.id.chkAddImage);
        ImageView imgFlashcardImage = step2Layout.findViewById(R.id.imgFlashcardImage);
        Button btnChooseImage = step2Layout.findViewById(R.id.btnChooseImage);
        Button btnBack = step2Layout.findViewById(R.id.btnBack);
        Button btnNext = step2Layout.findViewById(R.id.btnNext);

        // Create an AlertDialog to show the Step 2 layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(step2Layout);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showStep1Layout();
                // Set the stored values back to the EditText fields
                etFlashcardTitle.setText(flashcardTitle);
                etFlashcardCategory.setText(flashcardCategory);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Proceed to Step 3
                alertDialog.dismiss();
                showStep3Layout();
            }
        });

        // Handle the checkbox to show or hide the image view and button
        chkAddImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imgFlashcardImage.setVisibility(View.VISIBLE);
                    btnChooseImage.setVisibility(View.VISIBLE);
                    hasImage = true;
                } else {
                    imgFlashcardImage.setVisibility(View.GONE);
                    btnChooseImage.setVisibility(View.GONE);
                    hasImage = false;
                }
            }
        });

        // Handle the "Choose Image" button click
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to select an image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        });
    }

    private void showStep3Layout() {
        List<String> questionsList = new ArrayList<>();
        List<String> answersList = new ArrayList<>();
        // Inflate the Step 3 layout
        View step3Layout = getLayoutInflater().inflate(R.layout.create_flashcard_step_3, null);

        // Find the views inside the layout
        Button btnAddQuestion = step3Layout.findViewById(R.id.btnAddQuestion);
        Button btnNext = step3Layout.findViewById(R.id.btnNext);
        Button btnBack = step3Layout.findViewById(R.id.btnBack);
        LinearLayout containerQuestionsAndAnswers = step3Layout.findViewById(R.id.containerQuestionsAndAnswers);
        // Find the EditText views for question and answer inside the layout
        TextInputEditText etQuestion = step3Layout.findViewById(R.id.etQuestion);
        TextInputEditText etAnswer = step3Layout.findViewById(R.id.etAnswer);
        // Create an AlertDialog to show the Step 3 layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(step3Layout);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Handle the "Add More Question and Answer" button click
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user input from the EditText fields
                String question = etQuestion.getText().toString().trim();
                String answer = etAnswer.getText().toString().trim();

                // Check if both question and answer are not empty
                if (!question.isEmpty() && !answer.isEmpty()) {
                    // Add the question and answer to the respective lists
                    questionsList.add(question);
                    answersList.add(answer);

                    // Clear the EditText fields to get ready for the next input
                    etQuestion.getText().clear();
                    etAnswer.getText().clear();

                    // Optionally, you can show a toast or other feedback here to indicate that the input was added successfully
                } else {
                    // Optionally, show a toast or other feedback here to indicate that the fields cannot be empty
                    Toast.makeText(MainActivity.this, "Both question and answer must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle the "Next" button click
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if at least one question and answer set is added
                if (questionsList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Flashcard must have at least one question and answer", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Process the lists of questions and answers here
                StringBuilder flashcardContent = new StringBuilder();
                for (int i = 0; i < questionsList.size(); i++) {
                    String question = questionsList.get(i);
                    String answer = answersList.get(i);
                    flashcardContent.append("Question ").append(i + 1).append(": ").append(question).append("\n");
                    flashcardContent.append("Answer ").append(i + 1).append(": ").append(answer).append("\n\n");
                }
                Toast.makeText(MainActivity.this, flashcardContent.toString(), Toast.LENGTH_LONG).show();

                alertDialog.dismiss();
                showStep4Layout(questionsList, answersList);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showStep2Layout();
            }
        });

    }

    private void showStep4Layout(List<String> questions, List<String> answers) {
        // Inflate the Step 4 layout
        View step4Layout = getLayoutInflater().inflate(R.layout.create_flashcard_step_4, null);

        // Find the views inside the layout
        TextView tvFlashcardTitleSummary = step4Layout.findViewById(R.id.tvFlashcardTitleSummary);
        TextView tvFlashcardCategorySummary = step4Layout.findViewById(R.id.tvFlashcardCategorySummary);
        TextView tvImageSummary = step4Layout.findViewById(R.id.tvImageSummary);
        ImageView imgSummaryImage = step4Layout.findViewById(R.id.imgSummaryImage);
        LinearLayout containerQuestionsSummary = step4Layout.findViewById(R.id.containerQuestionsSummary);
        LinearLayout containerAnswersSummary = step4Layout.findViewById(R.id.containerAnswersSummary);
        ImageView imgEditTitle = step4Layout.findViewById(R.id.imgEditTitle);
        ImageView imgEditCategory = step4Layout.findViewById(R.id.imgEditCategory);
        ImageView imgEditImage = step4Layout.findViewById(R.id.imgEditImage);
        ImageView imgEditQuestions = step4Layout.findViewById(R.id.imgEditQuestions);
        ImageView imgEditAnswers = step4Layout.findViewById(R.id.imgEditAnswers);
        Button btnConfirm = step4Layout.findViewById(R.id.btnConfirm);

        // Set the summary text for Flashcard Title and Flashcard Category
        tvFlashcardTitleSummary.setText(flashcardTitle);
        tvFlashcardCategorySummary.setText(flashcardCategory);

        // Set the summary text for Image
        if (hasImage) {
            tvImageSummary.setText("Yes");
            imgSummaryImage.setVisibility(View.VISIBLE);
            imgSummaryImage.setImageURI(selectedImageUri);
        } else {
            tvImageSummary.setText("No");
            imgSummaryImage.setVisibility(View.GONE);
        }

        // Set the summary for Questions
        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i);
            TextView tvQuestion = new TextView(this);
            tvQuestion.setText((i + 1) + ". " + question);
            containerQuestionsSummary.addView(tvQuestion);
        }

// Display the initial summary for Answers
        for (int i = 0; i < answers.size(); i++) {
            String answer = answers.get(i);
            TextView tvAnswer = new TextView(this);
            tvAnswer.setText((i + 1) + ". " + answer);
            containerAnswersSummary.addView(tvAnswer);
        }

        // Create an AlertDialog to show the Step 4 layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(step4Layout);
        builder.setCancelable(false); // Prevent dismissing the dialog when clicking outside

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Handle the "Edit Flashcard Title" click
        imgEditTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog with an EditText for the user to edit the title
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Edit Flashcard Title");

                final EditText input = new EditText(MainActivity.this);
                input.setText(flashcardTitle); // Set the initial text to the current title
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update the flashcardTitle with the edited value
                        flashcardTitle = input.getText().toString().trim();
                        // Update the summary text in tvFlashcardTitleSummary
                        tvFlashcardTitleSummary.setText(flashcardTitle);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


        // Handle the "Edit Flashcard Category" click
        imgEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog with an EditText for the user to edit the category
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Edit Flashcard Category");

                final EditText input = new EditText(MainActivity.this);
                input.setText(flashcardCategory); // Set the initial text to the current category
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update the flashcardCategory with the edited value
                        flashcardCategory = input.getText().toString().trim();
                        // Update the summary text in tvFlashcardCategorySummary
                        tvFlashcardCategorySummary.setText(flashcardCategory);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        // Handle the "Edit Image" click
        imgEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog with a CheckBox to allow the user to change the image (Yes/No)
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Edit Image");

                final CheckBox checkBox = new CheckBox(MainActivity.this);
                checkBox.setText("Has Image");
                checkBox.setChecked(hasImage); // Set the initial value based on the current selection
                builder.setView(checkBox);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update the hasImage variable with the edited value
                        hasImage = checkBox.isChecked();
                        // Update the summary text in tvImageSummary and visibility of imgSummaryImage
                        if (hasImage) {
                            tvImageSummary.setText("Yes");
                            imgSummaryImage.setVisibility(View.VISIBLE);
                        } else {
                            tvImageSummary.setText("No");
                            imgSummaryImage.setVisibility(View.GONE);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        // Handle the "Edit Questions" click
        imgEditQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog with EditText fields for the user to edit questions
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Edit Questions");

                final LinearLayout container = new LinearLayout(MainActivity.this);
                container.setOrientation(LinearLayout.VERTICAL);

                // Create EditText fields for each question in the questionsList
                final List<EditText> questionEditTexts = new ArrayList<>();
                for (String question : questions) {
                    EditText editText = new EditText(MainActivity.this);
                    editText.setText(question);
                    container.addView(editText);
                    questionEditTexts.add(editText);
                }

                builder.setView(container);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update the questionsList with the edited values
                        questions.clear();
                        for (EditText editText : questionEditTexts) {
                            String editedQuestion = editText.getText().toString().trim();
                            if (!editedQuestion.isEmpty()) {
                                questions.add(editedQuestion);
                            }
                        }

                        // Update the summary in containerQuestionsSummary
                        containerQuestionsSummary.removeAllViews();
                        for (String question : questions) {
                            TextView tvQuestion = new TextView(MainActivity.this);
                            tvQuestion.setText(question);
                            containerQuestionsSummary.addView(tvQuestion);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        // Handle the "Edit Answers" click
        imgEditAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog with EditText fields for the user to edit answers
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Edit Answers");

                final LinearLayout container = new LinearLayout(MainActivity.this);
                container.setOrientation(LinearLayout.VERTICAL);

                // Create EditText fields for each answer in the answersList
                final List<EditText> answerEditTexts = new ArrayList<>();
                for (String answer : answers) {
                    EditText editText = new EditText(MainActivity.this);
                    editText.setText(answer);
                    container.addView(editText);
                    answerEditTexts.add(editText);
                }

                builder.setView(container);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update the answersList with the edited values
                        answers.clear();
                        for (EditText editText : answerEditTexts) {
                            String editedAnswer = editText.getText().toString().trim();
                            if (!editedAnswer.isEmpty()) {
                                answers.add(editedAnswer);
                            }
                        }

                        // Update the summary in containerAnswersSummary
                        containerAnswersSummary.removeAllViews();
                        for (String answer : answers) {
                            TextView tvAnswer = new TextView(MainActivity.this);
                            tvAnswer.setText(answer);
                            containerAnswersSummary.addView(tvAnswer);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        // Handle the "Confirm" button click in Step 4
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the flashcardTitle and flashcardCategory with the edited values (if any)
                String editedTitle = tvFlashcardTitleSummary.getText().toString().trim();
                String editedCategory = tvFlashcardCategorySummary.getText().toString().trim();
                flashcardTitle = !editedTitle.isEmpty() ? editedTitle : flashcardTitle;
                flashcardCategory = !editedCategory.isEmpty() ? editedCategory : flashcardCategory;

                // Create a new Flashcard object
                Flashcard flashcard = new Flashcard();
                flashcard.setTitle(flashcardTitle);
                flashcard.setCategory(flashcardCategory);
                //flashcard.setHasImage(hasImage);

                // Add questions and answers to the flashcard
                flashcard.setQuestions(questions);
                flashcard.setAnswers(answers);

                // Add the flashcard to the flashcardList
                flashcardList.add(flashcard);

                Toast.makeText(MainActivity.this, "Flashcard created successfully", Toast.LENGTH_SHORT).show();
                createCategories();
                uploadNewFlashcards(categories, username);
                // Close the Step 4 dialog
                alertDialog.dismiss();
            }
        });
    }


    private void createCategories() {
        // Iterate through the flashcardList and organize flashcards into categories
        for (Flashcard flashcard : flashcardList) {
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

        for (Flashcard flashcard : flashcardList) {
            // Check if the flashcard belongs to the selected category
            if (flashcard.getCategory().equals(selectedCategory)) {
                flashcardsToShow.add(flashcard);
            }
        }


        // Set up the RecyclerView with the filtered flashcardList
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

        // Set up the RecyclerView with the filtered flashcardList
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
                //alertDialogQuiz(flashcard);
                // Start FlashCardQuestionPage activity with the selected flashcard

                /*Intent intent = new Intent(MainActivity.this, Testyourself.class);
                intent.putExtra("flashcard", flashcard);
                intent.putExtra("Username", username);
                startActivity(intent);
                //startShuffleCardActivity(flashcard);*/
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Test Yourself");
                builder.setMessage("What kind of test would you like?");
                builder.setCancelable(false);
                builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Intent intent = new Intent(MainActivity.this, DifficultyLevelActivity.class);
                        intent.putExtra("flashcard", flashcard);
                        intent.putExtra("Username", username);
                        startActivityForResult(intent, 1);
                    }
                });
                builder.setNegativeButton("MCQ Quiz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, MCQuiz.class);
                        intent.putExtra("flashcard", flashcard);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();
            }
        });
    }



    private void displayAllFlashcards() {
        RecyclerView recyclerView;
        FlashcardAdapter fcAdapter = new FlashcardAdapter(flashcardList);
        LinearLayoutManager mLayoutManager;
        int spacingInPixels;

        for (Flashcard flashcard : flashcardList) {
            recyclerView = findViewById(R.id.recyclerView1);
            fcAdapter = new FlashcardAdapter(flashcardList);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
            spacingInPixels = 4;
            recyclerView.addItemDecoration(new SpaceItemDeco(spacingInPixels));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fcAdapter);
        }

        /*
        fcAdapter.setOnItemClickListener(new FlashcardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Flashcard flashcard) {
                // Start FlashCardQuestionPage activity with the selected flashcard
                Intent intent = new Intent(MainActivity.this, LearnYourself.class);
                intent.putExtra("flashcard", flashcard);
                Log.v("Username out:", username);
                intent.putExtra("Username", username);
                startActivity(intent);
                startShuffleCardActivity(flashcard);
            }
        });

         */

        for (Flashcard flashcard : flashcardList) {
            // Access the current flashcard
            // You can perform operations or access its properties here
            String title = flashcard.getTitle();

            recyclerView = findViewById(R.id.recyclerView2);
            fcAdapter = new FlashcardAdapter(flashcardList);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Test Yourself");
                builder.setMessage("What kind of test would you like?");
                builder.setCancelable(false);
                builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Intent intent = new Intent(MainActivity.this, DifficultyLevelActivity.class);
                        intent.putExtra("flashcard", flashcard);
                        intent.putExtra("Username", username);
                        startActivityForResult(intent, 1);
                    }
                });
                builder.setNegativeButton("MCQ Quiz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, MCQuiz.class);
                        intent.putExtra("flashcard", flashcard);
                        intent.putExtra("Username", username);
                        startActivity(intent);


                    }
                });
                AlertDialog alert=builder.create();
                alert.show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the DifficultyLevelActivity
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Handle the result for the DifficultyLevelActivity here
            int difficultyLevel = data.getIntExtra("difficultyLevel", 0);
            int timerDuration = data.getIntExtra("timerDuration", -1);
            Flashcard flashcard = data.getParcelableExtra("flashcard");
            if (flashcard != null) {
                Log.d("MainActivity", "Receiveds Flashcard: " + flashcard.getTitle());
            // Now start the Testyourself activity with the selected difficulty level and timer duration
            Intent intent = new Intent(MainActivity.this, Testyourself.class);
            intent.putExtra("flashcard", flashcard);
            intent.putExtra("difficultyLevel", difficultyLevel);
            intent.putExtra("timerDuration", timerDuration);
            intent.putExtra("username",username);
            startActivity(intent);
        } else {
                Log.e("MainActivity", "Flashcard is null.");
            }
        }
        // Add more conditions for other requestCodes if needed
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            // Set the selected image to the ImageView
            ImageView imgFlashcardImage = findViewById(R.id.imgFlashcardImage);
            imgFlashcardImage.setImageURI(selectedImageUri);
        }
    }

}





