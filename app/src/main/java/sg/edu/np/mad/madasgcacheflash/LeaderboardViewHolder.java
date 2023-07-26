package sg.edu.np.mad.madasgcacheflash;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
    private TextView usernameTextView;
    private TextView pointsTextView;

    public LeaderboardViewHolder(@NonNull View itemView) {
        super(itemView);
        usernameTextView = itemView.findViewById(R.id.usernameTextView);
        pointsTextView = itemView.findViewById(R.id.pointsTextView);
    }

    public void bind(User user) {
        usernameTextView.setText(user.getUsername());
        pointsTextView.setText(String.valueOf(user.getPoints()));
    }
}

