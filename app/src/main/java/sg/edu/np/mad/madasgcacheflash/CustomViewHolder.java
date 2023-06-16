package sg.edu.np.mad.madasgcacheflash;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    TextView txt;
    //TextView desc;
    ImageView image;
    public CustomViewHolder(View itemView) {
        super(itemView);
        txt = itemView.findViewById(R.id.textView2);
        //desc = itemView.findViewById(R.id.textView3);
        image = itemView.findViewById(R.id.imageView2);
    }
}