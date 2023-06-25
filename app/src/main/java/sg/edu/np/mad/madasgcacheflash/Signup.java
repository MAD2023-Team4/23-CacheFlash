package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    String title = "Main Activity 2";
    /*
    private String GLOBAL_PREF = "MyPrefs";
    private String MY_USERNAME = "MyUserName";
    private String MY_PASSWORD = "MyPassword";
    SharedPreferences sharedPreferences;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
                /*
                sharedPreferences = getSharedPreferences(GLOBAL_PREF,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MY_USERNAME, etUsername.getText().toString());
                editor.putString(MY_PASSWORD, etPassword.getText().toString());
                editor.commit();

                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
                */

                User dbData = myDBHandler.findUser(etUsername.getText().toString());
                if (dbData.getUsername().equals(etUsername.getText().toString())) {
                    Toast.makeText(Signup.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                }
                else if(dbData != null)
                {Toast.makeText(Signup.this, "Username already exists!", Toast.LENGTH_SHORT).show();}
                else {
                    String dbUserName = etUsername.getText().toString();
                    String dbPassword = etPassword.getText().toString();
                    User dbUserData = new User(dbUserName, dbPassword);

                    myDBHandler.addUser(dbUserData);
                    Toast.makeText(Signup.this, "Username created!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signup.this, Login.class);
                    startActivity(intent);
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
    }
}