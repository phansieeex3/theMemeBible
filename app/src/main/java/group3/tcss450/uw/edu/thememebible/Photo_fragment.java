package group3.tcss450.uw.edu.thememebible;


import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import MemeObject.Meme;

/**
 * A simple {@link Fragment} subclass.
 */
public class Photo_fragment extends Fragment {

    private List<Photo> mPhoto;
    private RecyclerView rv;
    private static final String TAG = "MEME FRAGMENT";
    private static final String API_KEY =
            "7A81A4B0-C434-4DA2-B8D6-1A63E5D63400";
    //hardcode for now.
    private String mUser = "yellowbears";
    private String mPass = "nomnomnom2";
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 3;

    /**different links for different searches.*/
    private final String mLink = "http://version1.api.memegenerator.net/Generators_Select_ByNew?pageIndex=0&pageSize=12";



    public Photo_fragment() {
        // Required empty public constructor
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View v = inflater.inflate(R.layout.fragment_meme_list, container, false);

        if(v instanceof RecyclerView){
            Context context = v.getContext();
            rv = (RecyclerView) v;


            /*If orientation changes. then view four on each side.
             * source: http://stackoverflow.com/questions/29579811/changing-number-of-columns-with-gridlayoutmanager-and-recyclerview */
            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                rv.setLayoutManager(new GridLayoutManager(context, 3));
            }
            else{
                rv.setLayoutManager(new GridLayoutManager(context, 4));
            }

        }


        //rv = (RecyclerView) v.findViewById(R.id.recyclerView);
        //LinearLayoutManager llm = new LinearLayoutManager(getContext());
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        //rv.setLayoutManager(llm);


        initializeData();
        initializeAdapter();

        rv.setHasFixedSize(true);

        AsyncTask<String, Void, String> task = new DownloadPhotos();
        String url = buildUrl("", "");
        //URL url1 = new URL(url);
        task.execute(url, "Downloading photos!");
        Log.e("URL", url);

        return v;
    }

    private void initializeData(){
        mPhoto = new ArrayList<>();
        mPhoto.add(new Photo(R.drawable.cropped_doge));
       mPhoto.add(new Photo(R.drawable.cropped_doge));
        mPhoto.add(new Photo(R.drawable.cropped_doge));
        mPhoto.add(new Photo(R.drawable.cropped_doge));
        mPhoto.add(new Photo(R.drawable.cropped_doge));

    }

    private void initializeAdapter(){
        RecyclerAdapter adapter = new RecyclerAdapter(mPhoto);
       // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        //rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(adapter);
    }

    /**
     * This method helps append the url.
     *@param username of the user.
     *@param password of the user.
     * @return a String url.
     */
    private String buildUrl(String username, String password) {

        StringBuilder sb = new StringBuilder(mLink);
        try {
            sb.append("&apiKey=");
            sb.append(URLEncoder.encode(API_KEY, "UTF-8"));

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


            List<Meme> list = new ArrayList<Meme>();

            Bundle args = new Bundle();
            //args.putString("Random Setlist", result);
            /*
            try {

                //parse in the objects.
                JSONArray obj = new JSONArray(result);
                Meme m = new Meme();
                for(int i =0 ; i < obj.length(); i++)
                {
                    JSONObject object = obj.getJSONObject(i);
                    list.add(Meme.getMeme(object));
                }

                String size = "" + obj.length();
                Log.e("TopTEN ", obj.toString());
                Log.e("LENGTH", size);

                //args.putSerializable("Array", list);

                //mArraysetlistArgs = args;


            }catch (Throwable t){
                Log.e("TAG", "Could not parse malformed JSON: " + t.toString());
            }
*/
            Log.d(TAG, result);


            // Geocoder geocoder;
            //geocoder = new Geocoder(getContext(), Locale.getDefault());

            //Add the trip items to the view
            //mRecyclerView.setAdapter(new MemeBrowserRecyclerViewAdapter(list));

        }
    }

}
