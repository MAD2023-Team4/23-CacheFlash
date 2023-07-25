package sg.edu.np.mad.madasgcacheflash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.FlashcardViewHolder> {
    private List<Flashcard> flashcardList;
    private String selectedDifficulty;



    public DashboardAdapter(List<Flashcard> flashcardList, String selectedDifficulty) {
        this.flashcardList = flashcardList;
        this.selectedDifficulty = selectedDifficulty;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard_dashboard, parent, false);
        FlashcardViewHolder viewHolder = new FlashcardViewHolder(itemView);
        viewHolder.setSelectedDifficulty(selectedDifficulty); // Set the selectedDifficulty for the ViewHolder
        return viewHolder;
    }

    // Add a new method to update the flashcard list
    public void setFlashcards(List<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
        notifyDataSetChanged();
    }
    public void setSelectedDifficulty(String selectedDifficulty) {
        this.selectedDifficulty = selectedDifficulty;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        // Calculate the position of the first flashcard in the list
        int firstFlashcardPosition = position * 2;

        // Check if the first flashcard position is within the list size
        if (firstFlashcardPosition < flashcardList.size()) {
            Flashcard firstFlashcard = flashcardList.get(firstFlashcardPosition);
            holder.bindFlashcard1(firstFlashcard,selectedDifficulty);
        } else {
            holder.clearFlashcard1();
        }

        // Calculate the position of the second flashcard in the list
        int secondFlashcardPosition = firstFlashcardPosition + 1;

        // Check if the second flashcard position is within the list size
        if (secondFlashcardPosition < flashcardList.size()) {
            Flashcard secondFlashcard = flashcardList.get(secondFlashcardPosition);
            holder.bindFlashcard2(secondFlashcard,selectedDifficulty);
        } else {
            holder.clearFlashcard2();
        }
    }

    @Override
    public int getItemCount() {
        // Calculate the number of rows based on the number of flashcards
        return (int) Math.ceil(flashcardList.size() / 2.0);
    }

    static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView1;
        TextView descTextView1;
        TextView descTextView3;
        TextView titleTextView2;
        TextView descTextView2;
        TextView descTextView4;

        private String selectedDifficulty;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views in the item_flashcard_dashboard.xml layout for both flashcards
            titleTextView1 = itemView.findViewById(R.id.titleTextView1);
            descTextView1 = itemView.findViewById(R.id.textViewDesc1);
            descTextView3 = itemView.findViewById(R.id.textViewDesc3);
            titleTextView2 = itemView.findViewById(R.id.titleTextView2);
            descTextView2 = itemView.findViewById(R.id.textViewDesc2);
            descTextView4 = itemView.findViewById(R.id.textViewDesc4);

        }
        public void setSelectedDifficulty(String selectedDifficulty) {
            this.selectedDifficulty = selectedDifficulty;
        }
        // Getter method for selectedDifficulty
        public String getSelectedDifficulty() {
            return selectedDifficulty;
        }

        // Bind the flashcard data to the layout views for flashcard 1
        public void bindFlashcard1(Flashcard flashcard, String selectedDifficulty) {
            titleTextView1.setText(flashcard.getTitle());
            Double percentage = getPercentageForDifficulty(flashcard, selectedDifficulty);
            int attempts = getAttemptsForDifficulty(flashcard, selectedDifficulty);
            descTextView1.setText(percentage != null ? String.valueOf(percentage) + "%" : "N/A");
            descTextView3.setText("Attempts: " + attempts);
        }

        // Bind the flashcard data to the layout views for flashcard 2
        public void bindFlashcard2(Flashcard flashcard, String selectedDifficulty) {
            titleTextView2.setText(flashcard.getTitle());
            Double percentage = getPercentageForDifficulty(flashcard, selectedDifficulty);
            int attempts = getAttemptsForDifficulty(flashcard, selectedDifficulty);
            descTextView2.setText(percentage != null ? String.valueOf(percentage) + "%" : "N/A");
            descTextView4.setText("Attempts: " + attempts);
        }

        // Clear the views for flashcard 1
        public void clearFlashcard1() {
            titleTextView1.setText("");
            descTextView3.setText("");
            descTextView1.setText("");
        }

        // Clear the views for flashcard 2
        public void clearFlashcard2() {
            titleTextView2.setText("");
            descTextView2.setText("");
            descTextView4.setText("");
        }
        // Helper method to get the percentage for the selected difficulty from the Flashcard object
        private Double getPercentageForDifficulty(Flashcard flashcard, String selectedDifficulty) {
            Map<String, Double> percentageMap = flashcard.getPercentage();
            if (percentageMap != null) {
                return percentageMap.get(selectedDifficulty.toLowerCase());
            }
            return null;
        }

        // Helper method to get the number of attempts for the selected difficulty from the Flashcard object
        private int getAttemptsForDifficulty(Flashcard flashcard, String selectedDifficulty) {
            Map<String, Integer> attemptsMap = flashcard.getAttempts();
            if (attemptsMap != null) {
                return attemptsMap.get(selectedDifficulty.toLowerCase());
            }
            return 0;
        }
    }
}




