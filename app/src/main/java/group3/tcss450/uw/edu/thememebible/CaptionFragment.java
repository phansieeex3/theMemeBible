package group3.tcss450.uw.edu.thememebible;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * This fragment allows for captioning of meme images.
 *
 * @author Vu Hoang
 */
public class CaptionFragment extends Fragment implements View.OnClickListener {

    private Drawable mDrawable;

    public CaptionFragment(Drawable d) {
        // Required empty public constructor
        mDrawable = d;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_caption, container, false);
        ImageView meme = (ImageView) v.findViewById(R.id.meme_item);
        meme.setImageDrawable(mDrawable);


        return v;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnFragmentInteractionListener {
    }
}