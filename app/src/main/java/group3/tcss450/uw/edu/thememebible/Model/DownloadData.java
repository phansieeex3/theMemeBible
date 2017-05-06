package group3.tcss450.uw.edu.thememebible.Model;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import group3.tcss450.uw.edu.thememebible.MemeObject.Meme;

/**
 * @author Phansa Chaonpoj
 * This class was created to avoid redundancies,
 * parses through the json array from the given api link.
 * Use getmMemes for the parsed List objects.
 *
 */

public class DownloadData extends  AsyncTask<String, Void, String> {
    /**list of memes from API */
    private ArrayList<Meme> mMemes;
    /** Link for the api.*/
    private String mLink;
    /**API KEY */
    private static final String API_KEY = "7A81A4B0-C434-4DA2-B8D6-1A63E5D63400";
    /**username */
    private String mUsername;
    /**password */
    private String mPassword;
    /**TAG */
    private static final String TAG = "DownloadData";



    /** Default constructor for data that does not need username and password.*/
    public DownloadData(String link){
        this("", "", link);

    }

    /**Overriden constructor.
     * @param username
     * @param  password
     * @param link link of API*/
    public DownloadData(String username, String password, String link){
        mUsername = username;
        mPassword = password;
        mMemes = new ArrayList<Meme>();
        mLink = link;


    }
    /**
     * This method helps append the url.
     *@param username of the user.
     *@param password of the user.
     * @return a String url.
     */
    public String buildUrl(String username, String password, String command) {

        StringBuilder sb = new StringBuilder(mLink);
        try {
            sb.append("&apiKey=");
            sb.append(URLEncoder.encode(API_KEY, "UTF-8"));

           // sb.append(command);
            //sb.append(URLEncoder.encode(API_KEY, "UTF-8"));

            Log.i("buildString", sb.toString());

        } catch (Exception e) {
            Log.e("Catch", e.getMessage());
        }
        return sb.toString();
    }

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
     *
     *
     * @param result String
     */
    @Override
    protected void onPostExecute(String result) {
        Log.i(TAG, result);
        // Something wrong with the network or the URL.
        if (result.startsWith("Unable to")) {

            Log.e("cannot parse string", result);
            return;
        }



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



            //Log.e("holy " , mMemes.toString());
            ArrayList<Meme> test = new ArrayList<>(mMemes);
            Log.e("TESTTT" , test.toString());



        }catch (Throwable t){
            Log.e("TAG", "Could not parse malformed JSON: " + t.toString());
        }

        Log.d(TAG, result);




    }




    /**@returns the list of memes from API source..
     *  */
    public ArrayList<Meme> getmMemes() {
        return new ArrayList<>(mMemes);
    }
}
