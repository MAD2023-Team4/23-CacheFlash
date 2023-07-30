package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

                //------------------------------------------------------------------------------------------
                //A checker to check if there are users with the same username. If left unchecked, it can
                //be quite dangerous to have two users with the same username. Then, both are updated
                //simultaneously.
                //------------------------------------------------------------------------------------------
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isUsernameTaken = false;
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String usernameTmp = userSnapshot.getKey(); // Assuming the key is the username

                            if (usernameTmp.equals(username)) {
                                isUsernameTaken = true;
                                break;
                            }
                        }

                        if (isUsernameTaken) {
                            Toast.makeText(Signup.this, "Username is already taken. Please choose another one.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Continue with the user registration process, as the username is not taken.
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle the error, if needed.
                        Toast.makeText(Signup.this, "Failed to retrieve data from the database.", Toast.LENGTH_SHORT).show();
                    }
                });

                //------------------------------------------------------------------------------------------
                //Here, various checkers (try-catch) statements are made, to ensure username
                //at least fills in username, password, and email.
                //------------------------------------------------------------------------------------------
                if(username.isEmpty() && password.isEmpty() && email.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()) {
                    Toast.makeText(Signup.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }
                else if(username.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(Signup.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    
                }
                else {
                    // Check if the username contains only letters and numbers
                    if (!username.matches("^[a-zA-Z0-9]+$")) {
                        Toast.makeText(Signup.this, "Username can only contain letters and numbers", Toast.LENGTH_SHORT).show();
                    }

                    else if (password.length() < 6) {
                        Toast.makeText(Signup.this, "Length of password must be at least 6", Toast.LENGTH_SHORT).show();
                    }


                    else {
                        //------------------------------------------------------------------------------------------
                        //Email checker if there is an @, then if the username matches the email before the @.
                        //If statement calls, no matter what. If this is else if, it may or may not happen.
                        //If everything is in favour, sign up the user.
                        //------------------------------------------------------------------------------------------
                        if (email.contains("@")){
                            String[] username1 = email.split("@");
                            String usernameTmp = username1[0]; //we store it in a temp variable for comparison
                            //username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
                            //Now, John's feature requires a checker to then if the username matches the email before the @
                            if (usernameTmp.equals(username)){
                                signup(email, password,username);
                            }
                            else {
                                Toast.makeText(Signup.this, "Username must be same as the email before the @ symbol",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {Toast.makeText(Signup.this, "Please enter a valid email address",
                                Toast.LENGTH_SHORT).show();
                        }
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

        //------------------------------------------------------------------------------------------
        //Hide password icon can toggle between visible and not visible. Calls another  method,
        //togglingvisibility function.
        //------------------------------------------------------------------------------------------
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
                        intent.putExtra("Username",username);
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

    //------------------------------------------------------------------------------------------
    //togglingvisibility function.
    //------------------------------------------------------------------------------------------
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





