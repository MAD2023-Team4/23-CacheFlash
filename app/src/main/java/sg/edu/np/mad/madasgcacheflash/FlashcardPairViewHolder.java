package sg.edu.np.mad.madasgcacheflash;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FlashcardPairViewHolder extends RecyclerView.ViewHolder {
    // Declare the views in flashcard_item.xml for the first flashcard
    public TextView titleTextView;
    public TextView descTextView;

    // Declare the views for the second flashcard
    public TextView secondTitleTextView;
    public TextView secondDescTextView;

    public FlashcardPairViewHolder(View itemView) {
        super(itemView);

        // Initialize the views for the first flashcard
        titleTextView = itemView.findViewById(R.id.titleTextView);
        descTextView = itemView.findViewById(R.id.textViewDesc);

        // Initialize the views for the second flashcard
        secondTitleTextView = itemView.findViewById(R.id.secondTitleTextView);
        secondDescTextView = itemView.findViewById(R.id.secondTextViewDesc);
    }
}

