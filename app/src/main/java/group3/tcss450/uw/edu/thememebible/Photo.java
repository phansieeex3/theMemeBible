package group3.tcss450.uw.edu.thememebible;

import android.graphics.drawable.Drawable;

/**
 * Created by Vu Hoang on 4/30/2017.
 * For holding the image in a drawable.
 */

public class Photo  {

    int photoID;
    Drawable mPhotoDraw;

    Photo (int photoID, Drawable draw){
        this.photoID = photoID;
        mPhotoDraw = draw ;

    }


    /**Overloaded constructor */
    Photo ( Drawable draw){
        mPhotoDraw = draw ;

        photoID = 0;
    }


}
