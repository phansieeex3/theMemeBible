package group3.tcss450.uw.edu.thememebible;

import android.graphics.drawable.Drawable;

import group3.tcss450.uw.edu.thememebible.Model.Meme;

/**
 * Created by Vu Hoang on 4/30/2017.
 * For holding the image in a drawable.
 */

public class Photo  {

    int photoID;
    Drawable mPhotoDraw;
    Meme mMeme;

    Photo (int photoID, Drawable draw, Meme meme) {
        this.photoID = photoID;
        mPhotoDraw = draw;
        mMeme = meme;
    }


    /**Overloaded constructor */
    Photo (Drawable draw, Meme meme) {
        mPhotoDraw = draw;
        photoID = 0;
        mMeme = meme;
    }
}
