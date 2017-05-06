package group3.tcss450.uw.edu.thememebible;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import group3.tcss450.uw.edu.thememebible.Model.Meme;


/**
 * Displays images retrieved via the Meme Generator Web API.
 */
public class PhotoFragment extends Fragment   {

    /** list of photos. */
    private List<Photo> mPhoto;
    /** RecyclerView */
    private RecyclerView rv;
    /**list of memes from API */
    private List<Meme> mMemes;
    private static final String TAG = "PhotoFragment";
    private static final String API_KEY = "7A81A4B0-C434-4DA2-B8D6-1A63E5D63400";
    private String mQuery;

    public void setmQuery(String mQuery) {
        this.mQuery = mQuery;
    }

    //hardcode for now.
    private String mUser = "phansac";
    private String mPass = "nomnomnom2";
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private static final int COLUMN_COUNT = 3;
    private ImageView mItemImage;
    /**for our search api. */
    private String mSearch;




    /**different links for different searches.*/
    private String mLink = "http://version1.api.memegenerator.net/Generators_Select_ByNew?pageIndex=0&pageSize=12";


    public PhotoFragment() {

    }


    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mMemes = new ArrayList<Meme>();
        /** to avoid network on mainthread exception
         * http://stackoverflow.com/questions/25093546/android-os-networkonmainthreadexception-at-android-os-strictmodeandroidblockgua*/
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        
        View v = inflater.inflate(R.layout.fragment_meme_list, container, false);

        mItemImage = (ImageView) v.findViewById(R.id.item_image);

        if(v instanceof RecyclerView){
            Context context = v.getContext();
            rv = (RecyclerView) v;


            /*If orientation changes. then view four on each side.
             * source: http://stackoverflow.com/questions/29579811/changing-number-of-columns-with-gridlayoutmanager-and-recyclerview */
            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                rv.setLayoutManager(new GridLayoutManager(context, COLUMN_COUNT, 0, false));
            }

            else{
                rv.setLayoutManager(new GridLayoutManager(context, COLUMN_COUNT+1));
            }

        }




        rv.setHasFixedSize(true);
        //for the search.....
        String url = buildUrl(mLink);
        AsyncTask<String, Void, String> task = new DownloadPhotos();
        task.execute(url, "downloading photos");





        return v;
    }

    /**
     * Put all of our pictures in here.
     */
    private void initializeData(){
        mPhoto = new ArrayList<>();
        Log.e("Meme size" + TAG, "" + mMemes.size());

        for(int i = 0 ; i < mMemes.size(); i++)
        {
            Log.e(TAG, "" + mMemes.get(i).getmImageUrl() );
            Drawable d = LoadImageFromWebOperations(mMemes.get(i).getmImageUrl());
            if(d != null)
            mPhoto.add(new Photo((Drawable)d));



        }


    }


    /**
     * COnverting url into a drawable object.
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

    private void initializeAdapter(){
        RecyclerAdapter adapter = new RecyclerAdapter(mPhoto);
        rv.setAdapter(adapter);
    }

    /**
     * This method helps append the url.

     * @return a String url.
     */
    private String buildUrl(String link) {

        StringBuilder sb = new StringBuilder(link);
        try {
            sb.append("&apiKey=");
            sb.append(URLEncoder.encode(API_KEY, "UTF-8"));

            if(mQuery!=null) //check if search query then build the string.
            {
                sb.append("&q=");
                sb.append(URLEncoder.encode(mQuery, "UTF-8"));
            }

            Log.i("buildString", sb.toString());

        } catch (Exception e) {
            Log.e("Catch", e.getMessage());
            Toast.makeText(getActivity(), "url is not correct" + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }



    /**
     * an Async class that retrieves data from the database.
     */
    private class DownloadPhotos extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = urls[0];
            try {
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response; }

        /**
         * We retrieve data only until there are 10 trip items in the view, if there are any more
         * trips that need to be retrieved from the database, then the earliest trips are deleted while
         * the latest trips are kept, only dipslaying the previous 10 trips.
         *
         * @param result String
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }


            mMemes = new ArrayList<Meme>();

            Bundle args = new Bundle();
            //args.putString("Random Setlist", result);

            try {

                //parse in the objects.
                JSONArray obj = new JSONObject(result).getJSONArray("result");
                Meme m = new Meme();
                for(int i =0 ; i < obj.length(); i++)
                {
                    JSONObject object = obj.getJSONObject(i);
                    mMemes.add(Meme.getMeme(object));
                }



                Log.e("holy " , mMemes.toString());

                if(mMemes.isEmpty())
                {
                    Toast.makeText(getContext(), "Sorry cant find anything :/", Toast.LENGTH_LONG).show();
                }



            }catch (Throwable t){
                Log.e("TAG", "Could not parse malformed JSON: " + t.toString());
            }

            Log.d(TAG, result);


            initializeData();
            initializeAdapter();

        }
    }

}
