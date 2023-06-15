package sg.edu.np.mad.madasgcacheflash;

import com.google.gson.annotations.SerializedName;


public class JsonExample {

    @SerializedName("jack")
    User jack;

    public void setJack(User jack) {
        this.jack = jack;
    }
    public User getJack() {
        return jack;
    }

}