package sg.edu.np.mad.madasgcacheflash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private ArrayList<MyObject> list_object;

    public CustomAdapter(ArrayList<MyObject> input){
        list_object = input;
    }
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.customlayout, parent, false);
            CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customlayout, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;

    }

    public void onBindViewHolder(CustomViewHolder holder, int position) {
        MyObject obj = list_object.get(position);
        holder.txt.setText(obj.getText());
        //holder.desc.setText(obj.getDesc());
        holder.image.setImageResource(obj.getMyImageView());
    }

    public int getItemCount(){
        return list_object.size();
    }

}
