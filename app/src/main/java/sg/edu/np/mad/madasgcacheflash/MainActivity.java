package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    String title = "Main Activity";
    List<Flashcard> flashcardList = new ArrayList<>();
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();
    List<String> currentQuestions = new ArrayList<>();
    List<String> currentAnswers = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    //TextView cred = findViewById(R.id.welcome);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView welcomeTxt = findViewById(R.id.welcome);
        //Instantiating all default flashcard objects
        //__________________________________________________________________________________________
        // Create a new Flashcard object
        Flashcard france = new Flashcard();

        // Set the title of the Flashcard
        france.setTitle("France");

        // Set the questions for the Flashcard
        questions.add("What is the capital of France?");
        questions.add("What is the largest city in France?");
        questions.add("What is the national language of France?");
        france.setQuestions(questions);

        // Set the answers for the Flashcard
        answers.add("Paris");
        answers.add("Lyon");
        answers.add("French");
        france.setAnswers(answers);
        questions.clear();
        answers.clear();

        Flashcard math = new Flashcard();

        // Set the title of the Flashcard
        math.setTitle("Math");

        // Set the questions for the Flashcard
        questions.add("What is the product rule of logarithm?");
        questions.add("What is the quotient rule of logarithm?");
        questions.add("What is the notation for differentiation?");
        math.setQuestions(questions);

        // Set the answers for the Flashcard
        answers.add("logb(xy) = logb x + logb y");
        answers.add("loga(x/y)  = loga x – loga y");
        answers.add("dy/dx or f'(x)");
        math.setAnswers(answers);
        questions.clear();
        answers.clear();

        Flashcard socialStudies = new Flashcard();

        // Set the title of the Flashcard
        socialStudies.setTitle("Social Studies");

        // Set the questions for the Flashcard
        questions.add("When did Singapore gain independence?");
        questions.add("Who won the gold medal in Rio 2016?");
        questions.add("Who is the president of Singapore in 2020?");
        socialStudies.setQuestions(questions);

        // Set the answers for the Flashcard
        answers.add("1965");
        answers.add("Joseph Schooling");
        answers.add("Mdm Halimah Yacob");
        socialStudies.setAnswers(answers);

        flashcardList.add(france);
        flashcardList.add(socialStudies);
        flashcardList.add(math);
        //https://www.planetware.com/pictures/france-f.htm

        /*
        MyDBHandler myDBHandler = new MyDBHandler(this);
        SQLiteDatabase db = myDBHandler.getWritableDatabase();
        // Perform database operations using the 'db' object
        // Save the Flashcard in the database
        myDBHandler.saveFlashcards(flashcardList);
        // Get a writable database instance
        db.close();
         */
        // Creating user object, passed from the Login page

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            // Retrieve the username and password from the intent extras
            String username = intent.getStringExtra("Username");
            String password = intent.getStringExtra("Password");
            welcomeTxt.setText("Welcome, " + username + "!");
        }

        /*
            catch (Exception e){
                Toast.makeText (getApplicationContext (),
                        "Whoops, please re-login again.", Toast.LENGTH_SHORT).show();
                finish(); // Close the current activity

                // Start the login activity again
                Intent loginIntent = new Intent(MainActivity.this, Login.class);
                startActivity(loginIntent);
                }
                */


        //Unpacking flashcards from Questions list and Answers list
        //__________________________________________________________________________________________
        for (Flashcard flashcard : flashcardList) {
            // Access the current flashcard
            // You can perform operations or access its properties here
            String title = flashcard.getTitle();

            // ... Perform further operations with the flashcard

            RecyclerView recyclerView = findViewById(R.id.recyclerView1);
            FlashcardAdapter fcAdapter = new FlashcardAdapter(flashcardList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
            int spacingInPixels = 8;
            recyclerView.addItemDecoration(new SpaceItemDeco(spacingInPixels));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fcAdapter);
        }
        for (Flashcard flashcard : flashcardList) {
            // Access the current flashcard
            // You can perform operations or access its properties here
            String title = flashcard.getTitle();

            // ... Perform further operations with the flashcard

            RecyclerView recyclerView = findViewById(R.id.recyclerView2);
            FlashcardAdapter fcAdapter = new FlashcardAdapter(flashcardList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
            int spacingInPixels = 8;
            recyclerView.addItemDecoration(new SpaceItemDeco(spacingInPixels));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(fcAdapter);
        }



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
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    else if (id == R.id.home) {
                        return true;
                    }
                    else if (id == R.id.about){
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        });


    }
    MyDBHandler myDBHandler = new MyDBHandler(this);
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(title, "Starting Flashcards DB Creation");

    }
    public void onCreate(SQLiteDatabase db) {
        // Create the SQLite database if it does not exist.
        db.execSQL("CREATE TABLE IF NOT EXISTS flashcards (title TEXT, question TEXT, answer TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
