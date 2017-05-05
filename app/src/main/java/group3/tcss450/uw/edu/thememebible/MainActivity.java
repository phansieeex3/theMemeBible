package group3.tcss450.uw.edu.thememebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements InitialFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener,
        DisplayFragment.OnFragmentInteractionListener, MainMenuFragment.OnFragmentInteractionListener, CatalogFragment.OnFragmentInteractionListener


{
    private static final String TAG = "MainActivity";
    protected static final String PARTIAL_URL = "http://thememebible.ddns.net/app/app_";
    private Bundle mLoginArgs;
    private Bundle mRegisterArgs;
    private RecyclerView mRecyclerView;
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



    // implements all button press callbacks from other Fragments
    @Override
    public void onFragmentInteraction(int buttonID) {

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
                Toast.makeText(this, "Thanks for registering!", Toast.LENGTH_SHORT).show();
                loadFragment(new MainMenuFragment());
                break;

            case R.id.meme_catalog: // from MainMenuFragment
                loadFragment(new CatalogFragment());
                break;

            case R.id.my_meme: // from MainMenuFragment
                loadFragment(new CatalogFragment());
                break;

            case R.id.catalog_button1: // from CatalogFragment
                loadFragment(new PhotoFragment());

                //send type of  catalog string.
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


}
