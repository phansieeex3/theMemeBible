package group3.tcss450.uw.edu.thememebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import group3.tcss450.uw.edu.thememebible.Model.Meme;
import group3.tcss450.uw.edu.thememebible.Utility.DownloadData;

/**
 * The main entry point for the application.
 */
public class MainActivity extends AppCompatActivity implements InitialFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener,
        MainMenuFragment.OnFragmentInteractionListener, CatalogFragment.OnFragmentInteractionListener


{
    private static final String TAG = "MainActivity";
    protected static final String PARTIAL_URL = "http://thememebible.ddns.net/app/app_";
    protected static final String SEARCH_API = "http://version1.api.memegenerator.net/Instances_Search?&pageIndex=0&pageSize=12";
    protected static final String POPULAR_API = "http://version1.api.memegenerator.net/Generators_Select_ByPopular?pageIndex=0&pageSize=12&days=";
    private Bundle mLoginArgs;
    private Bundle mRegisterArgs;
    private String mSearch;
    private RecyclerView mRecyclerView;
    private ArrayList<Meme> mMemeList;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Photo> mPhotosList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate InitialFragment
        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new InitialFragment())
                        .commit();
            }
        }
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mPhotosList.size() == 0) {
//            requestPhoto();
//        }
//
//    }


    @Override
    public void setMemeList(ArrayList<Meme> list) {
        mMemeList = new ArrayList<Meme>(list);
    }

    // implements all button press callbacks from other Fragments
    @Override
    public void onFragmentInteraction(int buttonID) {

        DownloadData data;
        PhotoFragment f;
        ArrayList<Meme> memeList = new ArrayList<Meme>();
        // handle different buttonIDs
        switch(buttonID) {
            case R.id.btnLogin: // from InitialFragment
                loadFragment(new LoginFragment());
                break;

            case R.id.btnRegister: // from InitialFragment
                loadFragment(new RegisterFragment());
                break;

            case R.id.btnOK: // from LoginFragment
                loadFragment(new MainMenuFragment());
                break;

            case R.id.btnRegisterUser: // from RegisterFragment
                Toast.makeText(this, "Thanks for registering!", Toast.LENGTH_SHORT).show();//cute
                loadFragment(new MainMenuFragment());
                break;

            case R.id.meme_catalog: // from MainMenuFragment
                loadFragment(new CatalogFragment());
                break;

            case R.id.my_meme: // from MainMenuFragment
                loadFragment(new CatalogFragment());
                break;

            case R.id.catalog_button1: // from CatalogFragment
                f = new PhotoFragment();
                f.setmLink(POPULAR_API);
                loadFragment(f); //should be able to pass some strings.

                //send type of  catalog string.
                break;

            case R.id.search_button:

                //set async task DownloadData does not work.
                data = new DownloadData(SEARCH_API);
                data.setmQuery(mSearch);
                //call from task - getListofMemes
                memeList = data.getmMemes();
                f = new PhotoFragment();
                f.setmLink(SEARCH_API);
                f.setmQuery(mSearch);
                //pass into Photofragment to parse and display!
                loadFragment(f);
                break;


        }
    }

    /**
     * Helper method to switch out fragments.
     *
     * @param theFragment the fragment to be loaded
     */
    private void loadFragment(Fragment theFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, theFragment)
                .addToBackStack(null);
        transaction.commit();
    }

    // callback for DisplayFragment data
    @Override
    public void getRegistrationInformation(Bundle args) {
        mRegisterArgs = args;
    }

    // callback for LoginFragment data
    @Override
    public void getLogin(Bundle args) {
        mLoginArgs = args;
    }

    //callback for our CatalogFragment.

    /**
     * Sets the query for our search.
     * @param args
     */
    @Override
    public void setSearch(String args)
    {
        mSearch = args;
    }


}
