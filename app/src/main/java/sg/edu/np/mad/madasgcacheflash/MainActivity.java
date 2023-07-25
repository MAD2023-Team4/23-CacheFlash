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
        serviceIntent.putExtra("Username", username);
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
        createFlashcards();
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
                showStep4Layout(questionsList,answersList);
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
                uploadNewFlashcards(categories,username);
                // Close the Step 4 dialog
                alertDialog.dismiss();
            }
        });
    }






    private void createFlashcards() {
        // Create and add flashcards to the flashcardList

        Flashcard france = new Flashcard();
        france.setTitle("France");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        questions.add("What is the capital of France?");
        questions.add("What is the largest city in France?");
        questions.add("What is the national language of France?");
        answers.add("Paris");
        answers.add("Lyon");
        answers.add("French");
        france.setQuestions(questions);
        france.setAnswers(answers);
        france.setCategory("Social Studies");
        flashcardList.add(france);

        Flashcard math = new Flashcard();
        math.setTitle("Logarithm and Calculus");
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        questions.add("What is the product rule of logarithm?");
        questions.add("What is the quotient rule of logarithm?");
        questions.add("What is the notation for differentiation?");
        answers.add("logb(xy) = logb x + logb y");
        answers.add("loga(x/y)  = loga x – loga y");
        answers.add("dy/dx or f'(x)");
        math.setQuestions(questions);
        math.setAnswers(answers);
        math.setCategory("Math");
        flashcardList.add(math);

        //___________________________________________________________________________________________

        Flashcard socialStudies = new Flashcard();
        socialStudies.setTitle("General Knowledge");
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        questions.add("When did Singapore gain independence?");
        questions.add("Who won the gold medal in Rio 2016?");
        questions.add("Who is the president of Singapore in 2020?");
        questions.add("Who is the Franz Josef Islands named after?");
        questions.add("What is the full name of North Korea?");
        questions.add("What is the real name of Greece (country)");
        questions.add("What very chewy confectionary was banned in sales in Singapore?");
        questions.add("What anthem was played in Singapore before Majulah Singapura was used?");
        questions.add("What is the capital city of Singapore?");
        questions.add("Who was the first prime minister of Singapore?");
        questions.add("What was Singapore previously known as, and starts with 'T'?");
        questions.add("What is the most popular religion in Singapore?");
        questions.add("What is the national flower of Singapore?");
        questions.add("What is the highest point in Singapore, and what is its height?");
        questions.add("What is the name of the strait that separates Singapore from Malaysia?");
        questions.add("What is the name of the longest suspension bridge in Singapore?");
        questions.add("What is the name of the tallest building in Singapore?");
        questions.add("What is the national sport of Singapore?");

        answers.add("1965");
        answers.add("Joseph Schooling");
        answers.add("Mdm Halimah Yacob");
        answers.add("Franz Joseph I");
        answers.add("Democratic People's Republic of Korea");
        answers.add("The Hellenic Republic");
        answers.add("Chewing gum");
        answers.add("God Save the Queen");
        answers.add("Singapore");
        answers.add("Mr Lee Kuan Yew");
        answers.add("Temasek");
        answers.add("Buddhism");
        answers.add("The Vanda Miss Joaqium");
        answers.add("Bukit Timah Hill, 163.66m");
        answers.add("Johor Strait");
        answers.add("Benjamin Sheares Bridge");
        answers.add("Marina Bay Sands SkyPark Observation Deck");
        answers.add("Netball");
        socialStudies.setQuestions(questions);
        socialStudies.setAnswers(answers);
        socialStudies.setCategory("Social Studies");
        flashcardList.add(socialStudies);

        //___________________________________________________________________________________________

        Flashcard economics = new Flashcard();
        economics.setTitle("General Economics");
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        questions.add("What is the study of how people make choices?");
        questions.add("What do economists study?");
        questions.add("What is the basic economic problem?");
        questions.add("What is the law of demand?");
        questions.add("What is the law of supply?");
        questions.add("What is a market?");
        questions.add("What is a market economy?");
        questions.add("What is a command economy?");
        questions.add("What is a mixed economy?");
        questions.add("What is GDP?");
        questions.add("What is inflation?");
        questions.add("What is deflation?");
        questions.add("What is unemployment?");
        questions.add("What is a recession?");
        questions.add("What is a depression?");
        questions.add("What is a boom?");
        questions.add("What is a bubble?");
        questions.add("What is a financial crisis?");
        questions.add("What is a trade deficit?");
        questions.add("What is a trade surplus?");
        questions.add("What is a balance of payments?");
        questions.add("What is a currency?");
        questions.add("What is a central bank?");
        questions.add("What is interest?");
        questions.add("What is inflation targeting?");
        questions.add("What is quantitative easing?");
        questions.add("What is fiscal policy?");
        questions.add("What is a budget deficit?");
        questions.add("What is a budget surplus?");


        answers.add("Economics");
        answers.add("Consumption of goods, services");
        answers.add("Scarcity");
        answers.add("As the price of a good increases, demand for the good decreases");
        answers.add("As the price of a good increases, supply of the good increases");
        answers.add("A place where buyers and sellers of goods and services come together");
        answers.add("An economy where prices of goods & services are determined " +
                "by supply and demand");
        answers.add("An economy where the government controls the prices of goods and services");
        answers.add("An economy that combines elements of a market economy and a command economy");
        answers.add("Gross domestic product, total output of goods and services produced in a country");
        answers.add("A rise in the general level of prices");
        answers.add("A fall in the general level of prices");
        answers.add("How many of people who are willing and able to work but cannot find a job");
        answers.add("Two continuous quarters of negative economic growth");
        answers.add("A severe recession");
        answers.add("A period of rapid economic growth");
        answers.add("A period of rapid price increases in an asset, followed by a crash");
        answers.add("A widespread collapse of the financial system");
        answers.add("When a country imports more goods and services than it exports");
        answers.add("When a country exports more goods and services than it imports");
        answers.add("A record of a country's economic transactions with the rest of the world");
        answers.add("A medium of exchange, unit of account, and store of value");
        answers.add("An institution that manages a country's currency and monetary policy");
        answers.add("The price paid for the use of money");
        answers.add("A monetary policy framework which the central bank aims to keep inflation at target level");
        answers.add("A monetary policy tool which the central bank buys government bonds to increase money");
        answers.add("Government policy that affects the economy, through taxation and spending");
        answers.add("When the government spends more money than it collects in taxes");
        answers.add("When the government collects more money in taxes than it spends");

        //Source from:
        /* Bard, and
        Khan Academy: https://www.khanacademy.org/economics-finance-domain/macroeconomics
        The Economist: https://www.economist.com/
        Investopedia: https://www.investopedia.com/
        The Balance: https://www.thebalance.com/
        Britannica: https://www.britannica.com/
        https://www.bls.gov/ooh/life-physical-and-social-science/economists.htm#:~:text=Economists%20analyze%20topics%20related%20to,%2C%20health%2C%20and%20the%20environment.
         */


        economics.setQuestions(questions);
        economics.setAnswers(answers);
        economics.setCategory("Economics");
        flashcardList.add(economics);

        //___________________________________________________________________________________________

        Flashcard internationalTrade = new Flashcard();
        internationalTrade.setTitle("International Trade");
        questions = new ArrayList<>();
        answers = new ArrayList<>();

        questions.add("What is international trade?");
        answers.add("The exchange of goods, services, and capital across international borders.");

        questions.add("Explain the principle of comparative advantage.");
        answers.add("A country should specialize in producing the goods it can produce most efficiently relative to other countries and trade for goods it cannot produce as efficiently.");

        questions.add("What are the benefits of international trade?");
        answers.add("Increased efficiency, access to a wider variety of goods, and economic growth.");

        questions.add("Define trade surplus.");
        answers.add("When a country exports more goods and services than it imports.");

        questions.add("Explain trade deficit.");
        answers.add("When a country imports more goods and services than it exports.");

        questions.add("What are import tariffs?");
        answers.add("Taxes imposed on imported goods to protect domestic industries and raise revenue for the government.");

        questions.add("Define trade barriers.");
        answers.add("Government-imposed restrictions on international trade, such as tariffs, quotas, and embargoes.");

        questions.add("Explain the concept of trade liberalization.");
        answers.add("The removal or reduction of trade barriers to promote free trade.");

        questions.add("What is a free trade agreement?");
        answers.add("A pact between two or more countries to reduce or eliminate trade barriers among them.");

        questions.add("Define balance of trade.");
        answers.add("The difference between a country's exports and imports of goods and services.");

        questions.add("Explain the role of the World Trade Organization (WTO).");
        answers.add("It is an international organization that deals with global rules of trade between nations.");

        questions.add("What are export subsidies?");
        answers.add("Financial incentives given by governments to domestic companies to encourage exporting.");

        questions.add("Define protectionism.");
        answers.add("The economic policy of restricting trade between countries to protect domestic industries.");

        questions.add("Explain the terms of trade.");
        answers.add("The ratio at which a country can trade its exports for imports from other countries.");

        questions.add("What is a trade bloc?");
        answers.add("A group of countries that join together and eliminate trade barriers among themselves.");

        questions.add("Define foreign direct investment (FDI).");
        answers.add("When a company or individual from one country invests directly in assets in another country.");

        questions.add("Explain the infant industry argument.");
        answers.add("A justification for trade protectionism, stating that young domestic industries need protection to grow and compete with established foreign competitors.");

        questions.add("What are the implications of a devaluation of a country's currency?");
        answers.add("It makes a country's exports cheaper and imports more expensive, potentially improving the trade balance.");

        questions.add("Define trade sanctions.");
        answers.add("Economic penalties imposed by one or more countries against a targeted country to pressure it to change its policies.");

        questions.add("Explain the concept of trade dumping.");
        answers.add("When a country exports goods to another country at a price lower than the production cost to gain a competitive advantage.");

        questions.add("What are trade negotiations?");
        answers.add("Discussions between countries to reach agreements on international trade issues.");

        questions.add("Explain the concept of trade in services.");
        answers.add("The exchange of services, such as banking, tourism, and consulting, between countries.");

        questions.add("Define the terms 'exports' and 'imports'.");
        answers.add("Exports refer to goods and services sold to foreign countries, while imports refer to goods and services bought from foreign countries.");

        questions.add("What is a current account deficit?");
        answers.add("When a country's imports of goods, services, and transfers exceed its exports and income received from abroad.");

        questions.add("Explain the concept of foreign exchange reserves.");
        answers.add("The foreign currencies held by a country's central bank and used to settle international transactions.");
        internationalTrade.setQuestions(questions);
        internationalTrade.setAnswers(answers);
        internationalTrade.setCategory("Economics");
        flashcardList.add(internationalTrade);

        //___________________________________________________________________________________________

        Flashcard monetaryPolicy = new Flashcard();
        monetaryPolicy.setTitle("Monetary Policy");
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        questions.add("What is monetary policy?");
        answers.add("The use of central bank tools to control the money supply and interest rates to achieve economic goals.");

        questions.add("Explain the role of the central bank in monetary policy.");
        answers.add("The central bank is responsible for implementing monetary policy and regulating the banking system.");

        questions.add("What are the main goals of monetary policy?");
        answers.add("Stable prices, full employment, and economic growth.");

        questions.add("Define inflation.");
        answers.add("A sustained increase in the general price level of goods and services in an economy over time.");

        questions.add("Explain the concept of deflation.");
        answers.add("A sustained decrease in the general price level of goods and services in an economy over time.");

        questions.add("What is the Federal Reserve (Fed)?");
        answers.add("The central bank of the United States, responsible for conducting monetary policy.");

        questions.add("Define the discount rate.");
        answers.add("The interest rate at which the central bank lends money to commercial banks.");

        questions.add("Explain open market operations.");
        answers.add("The buying and selling of government securities by the central bank to control the money supply.");

        questions.add("What is the reserve requirement?");
        answers.add("The percentage of deposits that banks are required to hold as reserves.");

        questions.add("Define expansionary monetary policy.");
        answers.add("A policy that aims to stimulate economic growth by increasing the money supply and reducing interest rates.");

        questions.add("Explain contractionary monetary policy.");
        answers.add("A policy that aims to slow down economic growth by reducing the money supply and increasing interest rates.");

        questions.add("What is the Taylor rule?");
        answers.add("A formula that guides central banks in setting the appropriate interest rate based on inflation and economic output.");

        questions.add("Define quantitative easing.");
        answers.add("A monetary policy in which a central bank buys financial assets to increase the money supply.");

        questions.add("Explain the concept of the money multiplier.");
        answers.add("The ratio of the change in the money supply to the change in the monetary base.");

        questions.add("What is the Phillips curve?");
        answers.add("A concept that shows the trade-off between inflation and unemployment.");

        questions.add("Define the natural rate of unemployment.");
        answers.add("The rate of unemployment when the economy is at its potential output.");

        questions.add("Explain the concept of the zero lower bound.");
        answers.add("The lower limit to which interest rates can be reduced, typically close to zero.");

        questions.add("What is the role of the European Central Bank (ECB)?");
        answers.add("The central bank of the Eurozone, responsible for monetary policy and the euro currency.");

        questions.add("Define exchange rate.");
        answers.add("The rate at which one currency can be exchanged for another.");

        questions.add("Explain the concept of a currency peg.");
        answers.add("A fixed exchange rate system in which a country's currency is tied to another currency or a basket of currencies.");

        questions.add("What are the advantages of an independent central bank?");
        answers.add("It can focus on long-term economic stability without political interference.");

        questions.add("Define the real interest rate.");
        answers.add("The nominal interest rate adjusted for inflation.");

        questions.add("Explain the concept of forward guidance in monetary policy.");
        answers.add("Central banks use forward guidance to communicate their future policy intentions to influence expectations and economic behavior.");

        questions.add("What are the challenges of implementing effective monetary policy?");
        answers.add("Lags in policy impact, uncertainty, and potential unintended consequences.");

        questions.add("Define the money supply.");
        answers.add("The total amount of money in circulation in an economy, including cash and various forms of deposits.");

        monetaryPolicy.setQuestions(questions);
        monetaryPolicy.setAnswers(answers);
        monetaryPolicy.setCategory("Economics");
        flashcardList.add(monetaryPolicy);

        //___________________________________________________________________________________________

        Flashcard supplyAndDemand = new Flashcard();
        supplyAndDemand.setTitle("Supply and Demand");
        questions = new ArrayList<>();
        answers = new ArrayList<>();

        questions.add("What is supply?");
        answers.add("Supply refers to the quantity of a product or service that producers are willing and able to provide at various price levels in the market.");

        questions.add("What is demand?");
        answers.add("Demand refers to the quantity of a product or service that consumers are willing and able to buy at different price points in the market.");

        questions.add("How does price affect demand?");
        answers.add("As the price of a product decreases, the quantity demanded usually increases, and vice versa, assuming all other factors remain constant.");

        questions.add("How does price affect supply?");
        answers.add("As the price of a product increases, the quantity supplied generally increases, and vice versa, assuming all other factors remain constant.");

        questions.add("Explain the law of supply and demand.");
        answers.add("The law of supply states that as the price of a good or service increases, the quantity supplied also increases. The law of demand states that as the price of a good or service increases, the quantity demanded decreases, and vice versa.");

        questions.add("What is the equilibrium price?");
        answers.add("The equilibrium price is the market price at which the quantity demanded equals the quantity supplied, resulting in a stable market.");

        questions.add("What happens when there is a surplus in the market?");
        answers.add("A surplus occurs when the quantity supplied exceeds the quantity demanded at the prevailing price. This typically leads to a decrease in price until the surplus is eliminated.");

        questions.add("What happens when there is a shortage in the market?");
        answers.add("A shortage occurs when the quantity demanded exceeds the quantity supplied at the current price. This often results in an increase in price until the shortage is resolved.");

        questions.add("Explain the concept of elasticity of demand.");
        answers.add("Elasticity of demand measures how responsive the quantity demanded of a good or service is to changes in its price. If demand is elastic, a small change in price leads to a proportionately larger change in quantity demanded.");

        questions.add("What are the factors that influence demand?");
        answers.add("Factors influencing demand include consumer preferences, income levels, prices of related goods, population, and advertising.");

        questions.add("What are the factors that influence supply?");
        answers.add("Factors influencing supply include input prices, technology, government policies, and natural disasters.");

        questions.add("Explain the concept of substitute goods.");
        answers.add("Substitute goods are products that can be used in place of one another. When the price of one substitute increases, the demand for the other substitute may increase as consumers switch to the cheaper option.");

        questions.add("Define complementary goods.");
        answers.add("Complementary goods are products that are used together. When the price of one complementary good increases, the demand for the other complementary good may decrease.");

        questions.add("How do changes in income affect demand?");
        answers.add("For most goods, as income increases, the demand for normal goods increases. However, for inferior goods, as income increases, the demand may decrease.");

        questions.add("What are examples of essential factors affecting supply in the market?");
        answers.add("Essential factors affecting supply include changes in input prices (e.g., raw materials, labor), advancements in technology, and government regulations.");

        questions.add("Explain the concept of market equilibrium.");
        answers.add("Market equilibrium occurs when the quantity demanded equals the quantity supplied, resulting in a stable market price.");

        questions.add("What happens when there is a price ceiling?");
        answers.add("A price ceiling is a legal maximum price set by the government, typically to protect consumers from high prices. It may lead to shortages and a black market.");

        questions.add("What happens when there is a price floor?");
        answers.add("A price floor is a legal minimum price set by the government, usually to support producers. It may lead to surpluses and inefficiencies in the market.");

        questions.add("Explain the concept of elasticity of supply.");
        answers.add("Elasticity of supply measures how responsive the quantity supplied is to changes in price. If supply is elastic, a small change in price leads to a proportionately larger change in quantity supplied.");

        questions.add("What is a competitive market?");
        answers.add("A competitive market is a market with many buyers and sellers, where no single buyer or seller can influence the market price.");

        questions.add("How do changes in consumer tastes and preferences affect demand?");
        answers.add("When consumer tastes and preferences for a product increase, the demand for that product may increase.");

        questions.add("What is a price elasticity of demand greater than 1?");
        answers.add("A price elasticity of demand greater than 1 (elastic demand) indicates that demand is highly responsive to changes in price.");

        questions.add("What is a price elasticity of demand less than 1?");
        answers.add("A price elasticity of demand less than 1 (inelastic demand) indicates that demand is less responsive to changes in price.");

        questions.add("What is the cross-price elasticity of demand?");
        answers.add("Cross-price elasticity of demand measures how the quantity demanded of one good changes in response to a change in the price of another good.");

        questions.add("How does technological advancement affect supply?");
        answers.add("Technological advancement often increases supply, as it allows producers to produce more efficiently and at lower costs.");

        supplyAndDemand.setQuestions(questions);
        supplyAndDemand.setAnswers(answers);
        supplyAndDemand.setCategory("Economics");
        flashcardList.add(supplyAndDemand);

        //___________________________________________________________________________________________

        Flashcard sgGeneralKnowledge = new Flashcard();
        sgGeneralKnowledge.setTitle("Singapore General Knowledge");
        questions = new ArrayList<>();
        answers = new ArrayList<>();

        questions.add("What is the official name of Singapore?");
        answers.add("Republic of Singapore");

        questions.add("What are the four official languages of Singapore?");
        answers.add("English, Malay, Mandarin Chinese, and Tamil");

        questions.add("What is the capital city of Singapore?");
        answers.add("Singapore");

        questions.add("When did Singapore gain independence?");
        answers.add("August 9, 1965");

        questions.add("What is the national anthem of Singapore?");
        answers.add("Majulah Singapura");

        questions.add("Who is the current Prime Minister of Singapore?");
        answers.add("Lee Hsien Loong");

        questions.add("Which body of water separates Singapore from Malaysia?");
        answers.add("The Johor Strait");

        questions.add("What is the official currency of Singapore?");
        answers.add("Singapore Dollar (SGD)");

        questions.add("What is the iconic hotel and landmark in Singapore that looks like a ship on top of three towers?");
        answers.add("Marina Bay Sands");

        questions.add("Which famous shopping street in Singapore is known for its wide range of retail and dining options?");
        answers.add("Orchard Road");

        questions.add("What is the name of the world's first nighttime zoo, located in Singapore?");
        answers.add("Night Safari");

        questions.add("Which public housing system in Singapore is known for its unique architectural design and community spaces?");
        answers.add("HDB (Housing and Development Board) flats");

        questions.add("What is the name of the island resort in Singapore that offers attractions like Universal Studios and S.E.A. Aquarium?");
        answers.add("Sentosa Island");

        questions.add("What is the famous landmark in Singapore that resembles a large durian fruit?");
        answers.add("Esplanade - Theatres on the Bay");

        questions.add("Which annual event in Singapore is a grand celebration of lights and colors?");
        answers.add("Singapore's National Day Parade");

        questions.add("What is the name of the iconic Merlion statue, symbolizing Singapore's maritime heritage?");
        answers.add("Merlion Park");

        questions.add("Which cultural district in Singapore is known for its vibrant art scene and colorful murals?");
        answers.add("Haji Lane in Kampong Glam");

        questions.add("What is the primary religion practiced in Singapore?");
        answers.add("Buddhism");

        questions.add("Which famous food center in Singapore is renowned for its diverse and delicious hawker food?");
        answers.add("Maxwell Food Centre");

        questions.add("What is the name of the famous Formula 1 race held annually in Singapore?");
        answers.add("Singapore Grand Prix");

        questions.add("What is the tallest observation wheel in Singapore, offering panoramic views of the city?");
        answers.add("Singapore Flyer");

        questions.add("What is the world's largest indoor waterfall, located within Singapore's Jewel Changi Airport?");
        answers.add("Rain Vortex");

        questions.add("Which historical district in Singapore is known for its well-preserved Peranakan heritage?");
        answers.add("Katong and Joo Chiat");

        questions.add("What is the name of the public transit system in Singapore?");
        answers.add("MRT (Mass Rapid Transit)");

        questions.add("What is the iconic building in Singapore's Central Business District with a unique design resembling a durian?");
        answers.add("ION Orchard");

        questions.add("Which government agency in Singapore is responsible for promoting tourism?");
        answers.add("Singapore Tourism Board (STB)");

        questions.add("What is the famous food in Singapore made of stir-fried rice noodles with eggs, prawns, and bean sprouts?");
        answers.add("Char Kway Teow");

        questions.add("What is the name of the famous bird park in Singapore, home to various bird species?");
        answers.add("Jurong Bird Park");

        questions.add("What is the main island of Singapore connected to by the Causeway and the Second Link?");
        answers.add("Johor, Malaysia");

        questions.add("What is the name of the largest observation deck in Singapore, located at Marina Bay Sands?");
        answers.add("SkyPark Observation Deck");

        sgGeneralKnowledge.setQuestions(questions);
        sgGeneralKnowledge.setAnswers(answers);
        sgGeneralKnowledge.setCategory("Social Studies");
        flashcardList.add(sgGeneralKnowledge);

        //___________________________________________________________________________________________

        Flashcard algebra = new Flashcard();
        algebra.setTitle("Algebra");
        questions = new ArrayList<>();
        answers = new ArrayList<>();

        questions.add("Simplify the expression: 3x + 2x");
        answers.add("5x");

        questions.add("Factor the expression: x^2 - 9");
        answers.add("(x - 3)(x + 3)");

        questions.add("Solve for x: 2x - 7 = 15");
        answers.add("x = 11");

        questions.add("Expand the expression: (2a + 3b)^2");
        answers.add("4a^2 + 12ab + 9b^2");

        questions.add("Simplify the equation: 5(2x + 3) - 4(3x - 2) = 12");
        answers.add("x = 3");

        questions.add("Solve the system of equations: 2x + y = 8, x - y = 2");
        answers.add("x = 3, y = 2");

        questions.add("Factor the quadratic expression: x^2 + 5x + 6");
        answers.add("(x + 2)(x + 3)");

        questions.add("Simplify the expression: 3(x + 2) + 2(2x - 1)");
        answers.add("7x + 4");

        questions.add("Solve for x: 2(3x - 1) + 5 = 17");
        answers.add("x = 3");

        questions.add("Expand and simplify the expression: (3a - 4b)^2");
        answers.add("9a^2 - 24ab + 16b^2");

        questions.add("Solve the equation: 4x + 7 = 3x - 2");
        answers.add("x = -9");

        questions.add("Factor the trinomial: x^2 + 7x + 10");
        answers.add("(x + 5)(x + 2)");

        questions.add("Simplify the expression: 2(4x - 3) + 3(x + 2)");
        answers.add("11x + 1");

        questions.add("Solve for x: 3(x - 2) = 9 - 2x");
        answers.add("x = 3");

        questions.add("Factor the quadratic expression: 2x^2 - 8x");
        answers.add("2x(x - 4)");

        questions.add("Simplify the expression: 5(x + 3) - 2(2x - 5)");
        answers.add("x + 23");

        questions.add("Solve the system of equations: \n3x + y = 10\n2x - y = 4");
        answers.add("x = 2, y = 4");

        questions.add("Factor the trinomial: x^2 - 6x + 9");
        answers.add("(x - 3)^2");

        questions.add("Simplify the expression: 4(2x - 3) - 2(x + 5)");
        answers.add("6x - 22");

        questions.add("Solve for x: 3(2x + 1) = 15");
        answers.add("x = 2");

        questions.add("Factor the quadratic expression: 3x^2 + 12x + 9");
        answers.add("3(x + 3)^2");

        questions.add("Simplify the expression: 2(3x - 4) - 3(x - 1)");
        answers.add("x - 5");

        questions.add("Solve the equation: 5x - 7 = 8 - 2x");
        answers.add("x = 1");

        questions.add("Factor the trinomial: x^2 + 10x + 21");
        answers.add("(x + 7)(x + 3)");

        questions.add("Simplify the expression: 3(4x + 5) - 2(2x - 1)");
        answers.add("10x + 13");

        algebra.setQuestions(questions);
        algebra.setAnswers(answers);
        algebra.setCategory("Math");
        flashcardList.add(algebra);

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
                // Start FlashCardQuestionPage activity with the selected flashcard
                Intent intent = new Intent(MainActivity.this, Testyourself.class);
                intent.putExtra("flashcard", flashcard);
                intent.putExtra("Username", username);
                startActivityForResult(intent, 1);
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
                // Start DifficultyLevelActivity to choose the difficulty level
                Log.d("FlashcardDebug", "Sending flashcard to Testyourself: " + flashcard.getTitle());
                Intent intent = new Intent(MainActivity.this, Testyourself.class);
                intent.putExtra("flashcard", flashcard);
                intent.putExtra("username",username);
                startActivityForResult(intent, 1);
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


