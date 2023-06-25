package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    String title = "Main Activity 2"; //title

    @Override //OnCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //Set to nightmode
        setContentView(R.layout.activity_signup);
        Intent intent = getIntent();
    }

    MyDBHandler myDBHandler = new MyDBHandler(this);
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(title,"Starting Acct Creation");

        EditText etUsername = findViewById(R.id.editTextText3);
        EditText etPassword = findViewById(R.id.editTextText4);
        Button createButton = findViewById(R.id.button3);
        Button cancelButton = findViewById(R.id.button2);

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                User dbData = myDBHandler.findUser(etUsername.getText().toString());
                if (!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) //if input not blank
                {
                    if (dbData != null && dbData.getUsername().equals(etUsername.getText().toString())) { //if can find in db
                        Toast.makeText(Signup.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                    }
                    else { //if cannot, create
                        String dbUserName = etUsername.getText().toString();
                        String dbPassword = etPassword.getText().toString();
                        User dbUserData = new User(dbUserName, dbPassword);

                        myDBHandler.addUser(dbUserData);
                        Toast.makeText(Signup.this, "Username created!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signup.this, Login.class);
                        startActivity(intent);
                    }
                }
                else //if input blank
                {
                    Toast.makeText(Signup.this, "Please fill in both!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent); //next activity
            }
        });

    }
}