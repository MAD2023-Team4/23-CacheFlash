package sg.edu.np.mad.madasgcacheflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard extends AppCompatActivity {
    private String username;
    private RecyclerView recyclerView;

    public interface DataCallback {
        void onDataReceived(ArrayList<User> updatedUList);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Intent intent = getIntent();

        username = intent.getStringExtra("Username"); //get username

        ArrayList<User> userList = new ArrayList<>();
        ArrayList<User> updatedUList = queryAllUsersAndPoints(userList,new DataCallback(){
            @Override
            public void onDataReceived(ArrayList<User> updatedUList) {
                // Callback triggered when data retrieval is complete
                // Update the RecyclerView with the updated list
                updatedUList = sortUsersDesc(updatedUList);
                LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(updatedUList);
                recyclerView.setAdapter(leaderboardAdapter);
            }
        });

        //Sorting points in reverse order


        recyclerView = findViewById(R.id.ldboardRecycler);

        // Set the layout manager for the RecyclerView
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // Set an item animator for the RecyclerView
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        //Bottom nav view
        //_________________________________________________________________________
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.leaderboard);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item != null) {
                    int id = item.getItemId();

                    if (id == R.id.dashboard) {
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }

                    else if (id == R.id.search) {
                        Intent intent = new Intent(getApplicationContext(), Search.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }

                    else if (id == R.id.home) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                    }
                    else if (id == R.id.leaderboard) {
                        return true;
                    }
                    else if (id == R.id.about) {
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        intent.putExtra("Username", username); // Replace 'username' with your actual variable name
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    //Method to query all the user data objects, plus the points
    //___________________________________________________________
    public ArrayList<User> queryAllUsersAndPoints(ArrayList<User> uList, DataCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve the points attribute
                    Integer points = userSnapshot.child("points").getValue(Integer.class);
                    String username = userSnapshot.getKey(); // Assuming the key is the username
                    Log.v("User Points", "User: " + username + ", Points: " + points);

                    // Skip if username or points is null
                    if (username == null || points == null) {
                        continue; // Skip this iteration and proceed to the next user
                    }

                    // We temporarily set the password to null. Anyway, it is not needed and confidential.
                    User eachUser = new User(username, null, points);
                    uList.add(eachUser);
                }
                callback.onDataReceived(uList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });

        return uList;
    }


    // Method to sort users in descending order based on points
    private ArrayList<User> sortUsersDesc(ArrayList<User> userList) {
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                // Compare the points in descending order
                return Integer.compare(user2.getPoints(), user1.getPoints());
            }
        });
        return userList;
    }


}