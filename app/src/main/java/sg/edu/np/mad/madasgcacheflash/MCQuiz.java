package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MCQuiz extends AppCompatActivity {
    final String TITLE = "TestyourselfMCQ";
    int currentIndex = 0;
    int score=0;
    String username;
    Flashcard flashcard;
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();
    List<String> answerscheck = new ArrayList<>();
    List<String> optionsList = new ArrayList<>();
    private MotionLayout motionLayout;

    boolean isAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_mcquiz);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("flashcard")) {
            Flashcard flashcard = intent.getParcelableExtra("flashcard");
            username = intent.getStringExtra("Username");


            // Retrieve the questions from the flashcard object
            questions = flashcard.getQuestions();
            answers = flashcard.getAnswers();

            List<String> combinedList = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                combinedList.add(questions.get(i) + "|" + answers.get(i));
            }
            Collections.shuffle(combinedList);

            // Clear the original questions and answers lists
            questions.clear();
            answers.clear();

            // Separate the shuffled combined list into questions and answers lists
            for (String item : combinedList) {
                String[] parts = item.split("\\|");
                questions.add(parts[0]);
                answers.add(parts[1]);
            }
            answerscheck.addAll(answers);
            motionLayout = findViewById(R.id.motionLayout);

            motionLayout.transitionToEnd();


            Log.v(TITLE, "hi" + answerscheck.size());
            TextView QuestionView = findViewById(R.id.QuestionViews);
            TextView Title = findViewById(R.id.FlashcardTitle);
            Button option1 = findViewById(R.id.option1);
            Button option2 = findViewById(R.id.option2);
            Button option3 = findViewById(R.id.option3);
            Button option4 = findViewById(R.id.option4);
            Button next = findViewById(R.id.button9);
            Title.setText(flashcard.getTitle());
            changequestion(QuestionView,next,option1,option2,option3,option4,answers,questions,answerscheck,optionsList);
