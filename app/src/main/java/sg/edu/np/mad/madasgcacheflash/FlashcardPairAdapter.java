package sg.edu.np.mad.madasgcacheflash;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.madasgcacheflash.Flashcard;
import sg.edu.np.mad.madasgcacheflash.FlashcardPair;
import sg.edu.np.mad.madasgcacheflash.FlashcardViewHolder;

public class FlashcardPairAdapter extends RecyclerView.Adapter<FlashcardViewHolder> {
    private List<FlashcardPair> flashcardPairList;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(FlashcardPair flashcardPair);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public FlashcardPairAdapter(List<FlashcardPair> flashcardPairList) {
        this.flashcardPairList = flashcardPairList;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flashcard_for_dashboard, parent, false);
        return new FlashcardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        FlashcardPair flashcardPair = flashcardPairList.get(position);

        // Get the two flashcards from the FlashcardPair object
        Flashcard firstFlashcard = flashcardPair.getFirstFlashcard();
        Flashcard secondFlashcard = flashcardPair.getSecondFlashcard();

        // Bind the data for the first flashcard
        holder.titleTextView.setText(firstFlashcard.getTitle());
        holder.descTextView.setText(firstFlashcard.getQuestions().size() + " questions");
        // Bind other views for the first flashcard as needed

        // Create a separate instance of FlashcardViewHolder for the second flashcard
        FlashcardViewHolder secondViewHolder = new FlashcardViewHolder(holder.itemView);

        // Bind the data for the second flashcard using the second ViewHolder
        secondViewHolder.titleTextView.setText(secondFlashcard.getTitle());
        secondViewHolder.descTextView.setText(secondFlashcard.getQuestions().size() + " questions");
        // Bind other views for the second flashcard as needed

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the onItemClick method of the listener when the card is clicked
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(flashcardPair);
                    Log.v("Hi","Hi");
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return flashcardPairList.size();
    }
}
