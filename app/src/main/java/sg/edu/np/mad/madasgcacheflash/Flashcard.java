package sg.edu.np.mad.madasgcacheflash;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.PublicKey;
import java.util.List;
//__________________________________________________________________________________________________
// Source from: Bard AI - https://bard.google.com/?hl=en_GB
public class Flashcard implements Parcelable {
    private String title;
    private List<String> questions;
    private List<String> answers;
    private String username;
    private double percentage;
    private String category;

    public Flashcard() {
    }

    public Flashcard(String title, List<String> questions, List<String> answers, String username, String category) {
        this.title = title;
        this.questions = questions;
        this.answers = answers;
        this.username = username;
        this.category = category;
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

    public String getCategory() { return category; }

    public  void setCategory(String category)  { this.category = category; }

    public double getPercentage() { return percentage; }

    public void setPercentage(double percentage)  { this.percentage = percentage; }


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