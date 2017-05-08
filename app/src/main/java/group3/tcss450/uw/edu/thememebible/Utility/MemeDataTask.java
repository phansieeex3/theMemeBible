package group3.tcss450.uw.edu.thememebible.Utility;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

/**
 * A utility class for retrieving JSON objects as a List of Meme objects
 * based on the URL passed in.
 *
 * @author Peter Phe
 * @version 1.0
 *
 */
public class MemeDataTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "MemeDataTask";
    private Context mContext;
    private OnTaskComplete mListener;

    /**
     * Parameterized constructor for retrieving data from the specified Url.
     *
     * @param theListener listening class that implements the callback interface
     */
    public MemeDataTask(Context theContext, OnTaskComplete theListener) {
        super();
        mContext = theContext;
        mListener = theListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // encountered network issue or issue with URL
        if (result.startsWith("Unable to")) {
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<Meme> memeList = new ArrayList<>();

        try {
            // parse response data
            JSONArray jsonArray = new JSONObject(result).getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                memeList.add(Meme.getMeme(jsonObject));
            }

            // if we weren't able to populate memeList, notify user to try again
            if (memeList.isEmpty()) {
                Toast.makeText(mContext, "No memes returned. Try again?", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Could not parse malformed JSON: " + e.getMessage());
        }

        // make callback to pass data back to the implementing class
        mListener.onTaskComplete(memeList);
    }

    /**
     * A simple callback interface for returning the data to the implementing Fragment/class.
     */
    public interface OnTaskComplete {
        void onTaskComplete(ArrayList<Meme> theMemeData);
    }
}
