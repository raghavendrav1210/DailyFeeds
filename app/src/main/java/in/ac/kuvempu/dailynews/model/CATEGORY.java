package in.ac.kuvempu.dailynews.model;

import android.graphics.drawable.Drawable;

/**
 * Created by raghav on 4/8/2017.
 */

public class CATEGORY {

    public String catName;

    public Drawable imageId;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Drawable getImageId() {
        return imageId;
    }

    public void setImageId(Drawable imageId) {
        this.imageId = imageId;
    }
}