//            QuestionView.setText(questions.get(currentIndex));
//            next.setEnabled(false);
//            Random random = new Random();
//
//            String coption = answers.get(currentIndex);
//            answers.remove(currentIndex);
//            optionsList.add(coption);
//
//
//            for (int i = 0; i < 3; i++) {
//                int options = random.nextInt(answers.size());
//                String option = answers.get(options);
//                answers.remove(option);
//                optionsList.add(option);
//
//            }
//
//            Log.v(TITLE, "Size: " + answers.size());
//            Collections.shuffle(optionsList);
//            for (int i = 0; i < optionsList.size(); i++) {
//                String buttonText = optionsList.get(i);
//                if (i == 0) {
//                    option1.setText(buttonText);
//                } else if (i == 1) {
//                    option2.setText(buttonText);
//                } else if (i == 2) {
//                    option3.setText(buttonText);
//                } else if (i == 3) {
//                    option4.setText(buttonText);
//                }
//            }
            option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getanswer(option1, option2, option3, option4, answerscheck, currentIndex, next);
                }

            });
            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getanswer(option2, option3, option4, option1, answerscheck, currentIndex, next);

                }
            });
            option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getanswer(option3, option4, option1, option2, answerscheck, currentIndex, next);

                }
            });
            option4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getanswer(option4, option1, option2, option3, answerscheck, currentIndex, next);

                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex++;
                    if(currentIndex == flashcard.getQuestions().size()){
                        //make a toast message of the score
                        showAlert("Quiz Finished", "Your score: " + score, score, flashcard.getQuestions().size());
                        double percentage = score/flashcard.getQuestions().size();

                        //Update the flashcard's score locally
                        flashcard.setPercentage(percentage);
                        // Posting performance of the user into firebase
                        postPerformance(flashcard, score);
                    }
                    if (currentIndex >= questions.size()) {
                        currentIndex = questions.size() - 1;
                        //score
                    }
                    QuestionView.setText(questions.get(currentIndex));
                    changequestion(QuestionView,next,option1,option2,option3,option4,answers,questions,answerscheck,optionsList);


                    if (currentIndex == questions.size() - 1) {
                        next.setEnabled(false); // Disable the next button at the last question
                    }
                }
            });
        }
    }
    public void changequestion(TextView Qcard,Button next,Button option1,Button option2,Button option3,Button option4,List<String>AList,List<String>QList,List<String>SList,List<String>OList)
    {
        Qcard.setText(QList.get(currentIndex));
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
        option1.setBackgroundColor(Color.BLUE);
        option2.setBackgroundColor(Color.BLUE);
        option3.setBackgroundColor(Color.BLUE);
        option4.setBackgroundColor(Color.BLUE);
        Qcard.setText(QList.get(currentIndex));
        next.setEnabled(false);
        Random random = new Random();

        String coption = AList.get(currentIndex);
        OList.add(coption);
        AList.remove(coption);



        for (int i = 0; i < 3; i++) {
            int options = random.nextInt(AList.size());
            String option = AList.get(options);
            OList.add(option);
            AList.remove(option);


        }


        Log.v(TITLE, "Size: " + answers.size());
        Collections.shuffle(OList);
        for (int i = 0; i < optionsList.size(); i++) {
            String buttonText = optionsList.get(i);
            if (i == 0) {
                option1.setText(buttonText);
            } else if (i == 1) {
                option2.setText(buttonText);
            } else if (i == 2) {
                option3.setText(buttonText);
            } else if (i == 3) {
                option4.setText(buttonText);
            }
        }
        AList.clear();
        AList.addAll(SList);
        OList.clear();

        Log.v(TITLE, "Size: " + answers.size());
    }








    public boolean CheckAnswer(String correctAnswer, String guessedAnswer) {
        if (correctAnswer.equals(guessedAnswer)) {
            return true;
        }
        return false;
    }
    public void getanswer(Button b,Button b2,Button b3,Button b4, List<String> AL,int i,Button next)
    {

        if(CheckAnswer(AL.get(i),b.getText().toString()))
        {
            Log.v(TITLE,"here");
            b.setBackgroundColor(Color.GREEN);
            score+=1;

        }
        else{
            Log.v(TITLE,"her");
            b.setBackgroundColor(Color.RED);
            if(CheckAnswer(AL.get(i),b2.getText().toString()))
            {
                b2.setBackgroundColor(Color.GREEN);
            }
            else if(CheckAnswer(AL.get(i),b3.getText().toString()))
            {
                b3.setBackgroundColor(Color.GREEN);
            }
            else if(CheckAnswer(AL.get(i),b4.getText().toString()))
            {
                b4.setBackgroundColor(Color.GREEN);
            }
        }
        b.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false);
        next.setEnabled(true);

    }
    private void showAlert(String title, String text, int score, int total) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton("Dashboard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the "OK" button is clicked
                        Intent intent = new Intent(MCQuiz.this, Dashboard.class);
                        intent.putExtra("flashcards", flashcard);
                        intent.putExtra("Score", score);
                        intent.putExtra("Total", total);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the "Cancel" button is clicked
                    }
                })
                .show();
    }

    private void postPerformance(Flashcard flashcard, int score) {
        DatabaseReference flashcardsRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(username).child("flashcards");

        Query query = flashcardsRef.orderByChild("title").equalTo(flashcard.getTitle());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String flashcardKey = snapshot.getKey();
                        DatabaseReference flashcardRef = flashcardsRef.child(flashcardKey);

                        flashcardRef.child("score").setValue(score);
                        flashcardRef.child("percentage").setValue(calculatePercentage(score, flashcard.getQuestions().size()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QuoteApp", "Error searching for flashcard scores", databaseError.toException());
            }
        });
    }

    private float calculatePercentage(int score, int totalQuestions) {
        return ((float) score / totalQuestions) * 100;
    }



}