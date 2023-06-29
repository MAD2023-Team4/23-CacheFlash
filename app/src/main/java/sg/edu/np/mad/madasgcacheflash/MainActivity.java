package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String title = "Main Activity";
    List<Flashcard> flashcardList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    private String username;
    private static final String QUOTE_API_URL = "https://api.example.com/quote";
    private static final String SHARED_PREFS_NAME = "MyPrefs";
    private static final String KEY_QUOTE = "quote";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_LAST_UPDATED = "last_updated";

    private TextView quoteTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        MyDBHandler dbHandler = new MyDBHandler(this);
        username = dbHandler.findUsername(username);
        if (username != null) {
            TextView welcomeTxt = findViewById(R.id.welcome);
            // Create a StringBuilder object
            StringBuilder builder = new StringBuilder();

            // Append the welcome message
            builder.append(getString(R.string.welcome_message, username));

            // Append an exclamation mark
            builder.append("!");

            // Set the text of the welcome text view
            welcomeTxt.setText(builder);
        }
        else{
            Log.i(title, "The username is null, so it is a problem");
        }

        quoteTextView = findViewById(R.id.textView11);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);

        // Check if the quote needs to be updated
        String lastUpdated = sharedPreferences.getString(KEY_LAST_UPDATED, "");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (!currentDate.equals(lastUpdated)) {
            // Fetch a new quote from the API
            fetchNewQuoteFromAPI();
        } else {
            // Display the stored quote
            displayQuote();
        }

        // Set a click listener on the text view to fetch a new quote
        quoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchNewQuoteFromAPI();
            }
        });
            // Create the flashcards
        createFlashcards();

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
                startActivity(intent);
            }
        });


        //Call API
        //__________________________________________________________________________________________



        //Bottom Navigation View
        //Source from: https://www.youtube.com/watch?v=lOTIedfP1OA
        //__________________________________________________________________________________________
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item != null) {
                    int id = item.getItemId();

                    if (id == R.id.dashboard) {
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("Username", dbHandler.findUsername(username)); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    else if (id == R.id.home) {
                        return true;
                    }
                    else if (id == R.id.about){
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        intent.putExtra("Username", dbHandler.findUsername(username)); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }

                }
                return false;
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

                                // Store the new quote and author
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_QUOTE, quote);
                                editor.putString(KEY_AUTHOR, author);
                                editor.putString(KEY_LAST_UPDATED, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                                editor.apply();

                                return quote + "|" + author;
                            } else {
                                Log.d("QuoteApp", "Invalid quote or author field in API response.");
                            }
                        } else {
                            Log.d("QuoteApp", "Empty or invalid API response.");
                        }
                    } else {
                        Log.d("QuoteApp", "API request failed with response code: " + connection.getResponseCode());
                    }

                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    String[] parts = result.split("\\|");
                    String quote = parts[0];
                    String author = parts[1];

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String formattedQuote = String.format(Locale.getDefault(), "%s\n- %s", quote, author);
                            quoteTextView.setText(formattedQuote);
                            Log.d("QuoteApp", "Received Quote: " + quote);
                            Log.d("QuoteApp", "Author: " + author);
                        }
                    });
                }
            }
        }.execute();
    }

    private void displayQuote() {
        String quote = sharedPreferences.getString(KEY_QUOTE, "");
        String author = sharedPreferences.getString(KEY_AUTHOR, "");

        if (!quote.isEmpty() && !author.isEmpty()) {
            String formattedQuote = String.format(Locale.getDefault(), "%s\n- %s", quote, author);
            quoteTextView.setText(formattedQuote);
            Log.d("QuoteApp", "Displaying Quote: " + quote);
            Log.d("QuoteApp", "Author: " + author);
        } else {
            // If the stored quote is empty, fetch a new quote from the API
            fetchNewQuoteFromAPI();
        }
    }

    private void startShuffleCardActivity(Flashcard flashcard) {
        Intent shuffleCardIntent = new Intent(this, ShuffleCardActivity.class);
        shuffleCardIntent.putExtra("flashcard", flashcard);
        startActivity(shuffleCardIntent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        if (username != null) {
            TextView welcomeTxt = findViewById(R.id.welcome);
            // Create a StringBuilder object
            StringBuilder builder = new StringBuilder();

            // Append the welcome message
            builder.append(getString(R.string.welcome_message, username));

            // Append an exclamation mark
            //builder.append("!");

            // Set the text of the welcome text view
            welcomeTxt.setText(builder);
        }
        else{
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


