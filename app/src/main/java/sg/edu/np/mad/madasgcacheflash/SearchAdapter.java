package sg.edu.np.mad.madasgcacheflash;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private ArrayList<Flashcard> list_object;
    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void onItemClick (Flashcard flashcard);
    }

    public void setFilteredList(ArrayList<Flashcard> filteredList){
        this.list_object = filteredList;
        notifyDataSetChanged();

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public SearchAdapter(ArrayList<Flashcard> input){
        list_object = input;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.customlayout, parent, false);
            SearchViewHolder holder = new SearchViewHolder(view);
            return holder;
        }
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customlayout, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);
        return holder;

    }

    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Flashcard obj = list_object.get(position);
        holder.title.setText(obj.getTitle());
        holder.category.setText("Category: " + obj.getCategory());
        holder.lenQns.setText(obj.getQuestions().size() + " questions");
        holder.image.setImageResource(R.drawable.ic_flashcardlogo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the onItemClick method of the listener when the card is clicked
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(obj);
                    Log.v("Hi","Hi");
                }
            }
        });
    }

    public int getItemCount(){
        return list_object.size();
    }
    public void addFlashcard(Flashcard flashcard) {
        list_object.add(flashcard);
        notifyDataSetChanged();
    }
}
