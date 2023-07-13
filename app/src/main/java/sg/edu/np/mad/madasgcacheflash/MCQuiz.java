package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MCQuiz extends AppCompatActivity {
    final String TITLE = "Testyourself";
    int currentIndex = 0;
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();
    List<String> answerscheck = new ArrayList<>();
    List<String> optionsList = new ArrayList<>();
    List<Integer> answeredQuestions = new ArrayList<>(); // New list to track answered questions
    boolean isAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_mcquiz);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("flashcard")) {
            Flashcard flashcard = intent.getParcelableExtra("flashcard");

            // Retrieve the questions from the flashcard object
            questions = flashcard.getQuestions();
            answers = flashcard.getAnswers();
            answerscheck.addAll(answers);

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
                    if (currentIndex >= questions.size()) {
                        currentIndex = questions.size() - 1;
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



}