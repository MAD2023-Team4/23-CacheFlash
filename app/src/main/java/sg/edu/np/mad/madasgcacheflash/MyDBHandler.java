package sg.edu.np.mad.madasgcacheflash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDBHandler extends SQLiteOpenHelper {
    String title = "MyDBHandler";
    String currentUser;
    String currentPass;
    public static int DATABASE_VERSION = 1;
    //start with version 1, or version 0 also can.
    public static String DATABASE_NAME = "accountDB.db";
    public static String ACCOUNTS = "Accounts";
    //account is the table we are using.
    public static String FLASHCARDS = "Flashcards";
    //Flashcards is the table we are using.
    public static String COLUMN_USERNAME = "UserName";
    //a column for username
    public static String COLUMN_PASSWORD = "Password";
    //and a column for password
    public static String COLUMN_TITLE = "Title";
    //a column for questions
    public static String COLUMN_QUESTIONS = "Questions";
    //a column for questions
    public static String COLUMN_ANSWERS = "Answers";
    //and a column for answers

    // S.No   UserName    Pwd
    // 1      ...         ...
    // 2      ...         ...
    // 3


    public MyDBHandler(Context context, String title) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.title = title;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //onCreate just means we want to create the database, that's it.
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + ACCOUNTS + "(" + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";

        String CREATE_FLASHCARD_TABLE = "CREATE TABLE " + FLASHCARDS + "(" +COLUMN_TITLE + " TEXT, "
                + COLUMN_QUESTIONS + " TEXT, "
                + COLUMN_ANSWERS + " TEXT, "
                + COLUMN_USERNAME + " TEXT)";

        //CREATE TABLE ACCOUNTS (
        //  COLUMN_USERNAME TEXT,
        //  COLUMN_PASSWORD TEXT
        //);

        Log.i(title, CREATE_ACCOUNT_TABLE);
        Log.i(title, CREATE_FLASHCARD_TABLE);
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_FLASHCARD_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS);
        onCreate(db);
    }
    public void addUser(User userData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userData.getUsername());
        values.put(COLUMN_PASSWORD, userData.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ACCOUNTS, null, values);

        Log.i(title, "Inserted/created user" + values.toString());
        db.close();

        //This concludes the function for CREATE (C). CRUD.
    }

    public User findUser(String username){
        String query = "SELECT * FROM " + ACCOUNTS + " WHERE " +
                COLUMN_USERNAME + "=\'" + username + "\'";
        Log.i(title, "Query: " + query);

        //SELECT * FROM ACCOUNTS
        //WHERE COLUMN_USERNAME = 'username';

        User queryResult = new User();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            queryResult.setUsername(cursor.getString(0));
            queryResult.setPassword(cursor.getString(1));
            currentUser = queryResult.getUsername();
            currentPass = queryResult.getPassword();
            Log.i(title, "Queried Username: " + queryResult.getUsername());
            Log.i(title, "Queried Password: " + queryResult.getPassword());
            cursor.close();
        }
        else{
            queryResult = null;
        }
        db.close();
        return queryResult;
    }

    public void saveFlashcard(Flashcard flashcard) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, flashcard.getTitle());
        values.put(COLUMN_QUESTIONS, TextUtils.join("|", flashcard.getQuestions()));
        values.put(COLUMN_ANSWERS, TextUtils.join("|", flashcard.getAnswers()));
        values.put(COLUMN_USERNAME, flashcard.getUsername());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(FLASHCARDS, null, values);

        Log.i(title, "Inserted/created flashcard" + values.toString());
        db.close();

        // Log a message to indicate that the Flashcard was saved successfully
        Log.i("MyDBHandler", "Flashcard saved successfully");
    }
}
