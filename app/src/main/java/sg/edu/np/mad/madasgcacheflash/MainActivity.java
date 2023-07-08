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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        quoteTextView = findViewById(R.id.textView11);

        // Set a click listener on the text view to fetch a new quote
        // Fetch and display the quote from Firebase Firestore
        fetchQuoteFromFirestore();

        // Set up click listener for the quote text view
        quoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch a new quote from the API and update in Firebase Firestore
                fetchNewQuoteFromAPI();
            }
        });

        // Create the flashcards
        createFlashcards();
        postFlashCards(flashcardList);

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
                Log.v("Username out:",username);
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
                // Start FlashCardQuestionPage activity with the selected flashcard
                Intent intent = new Intent(MainActivity.this, Testyourself.class);
                intent.putExtra("flashcard", flashcard);
                intent.putExtra("username",username);
                startActivity(intent);
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
                    } else if (id == R.id.home) {
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

    private void postFlashCards(List<Flashcard> flashcards) {
        // Delete existing flashcards in Firebase Realtime Database
        DatabaseReference flashcardsRef = FirebaseDatabase.getInstance().getReference("users").child("sam").child("flashcards");
        flashcardsRef.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Upload new flashcards to Firebase Realtime Database
                        uploadNewFlashcards(flashcards);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("QuoteApp", "Error deleting existing flashcards", e);
                    }
                });
    }

    private void uploadNewFlashcards(List<Flashcard> flashcards) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference flashcardsRef = usersRef.child(username).child("flashcards");

        for (Flashcard flashcard : flashcards) {
            String flashcardName = flashcard.getTitle();

            DatabaseReference flashcardRef = flashcardsRef.child(flashcardName); // Use the flashcard name as the key

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
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
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
        flashcardList.add(france);

        Flashcard math = new Flashcard();
        math.setTitle("Math");
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
        flashcardList.add(math);

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
        flashcardList.add(socialStudies);

        Flashcard economics = new Flashcard();
        economics.setTitle("Economics");
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
        flashcardList.add(economics);
    }
}


