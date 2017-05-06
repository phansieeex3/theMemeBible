package group3.tcss450.uw.edu.thememebible.Model;

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
    private static final String TAG = "Meme Class";
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
    private int mInstancesCount;
    /*ranking*/
    private int mRanking;

    /*imageUrl*/
    private String mImageUrl;

    private JSONObject mEntityVotesSummary;

    /*entityName*/
    private String mEntityName;
    /*entityID*/
    private int mEntityID;
    /*TotalVotesSum*/
    private int mTotalVotesSum;
    /*userID*/
    private String mUserID;
    /*userVoteScore*/
    private String mUserVoteScore;

    private int mInstanceID;
    private String mText0;
    private String mText1;
    private int mCommentsCount;
    private String mMgUserID;
    private String mUsername;
    private String mInstanceImageUrl; // has the caption
    private String mInstanceUrl;

    /* Empty Constructor */
    public Meme(){}

    /**
     *@param meme JSONObject of the meme
     */
    public static Meme getMeme(final JSONObject meme)
    {
        Meme m = new Meme();

        try {

            if (meme.has("generatorID"))
                m.mGeneratorID = meme.getInt("generatorID");
            if (meme.has("imageID"))
                m.mImageID = meme.getInt("imageID");
            if (meme.has("displayName"))
                m.mDisplayName = meme.getString("displayName");
            if (meme.has("urlName"))
                m.mUrlName = meme.getString("urlName");
            if (meme.has("totalVotesScore"))
                m.mTotalVotesScore = meme.getInt("totalVotesScore");
            if (meme.has("instancesCount"))
                m.mInstancesCount = meme.getInt("instancesCount");
            if (meme.has("ranking"))
                m.mRanking = meme.getInt("ranking");
            if (meme.has("imageUrl"))
                m.mImageUrl = meme.getString("imageUrl");

            // handle entity votes summary JSONObject separately
            if (meme.has("entityVotesSummary")) {
                m.mEntityVotesSummary = meme.getJSONObject("entityVotesSummary");

                if (m.mEntityVotesSummary.has("entityName"))
                    m.mEntityName = m.mEntityVotesSummary.getString("entityName");
                if (m.mEntityVotesSummary.has("entityID"))
                    m.mEntityID = m.mEntityVotesSummary.getInt("entityID");
                if (m.mEntityVotesSummary.has("totalVotesSum"))
                    m.mTotalVotesSum = m.mEntityVotesSummary.getInt("totalVotesSum");
                if (m.mEntityVotesSummary.has("userID"))
                    m.mUserID = m.mEntityVotesSummary.getString("userID");
                if (m.mEntityVotesSummary.has("userVoteScore"))
                    m.mUserVoteScore = m.mEntityVotesSummary.getString("userVoteScore");
            }

            // additional fields for instances search
            if (meme.has("instanceID"))
                m.mInstanceID = meme.getInt("instanceID");
            if (meme.has("text0"))
                m.mText0 = meme.getString("text0");
            if (meme.has("text1"))
                m.mText1 = meme.getString("text1");
            if (meme.has("commentsCount"))
                m.mCommentsCount = meme.getInt("commentsCount");
            if (meme.has("mgUserID"))
                m.mMgUserID = meme.getString("mgUserID");
            if (meme.has("username"))
                m.mUsername = meme.getString("username");
            if (meme.has("instanceImageUrl"))
                m.mInstanceImageUrl = meme.getString("instanceImageUrl");
            if (meme.has("instanceUrl"))
                m.mInstanceUrl = meme.getString("instanceUrl");

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }


        return m;
    }

    public String getmUrlName() {
        return mUrlName;
    }

    public JSONObject getmEntityVotesSummary() {
        return mEntityVotesSummary;
    }

    public int getmInstanceID() {
        return mInstanceID;
    }

    public String getmText0() {
        return mText0;
    }

    public String getmText1() {
        return mText1;
    }

    public int getmCommentsCount() {
        return mCommentsCount;
    }

    public String getmMgUserID() {
        return mMgUserID;
    }

    public String getmUsername() {
        return mUsername;
    }

    public String getmInstanceImageUrl() {
        return mInstanceImageUrl;
    }

    public String getmInstanceUrl() {
        return mInstanceUrl;
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

    public int getmInstancesCount() {
        return mInstancesCount;
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

    public String getmUserID() {
        return mUserID;
    }

    public String getmUserVoteScore() {
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
        meme += "url:" + mImageUrl + "\n";
        return meme;
    }


    public String getmImageUrl() {
        return mImageUrl;
    }


}
