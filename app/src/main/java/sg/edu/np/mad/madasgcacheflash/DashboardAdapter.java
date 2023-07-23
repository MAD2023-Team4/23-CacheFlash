package sg.edu.np.mad.madasgcacheflash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.FlashcardViewHolder> {
    private List<Flashcard> flashcardList;

    public DashboardAdapter(List<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard_dashboard, parent, false);
        return new FlashcardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        // Calculate the position of the first flashcard in the list
        int firstFlashcardPosition = position * 2;

        // Check if the first flashcard position is within the list size
        if (firstFlashcardPosition < flashcardList.size()) {
            Flashcard firstFlashcard = flashcardList.get(firstFlashcardPosition);
            holder.bindFlashcard1(firstFlashcard);
        } else {
            holder.clearFlashcard1();
        }

        // Calculate the position of the second flashcard in the list
        int secondFlashcardPosition = firstFlashcardPosition + 1;

        // Check if the second flashcard position is within the list size
        if (secondFlashcardPosition < flashcardList.size()) {
            Flashcard secondFlashcard = flashcardList.get(secondFlashcardPosition);
            holder.bindFlashcard2(secondFlashcard);
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

        // Bind the flashcard data to the layout views for flashcard 1
        public void bindFlashcard1(Flashcard flashcard) {
            titleTextView1.setText(flashcard.getTitle());
            descTextView1.setText(flashcard.getPercentage() + "%");
            descTextView3.setText("Recall time: NA");
        }

        // Bind the flashcard data to the layout views for flashcard 2
        public void bindFlashcard2(Flashcard flashcard) {
            titleTextView2.setText(flashcard.getTitle());
            descTextView2.setText(flashcard.getPercentage() + "%");
            descTextView4.setText("Recall time: NA");
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
    }
}




