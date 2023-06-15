package sg.edu.np.mad.madasgcacheflash;

import java.util.List;
//__________________________________________________________________________________________________
// Source from: Bard AI - https://bard.google.com/?hl=en_GB
public class User {
    private String username;
    private String password;
    private List<Flashcard> flashcards;
    public User(){}

    // Constructors, getters, and setters
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
}
