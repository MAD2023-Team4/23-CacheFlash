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

public class FlashcardPairAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    @Override
    public int getItemViewType(int position) {
        // Return different view types for the first and second flashcards
        if (position % 2 == 0) {
            return 0; // First flashcard
        } else {
            return 1; // Second flashcard
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the appropriate layout based on the view type
        if (viewType == 0) {
            View itemView = inflater.inflate(R.layout.flashcard_for_dashboard, parent, false);
            return new FlashcardViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.flashcard_for_dashboard, parent, false);
            return new FlashcardPairViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FlashcardPair flashcardPair = flashcardPairList.get(position);

        if (holder instanceof FlashcardViewHolder) {
            FlashcardViewHolder flashcardViewHolder = (FlashcardViewHolder) holder;
            Flashcard firstFlashcard = flashcardPair.getFirstFlashcard();

            flashcardViewHolder.titleTextView.setText(firstFlashcard.getTitle());
            flashcardViewHolder.descTextView.setText(firstFlashcard.getQuestions().size() + " questions");
            // Bind other views for the first flashcard as needed
        } else if (holder instanceof FlashcardPairViewHolder) {
            FlashcardPairViewHolder pairViewHolder = (FlashcardPairViewHolder) holder;
            Flashcard secondFlashcard = flashcardPair.getSecondFlashcard();

            pairViewHolder.titleTextView.setText(secondFlashcard.getTitle());
            pairViewHolder.descTextView.setText(secondFlashcard.getQuestions().size() + " questions");
            // Bind other views for the second flashcard as needed
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
