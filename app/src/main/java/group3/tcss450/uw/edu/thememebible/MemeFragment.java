package group3.tcss450.uw.edu.thememebible;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.json.JSONException;
import org.json.JSONObject;

import MemeObject.Meme;
import Model.MemeBrowserRecyclerViewAdapter;
import group3.tcss450.uw.edu.thememebible.dummy.DummyContent;
import group3.tcss450.uw.edu.thememebible.dummy.DummyContent.DummyItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MemeFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String TAG = "MEME FRAGMENT";
    private static final String API_KEY = "7A81A4B0-C434-4DA2-B8D6-1A63E5D63400";
    //hardcode for now.
    private String mUser = "yellowbears";
    private String mPass = "nomnomnom2";
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 3;
    private OnListFragmentInteractionListener mListener;

    /**different links for different searches.*/
    private String mLink = "http://version1.api.memegenerator.net/Generators_Select_ByNew?pageIndex=0&pageSize=12&apiKey=demo";




    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MemeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MemeFragment newInstance(int columnCount) {
        MemeFragment fragment = new MemeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meme_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyMemeRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    /**
     * an Async class that retrieves data from the database.
     */
    private class DownloadTripsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
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
                    response = "Unable to download the list of trips, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
            }
            return response;
        }

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
                Log.e("My app", "Could not parse malformed JSON: " + t.toString());
            }

            Log.d(TAG, result);


           // Geocoder geocoder;
            //geocoder = new Geocoder(getContext(), Locale.getDefault());

            //Add the trip items to the view
            //mRecyclerView.setAdapter(new MemeBrowserRecyclerViewAdapter(list));

        }
    }
}
