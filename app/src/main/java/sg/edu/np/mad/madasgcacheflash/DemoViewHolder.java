package sg.edu.np.mad.madasgcacheflash;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DemoViewHolder extends RecyclerView.ViewHolder {
    String title = "ViewHolder!";
    TextView txt;
    //TextView desc;
    public DemoViewHolder(View itemView) {
        super(itemView);
        txt = itemView.findViewById(R.id.textView2);
        //desc = itemView.findViewById(R.id.textView3);
        Log.i(title, "viewholder"); //v stands for ??, i stands for information being logged.
        //w is warning (amber yellow).
    }
}