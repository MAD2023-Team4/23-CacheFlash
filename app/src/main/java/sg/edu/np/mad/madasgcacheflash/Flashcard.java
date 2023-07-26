package sg.edu.np.mad.madasgcacheflash;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flashcard implements Parcelable {
    private String title;
    private List<String> questions;
    private List<String> answers;
    private String username;
    private String category;
    private Map<String, Double> percentage; // Store percentage as HashMap for different difficulty levels
    private Map<String, Integer> attempts;
    private int imageResourceId;
    private double totalPercentage;
    public Flashcard() {
        percentage = new HashMap<>();
        attempts = new HashMap<>();

    }

    public Flashcard(String title, List<String> questions, List<String> answers, String username, String category) {
        this.title = title;
        this.questions = questions;
        this.answers = answers;
        this.username = username;
        this.category = category;
        percentage = new HashMap<>();
        this.imageResourceId = imageResourceId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Double> getPercentage() {
        return percentage;
    }

    public void setPercentage(Map<String, Double> percentage) {
        this.percentage = percentage;
    }
    public Map<String, Integer> getAttempts() {
        return attempts;
    }

    public void setAttempts(Map<String, Integer> attempts) {
        this.attempts = attempts;
    }

    // Getter and setter for imageResourceId
    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }



    public void updateStats(double percentage, String difficultyLevel) {
        // Update the total percentage and increment the attempts for the respective difficulty level
        totalPercentage += percentage;

        int currentAttempts = 0;
        if (attempts.containsKey(difficultyLevel)) {
            currentAttempts = attempts.get(difficultyLevel);
        }
        attempts.put(difficultyLevel, currentAttempts + 1);
    }

    // Method to calculate and return the average percentage for this flashcard
    public double getAveragePercentage() {
        int totalAttempts = 0;
        for (int attemptsCount : attempts.values()) {
            totalAttempts += attemptsCount;
        }

        if (totalAttempts > 0) {
            return totalPercentage / totalAttempts;
        } else {
            return 0.0;
        }
    }

    protected Flashcard(Parcel in) {
        title = in.readString();
        questions = in.createStringArrayList();
        answers = in.createStringArrayList();

    }

    public static final Creator<Flashcard> CREATOR = new Creator<Flashcard>() {
        @Override
        public Flashcard createFromParcel(Parcel in) {
            return new Flashcard(in);
        }

        @Override
        public Flashcard[] newArray(int size) {
            return new Flashcard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringList(questions);
        dest.writeStringList(answers);
    }
}
