package sg.edu.np.mad.madasgcacheflash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class CalendarView extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        calendarView = findViewById(R.id.calendarView);
        editText = findViewById(R.id.editTextText);

        calendarView.setonClickListener()
    }
}