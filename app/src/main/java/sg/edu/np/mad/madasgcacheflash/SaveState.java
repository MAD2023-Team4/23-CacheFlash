package sg.edu.np.mad.madasgcacheflash;

import android.content.Context;
import android.content.SharedPreferences;


//--------------------------------------------------------------------------------------
//Source from: https://www.youtube.com/watch?v=qX5Z5eKMfd0
//This is an on-boarding screen layout that guides the user.
//--------------------------------------------------------------------------------------
public class SaveState {

    Context context;
    String saveName;
    SharedPreferences sp;

    public SaveState(Context context, String saveName) {
        this.context = context;
        this.saveName = saveName;
        sp = context.getSharedPreferences(saveName,context.MODE_PRIVATE);
    }

    public void setState(int key){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("Key",key);
        editor.apply();
    }

    public int getState(){
        return sp.getInt("Key",0);
    }
}
