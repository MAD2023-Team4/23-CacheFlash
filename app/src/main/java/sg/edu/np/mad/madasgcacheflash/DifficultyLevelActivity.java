package sg.edu.np.mad.madasgcacheflash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultyLevelActivity extends AppCompatActivity {

    private int difficultyLevel;
    private int timerDuration;
    private String username;

    private Flashcard flashcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_level);

        Intent intent = getIntent();
        flashcard = intent.getParcelableExtra("flashcard");
        username = intent.getStringExtra("username");
        Log.d("difficulty name",username);

        Button btnEasy = findViewById(R.id.btnEasy);
        Button btnMedium = findViewById(R.id.btnMedium);
        Button btnHard = findViewById(R.id.btnHard);


        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyLevel = 0; // Easy
                timerDuration = -1; // Unlimited time
                startTest();
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyLevel = 1; // Medium
                timerDuration = 30; // 30 seconds per question
                startTest();
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyLevel = 2; // Hard
                timerDuration = 15; // 15 seconds per question
                startTest();
            }
        });
    }

    private void startTest() {
        // Create a result Intent to pass back data to the calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("difficultyLevel", difficultyLevel);
        resultIntent.putExtra("timerDuration", timerDuration);
        resultIntent.putExtra("flashcard",flashcard);
        resultIntent.putExtra("username",username);

        // Set the result to RESULT_OK to indicate success
        setResult(RESULT_OK, resultIntent);

        // Finish the activity to return to the calling activity (Testyourself.java)
        finish();
    }
}
