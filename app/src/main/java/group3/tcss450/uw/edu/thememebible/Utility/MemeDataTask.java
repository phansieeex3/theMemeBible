package group3.tcss450.uw.edu.thememebible.Utility;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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

        Log.e(TAG, strings[0]);

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

        ArrayList<Meme> memeList = new ArrayList<>();

        // encountered network issue or issue with URL
        if (result.startsWith("Unable to")) {
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            // if API is down, could try to grab from a homebrewed API service here
        } else {

            try {

                Object json = new JSONObject(result).get("result");

                if (json instanceof JSONObject) {
                    // parse response data
                    JSONArray jsonArray = new JSONObject(result).getJSONArray("result");

                    if(jsonArray.length() == 1)
                    {
                        Log.e(TAG, result);
                        mListener.onTaskCompleteCreate(Meme.getMeme(jsonArray.getJSONObject(0)));
                    }
                    else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            memeList.add(Meme.getMeme(jsonObject));
                        }
                        mListener.onTaskComplete(memeList);
                    }


                    // if we weren't able to populate memeList, notify user to try again
                    if (memeList.isEmpty()) {
                        Toast.makeText(mContext, "No memes returned. Try again?", Toast.LENGTH_LONG).show();
                    }

                    // make callback to pass data back to the implementing class


                }
                /*
                else if (json instanceof JSONObject){

                    JSONObject j = new JSONObject(result);
                    Meme m = Meme.getMeme(j);
                    mListener.onTaskCompleteCreate(m);
                    //you have an object



                }*/

                /*
                    //you have an array

                if(new JSONObject(result).getJSONObject("result") != JSONObject.NULL)
                {

                    JSONObject json = new JSONObject(result);
                    Meme m = Meme.getMeme(json);
                    mListener.onTaskCompleteCreate(m);

                }
                else // we know it's an array
                    {

                        // parse response data
                        JSONArray jsonArray = new JSONObject(result).getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            memeList.add(Meme.getMeme(jsonObject));
                        }

                        // if we weren't able to populate memeList, notify user to try again
                        if (memeList.isEmpty()) {
                            Toast.makeText(mContext, "No memes returned. Try again?", Toast.LENGTH_LONG).show();
                        }

                        // make callback to pass data back to the implementing class
                        mListener.onTaskComplete(memeList);

*/

                //}
            } catch (JSONException e) {
                Log.e(TAG, "Could not parse malformed JSON: " + e.getMessage() + result);
            }
        }


    }

    /**
     * A simple callback interface for returning the data to the implementing Fragment/class.
     */
    public interface OnTaskComplete {
        void onTaskComplete(ArrayList<Meme> theMemeData);
        void onTaskCompleteCreate(Meme m);
    }
}
