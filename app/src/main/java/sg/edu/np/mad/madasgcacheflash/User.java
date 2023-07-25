package sg.edu.np.mad.madasgcacheflash;

import java.util.List;
//__________________________________________________________________________________________________
// Source from: Bard AI - https://bard.google.com/?hl=en_GB
public class User {
    private String username;
    private String password;
    private int points;
    private List<Flashcard> flashcards;
    public User(){}

    // Constructors, getters, and setters

    //Setting points = 0, as a default parameter if it is not specified
    //_______________________________________________________
    public User(String username, String password) {
        this(username, password, 0); // Call the three-parameter constructor with points=0
    }
    public User(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;
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
    public int getPoints() {return points;}
    public void setPoints(int points) { this.points = points; }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }


}
