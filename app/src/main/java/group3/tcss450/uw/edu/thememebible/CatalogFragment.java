package group3.tcss450.uw.edu.thememebible;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import group3.tcss450.uw.edu.thememebible.MemeObject.Meme;

public class CatalogFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private static final String TAG = "CATALOGFRAGMENT";
    private static final String mUrl = "http://version1.api.memegenerator.net/Generators_Select_ByPopular?pageIndex=0&pageSize=12&days=12";
    private static final String API_KEY = "7A81A4B0-C434-4DA2-B8D6-1A63E5D63400";

    private ArrayList<Meme> mMemelist;

    private ArrayList<String> mTrendingAutoComplete;

    public CatalogFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

                View v = inflater.inflate(R.layout.fragment_catalog_fragment, container, false);
        AsyncTask<String, Void, String> task = new DownloadData();
        mTrendingAutoComplete = new ArrayList<String>();

        mMemelist = new ArrayList<Meme>();

        //populating my auto complete will clean up later.
        StringBuilder sb = new StringBuilder(mUrl);
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

        task.execute(sb.toString(), "trending");


        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mTrendingAutoComplete);
        AutoCompleteTextView text = (AutoCompleteTextView) v.findViewById(R.id.search_auto_complete);
        text.setAdapter(adapter);



        // add listeners for the buttons and inflate the view for InitialFragment
        Button b = (Button) v.findViewById(R.id.catalog_button1);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.catalog_button2);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.catalog_button3);
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
                case R.id.catalog_button1:
                    mListener.onFragmentInteraction(v.getId());
                    break;
                case R.id.catalog_button2:
                    mListener.onFragmentInteraction(v.getId());
                    break;
                case R.id.catalog_button3:
                    mListener.onFragmentInteraction(v.getId());
                    break;
                case R.id.search_button: //putting in my query.
                    AutoCompleteTextView search = (AutoCompleteTextView) getActivity().findViewById(R.id.search_auto_complete);
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

        void setMemeList(ArrayList<Meme> list);
        void onFragmentInteraction(int buttonID);
        void setSearch(String search);
    }

    /**
     * This asynctask fills my auto complete.
     */
    private class DownloadData extends AsyncTask<String, Void, String> {

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
         * Parse the data into a string array.
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



            try {

                ArrayList<Meme> mMemes = new ArrayList<Meme>();

                //parse in the objects.
                JSONArray obj = new JSONObject(result).getJSONArray("result");
                Meme m = new Meme();
                for(int i =0 ; i < obj.length(); i++)
                {
                    JSONObject object = obj.getJSONObject(i);
                    mMemes.add(Meme.getMeme(object));
                }


                //populating my autocomplete string array.
                for(int i = 0; i < mMemes.size(); i++)
                {
                    mTrendingAutoComplete.add(mMemes.get(i).getmDisplayName());
                    Log.e(TAG, mTrendingAutoComplete.get(i));
                }

                Log.e("holy " , mMemes.toString());



            }catch (Throwable t){
                Log.e("TAG", "Could not parse malformed JSON: " + t.toString());
            }

            Log.d(TAG, result);



        }
    }

}
