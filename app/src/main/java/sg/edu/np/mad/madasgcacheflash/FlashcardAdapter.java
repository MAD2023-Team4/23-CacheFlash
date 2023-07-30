package sg.edu.np.mad.madasgcacheflash;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardViewHolder> {
    private List<Flashcard> flashcardList;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Flashcard flashcard);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public FlashcardAdapter(List<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flashcard_item, parent, false);
        return new FlashcardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard flashcard = flashcardList.get(position);

        // Bind the data to the views in the flashcard_item.xml layout
        holder.titleTextView.setText(flashcard.getTitle());
        holder.descTextView.setText(flashcard.getQuestions().size() + " questions");
        // Check if the imageResourceId is valid (not null or 0)
        if (flashcard.getImageResourceId() != 0) {
            // Load the image into the ImageView using the resource ID
            holder.imageView.setImageResource(flashcard.getImageResourceId());
        } else {
            // If imageResourceId is null or 0, set a default picture here
            holder.imageView.setImageResource(R.drawable.defaultlearningimage); // Replace "default_image" with the resource ID of your default image
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the onItemClick method of the listener when the card is clicked
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(flashcard);
                    Log.v("Hi","Hi");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }
}