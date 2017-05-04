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
    /*entityVotesSummary*/
    private EntityVoteSummary mEntityVotesSummary;
    /*imageUrl*/
    private String mUrl;

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
           // m.mEntityVotesSummary = new EntityVoteSummary();





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

    public EntityVoteSummary getmEntityVotesSummary() {
        return mEntityVotesSummary;
    }





    public String getmUrl() {
        return mUrl;
    }

    /* Another inner object that holds the entityVoteSummary*/
    public class EntityVoteSummary implements Serializable{

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

        public  EntityVoteSummary getEntityVoteSummary(final JSONObject meme){
            EntityVoteSummary es = new EntityVoteSummary();

            try{

                es.mEntityName = meme.getString("entityName");
                es.mEntityID = meme.getInt("entityID");
                es.mTotalVotesSum = meme.getInt("totalVotesSum");
                es.mUserID = meme.getInt("userID");
                es.mUserVoteScore = meme.getInt("userVoteScore");

            }catch(JSONException e){

                Log.e(TAG, e.getMessage());
            }

            return es;
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
    }
}
