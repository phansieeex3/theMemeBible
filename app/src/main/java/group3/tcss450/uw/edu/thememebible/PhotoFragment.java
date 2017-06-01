package group3.tcss450.uw.edu.thememebible;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import group3.tcss450.uw.edu.thememebible.Listener.EndlessRecyclerViewScrollListener;
import group3.tcss450.uw.edu.thememebible.Model.Meme;
import group3.tcss450.uw.edu.thememebible.Utility.UrlBuilder;

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
    private static final int IMAGES_PER_REQUEST = 24;

    private List<Photo> mPhoto;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private GridLayoutManager mLayoutManager;
    private ArrayList<Meme> mMemeData;
    private LoadingFragment mLoadingFragment;
    private OnPhotofragmentInteractionListener mListener;
    private String mQueryType;

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

        mLoadingFragment = new LoadingFragment();

        if (v instanceof RecyclerView) {
            Context context = v.getContext();
            mRecyclerView = (RecyclerView) v;

            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // constructor allows for vertical scrolling
                mLayoutManager = new GridLayoutManager(context, COLUMN_COUNT);
            } else {
                mLayoutManager = new GridLayoutManager(context, COLUMN_COUNT + 1);
            }

            mRecyclerView.setLayoutManager(mLayoutManager);
        }

        // recyclerview properties to reduce view creation and binding times
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(IMAGES_PER_REQUEST);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setNestedScrollingEnabled(false);

        // check if Bundle was packaged in arguments for displaying
        if (getArguments() != null) {
            mMemeData = (ArrayList<Meme>) getArguments().getSerializable(getString(R.string.photo_data_key));
            mQueryType = getArguments().getString(getString(R.string.query_type_key)); // for the onLoadMore() ScrollListener
        } else {
            mMemeData = new ArrayList<>();
        }

        // add endless scrolling functionality
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                switch (mQueryType) {
                    case "popular":
                        new DownloadData().execute(UrlBuilder.getGeneratorsSelectByPopularUrl(page));
                        break;
                    case "search":
                        String search = getArguments().getString(getString(R.string.query_string_key));
                        new DownloadData().execute(UrlBuilder.getGeneratorSearchUrl(search, page));
                        break;
                    case "trending":
                        // intentionally empty - API does not allow pages for this call
                        break;
                    case "recent":
                        // intentionally empty - API does not allow pages for this call
                        break;
                }
            }
        });

        initializeData();
        initializeAdapter();

        return v;
    }

    /**
     * Helper method to create the list of photos for the RecyclerView adapter.
     */
    private void initializeData() {
        mPhoto = new ArrayList<>();

        for (int i = 0; i < mMemeData.size(); i++) {
            Drawable d = null;

            // if there's an instanceImageUrl link (captioned image), load it
            if (mMemeData.get(i).getmInstanceImageUrl() != null)
                d = LoadImageFromWebOperations(mMemeData.get(i).getmInstanceImageUrl());
            else // load imageUrl link instead (generator image)
                d = LoadImageFromWebOperations(mMemeData.get(i).getmImageUrl());

            if (d != null)
                mPhoto.add(new Photo(d, mMemeData.get(i)));
        }
    }

    /**
     * Helper method to initialize the RecyclerAdapter Photo data.
     */
    private void initializeAdapter() {
        mRecyclerAdapter = new RecyclerAdapter(mPhoto, mListener);
        mRecyclerView.setAdapter(mRecyclerAdapter);
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
     * Converting url into a drawable object.
     *
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
            Log.e("Cannot draw: ", e.toString());
            return null;
        }
    }

    /**
     * Callback interface.
     */
    public interface OnPhotofragmentInteractionListener {
        void onPhotofragmentInteractionListener();
        void setDrawableArgs(Drawable d, Meme m);
        void updateRecyclerAdapterData(RecyclerAdapter theRecyclerAdapter, int start, int end);
    }

    /**
     * Loads more images when scrolling.
     */
    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // show progress bar
            getFragmentManager().beginTransaction().add(R.id.fragmentContainer, mLoadingFragment)
                    .commit();
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i(TAG, "Grabbing more images from the API");

            if (strings.length != 1) {
                throw new IllegalArgumentException("One complete URL required (use UrlBuilder).");
            }

            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0].replaceAll(" ", "%20"); // for URLs containing spaces

            try {
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";

                // build response string
                while ((s = buffer.readLine()) != null)
                    response += s;
            } catch (Exception e) {
                response = "Unable to connect, Reason: " + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return response;
        }

        /**
         * Parse the data into a string array.
         *
         * @param result String
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // encountered network issue or issue with URL
            if (result.startsWith("Unable to")) {
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            } else {
                try {
                    ArrayList<Meme> memeList = new ArrayList<>();

                    JSONArray jsonArray = new JSONObject(result).getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Meme m = Meme.getMeme(jsonObject);
                        memeList.add(m);
                        mMemeData.add(m);

                        Drawable d = LoadImageFromWebOperations(Meme.getMeme(jsonObject).getmImageUrl());
                        if (d != null)
                            mPhoto.add(new Photo(d, memeList.get(i)));
                    }

                    // structural change to the recyclerview -> redraws all of the views
//                    mRecyclerAdapter.notifyDataSetChanged();

                    // localized structural change to the recycler view -> draws only the inserted views
//                    mRecyclerAdapter.notifyItemRangeInserted(mPhoto.size()-IMAGES_PER_REQUEST, mPhoto.size());

                    // let UI thread create a worker thread to draw inserted views since onPostExecute()
                    // also runs on the UI thread - don't want to block!
                    mListener.updateRecyclerAdapterData(mRecyclerAdapter, mPhoto.size()-IMAGES_PER_REQUEST,
                            mPhoto.size());

                } catch (JSONException e) {
                    Log.e(TAG, "Could not parse malformed JSON: " + e.getMessage() + result);
                }
            }

            // dismiss progress bar
            getFragmentManager().beginTransaction().remove(mLoadingFragment).commit();
        }
    }
}