package sg.edu.np.mad.madasgcacheflash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DemoAdapter extends RecyclerView.Adapter<DemoViewHolder> {
    ArrayList<String> data;

    public DemoAdapter(ArrayList<String> input){
        data = input;
    }

    public DemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customlayout, parent, false);
        //inflating the data, just like inflating a balloon
        return new DemoViewHolder(view);
    }

    public void onBindViewHolder(DemoViewHolder holder, int position){
        String s = data.get(position);
        holder.txt.setText(s);
        //holder.desc.setText(s);
    }

    public int getItemCount(){
        return data.size();
    }
}

