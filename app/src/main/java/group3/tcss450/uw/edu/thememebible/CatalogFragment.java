package group3.tcss450.uw.edu.thememebible;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import group3.tcss450.uw.edu.thememebible.Model.Meme;
import group3.tcss450.uw.edu.thememebible.Utility.UrlBuilder;

/**
 * The catalog fragment for selecting different categories.
 *
 * @author Vu Hoang
 * @author Phansa Chaonpoj
 * @author Peter Phe
 * @version 1.0
 */
public class CatalogFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CatalogFragment";

    private Bundle mSaveInstanceState;
    private OnFragmentInteractionListener mListener;
    private ArrayList<String> mTrendingAutoComplete;
    private ArrayAdapter<String> mStringAdapter;

    public CatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrendingAutoComplete = new ArrayList<>();
        mStringAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, mTrendingAutoComplete);
        mSaveInstanceState = new Bundle();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSaveInstanceState.putSerializable("actv", mTrendingAutoComplete);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_catalog_fragment, container, false);

        // for populating AutoCompleteTextView
        if (!mSaveInstanceState.isEmpty()) {
            mTrendingAutoComplete = (ArrayList<String>) mSaveInstanceState.getSerializable("actv");
        } else {
            AsyncTask<String, Void, String> task = new DownloadData();
            task.execute(UrlBuilder.getGeneratorsSelectByPopularUrl());
        }

        AutoCompleteTextView text = (AutoCompleteTextView) v.findViewById(R.id.search_auto_complete);
        text.setAdapter(mStringAdapter);

        // add listeners for the buttons and inflate the view for InitialFragment
        Button b = (Button) v.findViewById(R.id.popular_button);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.trending_button);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.recently_used);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.search_button);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.popular_button:
                    mListener.onFragmentInteraction(v.getId());
                    break;
                case R.id.trending_button:
                    mListener.onFragmentInteraction(v.getId());
                    break;
                case R.id.recently_used:
                    mListener.onFragmentInteraction(v.getId());
                    break;
                case R.id.search_button: //putting in my query.
                    AutoCompleteTextView search = (AutoCompleteTextView) getActivity()
                            .findViewById(R.id.search_auto_complete);
                    String query = search.getText().toString();
                    mListener.setSearch(query);
                    mListener.onFragmentInteraction(v.getId());
                    break;

            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int buttonID);
        void setSearch(String search);
    }

    /**
     * This asynctask fills my auto complete.
     */
    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

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

            // encountered network issue or issue with URL
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
                return;
            }

            try {

                // parse in the objects.
                JSONArray array = new JSONObject(result).getJSONArray("result");

                //populate autocomplete string array
                for (int i = 0 ; i < array.length(); i++)
                    mTrendingAutoComplete.add(Meme.getMeme(array.getJSONObject(i)).getmDisplayName());

            } catch (JSONException e) {
                Log.e("TAG", "Could not parse malformed JSON: " + e.getMessage());
            }

            // notify data changed
            mStringAdapter.notifyDataSetChanged();
        }
    }
}
