package sg.edu.np.mad.madasgcacheflash;

public class FlashcardPair {
    private Flashcard firstFlashcard;
    private Flashcard secondFlashcard;

    public FlashcardPair(Flashcard firstFlashcard, Flashcard secondFlashcard) {
        this.firstFlashcard = firstFlashcard;
        this.secondFlashcard = secondFlashcard;
    }

    public Flashcard getFirstFlashcard() {
        return firstFlashcard;
    }

    public Flashcard getSecondFlashcard() {
        return secondFlashcard;
    }

    public void setFirstFlashcard(Flashcard firstFlashcard) {
        this.firstFlashcard = firstFlashcard;
    }

    public void setSecondFlashcard(Flashcard secondFlashcard) {
        this.secondFlashcard = secondFlashcard;
    }
}
