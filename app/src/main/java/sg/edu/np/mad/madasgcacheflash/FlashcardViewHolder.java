package sg.edu.np.mad.madasgcacheflash;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FlashcardViewHolder extends RecyclerView.ViewHolder {
    // Declare the views in flashcard_item.xml
    public TextView titleTextView;

    public TextView descTextView;

    public FlashcardViewHolder(View itemView) {
        super(itemView);

        // Initialize the views in flashcard_item.xml
        titleTextView = itemView.findViewById(R.id.titleTextView);
        descTextView = itemView.findViewById(R.id.textViewDesc);
        // Initialize other views as needed

    }

}