package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

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
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    //TextView cred = findViewById(R.id.welcome);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiating all default flashcard objects
        //__________________________________________________________________________________________
        // Create a new Flashcard object
        Flashcard flashcard = new Flashcard();

        // Set the title of the Flashcard
        flashcard.setTitle("What is the capital of France?");

        // Set the questions for the Flashcard
        questions.add("What is the capital of France?");
        questions.add("What is the largest city in France?");
        questions.add("What is the national language of France?");
        flashcard.setQuestions(questions);

        // Set the answers for the Flashcard
        answers.add("Paris");
        answers.add("Lyon");
        answers.add("French");
        flashcard.setAnswers(answers);

        // Save the Flashcard in the database
        MyDBHandler databaseHelper = new MyDBHandler(this, "IT Basics");
        databaseHelper.saveFlashcard(flashcard);



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
}