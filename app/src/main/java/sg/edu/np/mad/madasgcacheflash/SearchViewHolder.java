package sg.edu.np.mad.madasgcacheflash;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SearchViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView category;
    TextView lenQns;
    ImageView image;
    public SearchViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.titleOfCard);
        category = itemView.findViewById(R.id.category);
        lenQns = itemView.findViewById(R.id.lenQns);
        image = itemView.findViewById(R.id.imgOfCard);
    }
}
