package vn.com.gojobs.Model;

import vn.com.gojobs.R;

public class News {
    public int image;
    public String content;
    public int likeicon = R.drawable.ic_like;

    public News(int image, String content){
        this.image = image;
        this.content = content;
    }
}
