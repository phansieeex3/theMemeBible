package group3.tcss450.uw.edu.thememebible;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import group3.tcss450.uw.edu.thememebible.Model.Meme;

/**
 * Displays images retrieved via the Meme Generator Web API.
 *
 * @author Vu Hoang
 * @author Phansa Chaonpoj
 * @author Peter Phe
 * @version 1.0
 */
public class PhotoFragment extends Fragment {

    private static final String TAG = "PhotoFragment";
    private static final int COLUMN_COUNT = 3;

    private List<Photo> mPhoto;
    private RecyclerView mRecyclerView;
    private ImageView mItemImage;
    private ArrayList<Meme> mMemeData;
    private OnPhotofragmentInteractionListener mListener;

    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * To avoid network on mainthread exception
         * http://stackoverflow.com/questions/25093546/android-os-networkonmainthreadexception-at-android-os-strictmodeandroidblockgua
         */
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        View v = inflater.inflate(R.layout.fragment_meme_list, container, false);

        mItemImage = (ImageView) v.findViewById(R.id.item_image);


        if (v instanceof RecyclerView) {
            Context context = v.getContext();
            mRecyclerView = (RecyclerView) v;

            /*
             * If orientation changes. then view four on each side.
             * source: http://stackoverflow.com/questions/29579811/changing-number-of-columns-with-gridlayoutmanager-and-recyclerview
             */
            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, COLUMN_COUNT, 0, false));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, COLUMN_COUNT+1));
            }
        }

        mRecyclerView.setHasFixedSize(true);

        // check if Bundle was packaged in arguments for displaying
        if (getArguments() != null) {
            mMemeData = (ArrayList<Meme>) getArguments()
                    .getSerializable(getString(R.string.photo_data_key));
        } else {
            mMemeData = new ArrayList<>();
        }

        initializeData();
        initializeAdapter();

        return v;
    }

    /**
     * Helper method to create the list of photos for the RecyclerView adapter.
     */
    private void initializeData() {
        mPhoto = new ArrayList<>();

        for(int i = 0 ; i < mMemeData.size(); i++) {
            Drawable d = null;

            // if there's an instanceImageUrl link (captioned image), load it
            if (mMemeData.get(i).getmInstanceImageUrl() != null)
                d = LoadImageFromWebOperations(mMemeData.get(i).getmInstanceImageUrl());
            else // load imageUrl link instead (generator image)
                d = LoadImageFromWebOperations(mMemeData.get(i).getmImageUrl());

            if (d != null)
                mPhoto.add(new Photo((Drawable)d, mMemeData.get(i)));
        }
    }

    /**
     * When attached initialize the listener
     *
     * @param context Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotofragmentInteractionListener) {
            mListener = (OnPhotofragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPhotofragmentInteractionListener");
        }
    }

    /**
     * Call super's onDetach and set our listener to null;
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Helper method to initialize the RecyclerAdapter Photo data.
     */
    private void initializeAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(mPhoto, mListener);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     *
     */
    public interface OnPhotofragmentInteractionListener
    {
        void onPhotofragmentInteractionListener(Drawable d, Meme m);
    }

    /**
     * Converting url into a drawable object.
     * @param url of the picture
     * @return drawable object of the picture.
     * source: http://stackoverflow.com/questions/6407324/how-to-display-image-from-url-on-android
     */
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.e("Cannot draw: ",  e.toString());
            return null;
        }
    }
}
