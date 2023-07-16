package sg.edu.np.mad.madasgcacheflash;

import java.util.List;

public class Category {
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
        return name;
    }

    // Setter for name
    public void setName(String name) {
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
}

