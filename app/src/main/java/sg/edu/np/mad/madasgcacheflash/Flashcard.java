package sg.edu.np.mad.madasgcacheflash;

import java.util.List;
//__________________________________________________________________________________________________
// Source from: Bard AI - https://bard.google.com/?hl=en_GB
public class Flashcard {
    private String title;
    private List<String> questions;
    private List<String> answers;
    private String username;
    public Flashcard(){}

    public Flashcard(String title, List<String> questions, List<String> answers, String username) {
        this.title = title;
        this.questions = questions;
        this.answers = answers;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
