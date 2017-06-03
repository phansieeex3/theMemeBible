package group3.tcss450.uw.edu.thememebible.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * The Meme object data model.
 *
 * @author Phansa Chaonpoj
 * @author Peter Phe
 * @version 1.0
 */
public class Meme implements Serializable
{
    private static final String TAG = "Meme Class";

    private int mGeneratorID;
    private int mImageID;
    private String mUrlName;
    private String mDisplayName;
    private int mTotalVotesScore;
    private int mInstancesCount;
    private int mRanking;
    private String mImageUrl;
    private String mEntityName;
    private int mEntityID;
    private int mTotalVotesSum;
    private String mUserID;
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
    public Meme() { }

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
                // temporary JSONObject just to grab data. JSONObject not serializable.
                JSONObject evSummary = meme.getJSONObject("entityVotesSummary");

                if (evSummary.has("entityName"))
                    m.mEntityName = evSummary.getString("entityName");
                if (evSummary.has("entityID"))
                    m.mEntityID = evSummary.getInt("entityID");
                if (evSummary.has("totalVotesSum"))
                    m.mTotalVotesSum = evSummary.getInt("totalVotesSum");
                if (evSummary.has("userID"))
                    m.mUserID = evSummary.getString("userID");
                if (evSummary.has("userVoteScore"))
                    m.mUserVoteScore = evSummary.getString("userVoteScore");
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

    public int getmGeneratorID() {
        return mGeneratorID;
    }

    public int getmImageID() {
        return mImageID;
    }

    public String getmUrlName() {
        return mUrlName;
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

    public String getmImageUrl() {
        return mImageUrl;
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
}
