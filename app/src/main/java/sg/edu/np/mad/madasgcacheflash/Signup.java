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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Signup extends AppCompatActivity {

    String title = "Main Activity 2"; //title
    private FirebaseAuth mAuth;

    @Override //OnCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //Set to nightmode
        setContentView(R.layout.activity_signup);
        Intent intent = getIntent();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(title,"Starting Acct Creation");
        mAuth = FirebaseAuth.getInstance();
        EditText etUsername = findViewById(R.id.editTextText3);
        EditText etPassword = findViewById(R.id.editTextText4);
        Button createButton = findViewById(R.id.button3);
        Button cancelButton = findViewById(R.id.button2);

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if(username.isEmpty() && password.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter username and password,do not leave it blank", Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()) {
                    Toast.makeText(Signup.this, "Please enter password,do not leave it blank", Toast.LENGTH_SHORT).show();
                }
                else if(username.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter username,do not leave it blank", Toast.LENGTH_SHORT).show();
                }

                else {
                    if(password.length() < 6){
                        Toast.makeText(Signup.this,"Length of password must be at least 6",Toast.LENGTH_SHORT).show();
                    }
                    else {
                    signup(username,password);
                    }
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
    private void signup(String username, String password) {
        mAuth.createUserWithEmailAndPassword(username + "@gmail.com", password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Signup.this, "User registered!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signup.this,Login.class);
                        startActivity(intent);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // User already exists, display toast message
                            Toast.makeText(Signup.this, "User already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Signup.this, "Failed to register user!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}