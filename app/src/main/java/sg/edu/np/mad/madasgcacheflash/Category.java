package sg.edu.np.mad.madasgcacheflash;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;

public class Category implements Parcelable {
    private String name;
    private List<Flashcard> flashcards;

    // Constructor
    public Category() {
        // Empty constructor required for Firebase
    }

    public Category(String name, List<Flashcard> flashcards) {
        this.name = name;
        this.flashcards = flashcards;
    }

    // Getter for name
    public String getName() {
        Log.d("CategoryDebugy", "Getting category name: " + name);
        return name;
    }

    // Setter for name
    public void setName(String name) {
        Log.d("CategoryDebugy", "Setting category name: " + name);
        this.name = name;
    }

    // Getter for flashcards
    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    // Setter for flashcards
    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    // Parcelable implementation
    protected Category(Parcel in) {
        name = in.readString();
        flashcards = in.createTypedArrayList(Flashcard.CREATOR);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(flashcards);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}


