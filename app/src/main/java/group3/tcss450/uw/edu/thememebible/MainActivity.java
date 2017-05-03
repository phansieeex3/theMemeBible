package group3.tcss450.uw.edu.thememebible;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements InitialFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener,
        DisplayFragment.OnFragmentInteractionListener, MainMenu.OnFragmentInteractionListener, Catalog_fragment.OnFragmentInteractionListener


{
    private static final String TAG = "MainActivity";
    protected static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~vhoang90/webservice/";
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
        FragmentTransaction transaction;
        DisplayFragment display;
        MainMenu menu;
        Catalog_fragment catalog;

        switch(buttonID) {
            case R.id.btnLogin:
                Log.i(TAG, "btnLogin buttonID");
                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new LoginFragment())
                        .addToBackStack(null);
                transaction.commit();
                break;

            case R.id.btnRegister:
                Log.i(TAG, "btnRegister buttonID");
                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new RegisterFragment())
                        .addToBackStack(null);
                transaction.commit();
                break;

            // redundant but oh well
            case R.id.btnOK:
                Log.i(TAG, "btnOK buttonID");

                display = new DisplayFragment();
                menu = new MainMenu();
                menu.setArguments(mLoginArgs);


                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, menu)
                        .addToBackStack(null);
                transaction.commit();
                break;

            // redundant but oh well
            case R.id.btnRegisterUser:
                Log.i(TAG, "btnRegister buttonID");

                display = new DisplayFragment();
                display.setArguments(mRegisterArgs);

                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, display)
                        .addToBackStack(null);
                transaction.commit();
                break;
            case R.id.meme_catalog:
                Log.i(TAG, "Meme Catalog id");
                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new Catalog_fragment())
                        .addToBackStack(null);
                transaction.commit();
                break;
            case R.id.my_meme:
                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new Catalog_fragment())
                        .addToBackStack(null);
                transaction.commit();
                break;
            case R.id.catalog_button1:
                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new Photo_fragment())
                        .addToBackStack(null);
                transaction.commit();
                break;

        }
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
