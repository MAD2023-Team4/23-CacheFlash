package sg.edu.np.mad.madasgcacheflash;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

//Source from: https://www.gsrikar.com/2019/03/add-spacing-to-recycler-view-linear.html
//_________________________________________________________________________________________
public class SpaceItemDeco extends RecyclerView.ItemDecoration {
    private int spacing;

    public SpaceItemDeco(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = spacing;
        outRect.right = spacing;
        outRect.bottom = spacing;

        /*
        // Add top spacing only for the first item to avoid uneven spacing
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spacing;
        } else {
            outRect.top = 0;
        }
        */

    }
}