package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {

    String title = "Main activity";
    private FirebaseAuth mAuth;
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
        setContentView(R.layout.activity_login);

        TextView privacyPolicyTextView = findViewById(R.id.privacy_policy_text);
        privacyPolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://westwq.github.io/MADPrivacy/"));
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(title, "Starting App Login Page");
        TextView newUser = findViewById(R.id.textView4);
        mAuth = FirebaseAuth.getInstance();
        EditText etUsername = findViewById(R.id.editTextText);
        EditText etPassword = findViewById(R.id.editTextText2);
        newUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Login.this, Signup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return false;
            }
        });

        Button loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(username.isEmpty() && password.isEmpty()){
                    Toast.makeText(Login.this, "Please enter username and password,do not leave it blank", Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter password,do not leave it blank", Toast.LENGTH_SHORT).show();
                }
                else if(username.isEmpty()){
                    Toast.makeText(Login.this, "Please enter username,do not leave it blank", Toast.LENGTH_SHORT).show();
                }
                else {
                    signIn(username,password);
                }
            }
        });
    }

    private void signIn(String username, String password) {
        mAuth.signInWithEmailAndPassword(username + "@gmail.com", password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        String standardizedUsername = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
                        Log.d("Login", "Username: " + username);
                        Toast.makeText(Login.this, "Login successful! Welcome " + standardizedUsername, Toast.LENGTH_SHORT).show();
                        // Start the MainActivity
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("Username",standardizedUsername);
                        startActivity(intent);
                    } else {
                        // Login failed
                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            // Invalid username
                            Toast.makeText(Login.this, "Invalid username", Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid password
                            Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other login error
                            Toast.makeText(Login.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}