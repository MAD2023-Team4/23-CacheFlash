package sg.edu.np.mad.madasgcacheflash;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import com.google.protobuf.NullValue;

public class Signup extends AppCompatActivity {

    String title = "Main Activity 2"; //title
    private FirebaseAuth mAuth;
    private TextInputEditText etPassword;
    private boolean isPasswordVisible;

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
        etPassword = findViewById(R.id.editTextText4);
        EditText etemail=findViewById(R.id.editTextText5);
        ImageView hideshowpassword=findViewById(R.id.imageView3);

        Button createButton = findViewById(R.id.button3);
        Button cancelButton = findViewById(R.id.button2);

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etemail.getText().toString().trim();

                if(username.isEmpty() && password.isEmpty() && email.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter username and password,do not leave it blank", Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()) {
                    Toast.makeText(Signup.this, "Please enter password,do not leave it blank", Toast.LENGTH_SHORT).show();
                }
                else if(username.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter username,do not leave it blank", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(Signup.this, "Please enter email,do not leave it blank", Toast.LENGTH_SHORT).show();
                    
                    
                } else {
                    // Check if the username contains only letters and numbers
                    if (!username.matches("^[a-zA-Z0-9]+$")) {
                        Toast.makeText(Signup.this, "Username can only contain letters and numbers", Toast.LENGTH_SHORT).show();
                    }
                    else if (password.length() < 6) {
                        Toast.makeText(Signup.this, "Length of password must be at least 6", Toast.LENGTH_SHORT).show();
                    }
                        
                    else {
                        signup(email, password,username);


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
        hideshowpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPasswordVisible)
                {
                    togglePassVisability();
                    hideshowpassword.setImageResource(R.drawable.hidepassword);
                    isPasswordVisible=false;
                }
                else {
                    togglePassVisability();
                    hideshowpassword.setImageResource(R.drawable.showpasswordicon);
                    isPasswordVisible=true;
                }
                }
            }
        );

    }
    private void signup(String email, String password,String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Signup.this, "User registered!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signup.this,Login.class);
                        intent.putExtra("username",username);
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


    private void togglePassVisability() {
        if (isPasswordVisible) {
            String pass = etPassword.getText().toString();
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setText(pass);
            etPassword.setSelection(pass.length());
        } else {
            String pass = etPassword.getText().toString();
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            etPassword.setText(pass);
            etPassword.setSelection(pass.length());
        }

    }



    }





