package sg.edu.np.mad.madasgcacheflash;

public class MyObject {
    private int myImageView;
    private String text;
    //private String desc;

    public MyObject(int myImageView, String text) {
        this.myImageView = myImageView;
        this.text = text;
        //this.desc = desc;
    }

    public MyObject() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) { this.text = text; }
    //public String getDesc() {
    //return desc;
    //}

    //public void setDesc(String desc) { this.desc = desc; }
    public int getMyImageView() {
        return myImageView;
    }

    public void setMyImageView(int myImageView) {
        this.myImageView = myImageView;
    }
}

