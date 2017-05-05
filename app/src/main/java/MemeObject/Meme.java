package MemeObject;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Phansa Chaonpoj on 5/3/17.
 * This is the Meme Object.
 */

public class Meme implements Serializable
{
    private static final String TAG = "MEME CLASS";
    /*generatorID*/
    private int mGeneratorID;
    /*imageID*/
    private int mImageID;
    /*urlName */
    private String mUrlName;
    /*displayName*/
    private String mDisplayName;
    /*totalVotesScore*/
    private int mTotalVotesScore;
    /*instancesCount*/
    private int mInstanceCount;
    /*ranking*/
    private int mRanking;

    /*imageUrl*/
    private String mUrl;
    /*entityName*/
    private String mEntityName;
    /*entityID*/
    private int mEntityID;
    /*TotalVotesSum*/
    private int mTotalVotesSum;
    /*userID*/
    private int mUserID;
    /*userVoteScore*/
    private int mUserVoteScore;

    /* Empty Constructor */
    public Meme(){}

    /**
     *@param meme JSONObject of the meme
     */
    public static Meme getMeme(final JSONObject meme)
    {
        Meme m = new Meme();

        try {

            m.mGeneratorID = meme.getInt("generatorID");
            m.mImageID = meme.getInt("imageID");
            m.mDisplayName = meme.getString("displayName");
            m.mUrlName = meme.getString("urlName");
            m.mTotalVotesScore = meme.getInt("totalVotesScore");
            m.mInstanceCount = meme.getInt("instancesCount");
            m.mRanking = meme.getInt("ranking");
            //this is another object that contatins other information.
            m.mUrl = meme.getString("imageUrl");

            //parse through the object so that it shows to entityVotesSummary
            JSONObject entity = meme.getJSONObject("entityVotesSummary");

            m.mEntityName = entity.getString("entityName");
            m.mEntityID = entity.getInt("entityID");
            m.mTotalVotesSum = entity.getInt("totalVotesSum");
            m.mUserID = entity.getInt("userID");
            m.mUserVoteScore = entity.getInt("userVoteScore");





        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }


        return m;


    }
    public int getmGeneratorID() {
        return mGeneratorID;
    }

    public int getmImageID() {
        return mImageID;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public int getmTotalVotesScore() {
        return mTotalVotesScore;
    }

    public int getmInstanceCount() {
        return mInstanceCount;
    }

    public int getmRanking() {
        return mRanking;
    }
    public String getmEntityName() {
        return mEntityName;
    }

    public int getmEntityID() {
        return mEntityID;
    }

    public int getmTotalVotesSum() {
        return mTotalVotesSum;
    }

    public int getmUserID() {
        return mUserID;
    }

    public int getmUserVoteScore() {
        return mUserVoteScore;
    }


    /** *
     * for testing purposes overriding the toString for this object.
     * @return String that has information about the meme.
     */
    @Override
    public String toString(){
        String meme = "";


        meme += "Display name: " + mDisplayName + ", ";
        meme += "imageID: " + mImageID + ", ";
        meme += "ranking:" + mRanking + ", ";
        meme += "entity name :" + mEntityName + ", ";
        meme += "entity id :" + mEntityID + ", ";
        meme += "url:" + mUrl + "\n";
        return meme;
    }


    public String getmUrl() {
        return mUrl;
    }


}
