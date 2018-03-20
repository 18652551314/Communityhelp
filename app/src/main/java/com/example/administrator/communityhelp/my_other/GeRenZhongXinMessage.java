package com.example.administrator.communityhelp.my_other;

/**
 * Created by Administrator on 2017/1/3.
 */
public class GeRenZhongXinMessage {
    private  int image;
    private String text;

    public GeRenZhongXinMessage(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
