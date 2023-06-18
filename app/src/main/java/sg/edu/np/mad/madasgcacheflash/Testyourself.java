package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Testyourself extends AppCompatActivity {

    final String TITLE = "Testyourself";
    int currentIndex = 0;
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testyourself);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("flashcard")) {
            Flashcard flashcard = intent.getParcelableExtra("flashcard");

            // Retrieve the questions from the flashcard object
            questions = flashcard.getQuestions();
            answers = flashcard.getAnswers();

            TextView Title = findViewById(R.id.ftitle);
            TextView qcard = findViewById(R.id.QCard);
            TextView acard = findViewById(R.id.ACard);
            Title.setText(flashcard.getTitle());
            qcard.setText(questions.get(currentIndex));
            acard.setText(answers.get(currentIndex));
            acard.setVisibility(View.GONE);
            Button b1 = findViewById(R.id.button3);
            Button b2 = findViewById(R.id.button);
            Button b3 = findViewById(R.id.button2);
            Button b4 = findViewById(R.id.button4);

            qcard.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v == qcard) {
                        acard.setText(answers.get(currentIndex));
                        acard.setVisibility(View.VISIBLE);
                        qcard.setVisibility(View.GONE);

                    }



                }


            });

            b4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    EditText input = findViewById(R.id.Userinput);
                    String answer=input.getText().toString();
                    String s=answers.get(currentIndex);
                    acard.setText(answers.get(currentIndex));


                    if (answer.equals(s)) {
                        acard.setText(answers.get(currentIndex));
                        acard.setVisibility(View.VISIBLE);
                        qcard.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), answer+" is Correct", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        acard.setVisibility(View.VISIBLE);
                        qcard.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), answer+" is Incorrect. Correct Answer is "+s , Toast.LENGTH_SHORT).show();
                        Log.v(TITLE, "Incorrect");
                    }







                }
            });









            acard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == acard) {
                        acard.setText(answers.get(currentIndex));
                        qcard.setVisibility(View.VISIBLE);
                        acard.setVisibility(View.GONE);


                    }


                }
            });

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Testyourself.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TITLE, String.valueOf(currentIndex));
                    currentIndex--;
                    if (currentIndex <= 0) {
                        currentIndex = questions.size();
                    }
                    qcard.setText(questions.get(currentIndex));
                }
            });
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex++;
                    if (currentIndex >= questions.size()) {
                        currentIndex = 0;
                    }
                    qcard.setText(questions.get(currentIndex));
                }
            });

            Log.v(TITLE, "On Create!");
        }
    }

    @Override
    protected void onStart () {
        super.onStart();
        Log.v(TITLE, "On Start!");
    }


    @Override
    protected void onResume () {
        super.onResume();
        Log.v(TITLE, "On Resume!");


    }

    @Override
    protected void onStop () {
        super.onStop();
        Log.v(TITLE, "On Stop");
    }

    @Override
    protected void onPause () {
        super.onPause();
        Log.v(TITLE, "On pause");
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        Log.v(TITLE, "On Destroy");
    }

}