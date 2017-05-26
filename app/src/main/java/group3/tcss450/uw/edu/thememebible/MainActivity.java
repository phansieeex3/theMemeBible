package group3.tcss450.uw.edu.thememebible;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import group3.tcss450.uw.edu.thememebible.Model.Meme;
import group3.tcss450.uw.edu.thememebible.Utility.MemeDataTask;
import group3.tcss450.uw.edu.thememebible.Utility.UrlBuilder;

/**
 * The main entry point for the application.
 *
 * @author Peter Phe
 * @author Phansa Chaonpoj
 * @author Vu Hoang
 */
public class MainActivity extends AppCompatActivity implements InitialFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener,
        MainMenuFragment.OnFragmentInteractionListener, CatalogFragment.OnFragmentInteractionListener,
        MemeDataTask.OnTaskComplete, LoadingFragment.DisplayInterface, CaptionFragment.OnFragmentInteractionListener,
        ShareFragment.OnFragmentInteractionListener, PhotoFragment.OnPhotofragmentInteractionListener
{

    private static final String TAG = "MainActivity";
    protected static final String PARTIAL_URL = "http://thememebible.ddns.net/app/app_"; // for backend
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL = 1001;
    private Bundle mLoginArgs;
    private Bundle mRegisterArgs;
    private Bundle mShareArgs;
    private ArrayList<Meme> mMemeData;
    private PhotoFragment mPhotoFragment;
    private LoadingFragment mLoadingFragment;
    private String mSearch;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoFragment = new PhotoFragment();
        mLoadingFragment = new LoadingFragment();
        mSearch = "";

        // request permission for storage here.
        // re-check in ShareFragment fragment for where to store images (external vs internal)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL);
        }

        // instantiate InitialFragment
        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new MainMenuFragment())//new InitialFragment())
                        .commit();
            }
        }
    }

    // implements all button press callbacks from other Fragments
    @Override
    public void onFragmentInteraction(int buttonID) {
        AsyncTask<String, Void, String> task;

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
                openGallery();
                break;

            case R.id.popular_button: // from CatalogFragment
                displayProgressBar();
                task = new MemeDataTask(getApplicationContext(), this);
                task.execute(UrlBuilder.getGeneratorsSelectByPopularUrl());
                break;

            case R.id.search_button:
                displayProgressBar();
                task = new MemeDataTask(getApplicationContext(), this);
                task.execute(UrlBuilder.getGeneratorSearchUrl(mSearch)); // mSearch set in CatalogFragment
                break;

            case R.id.catalog_button2:
                break;

            case R.id.catalog_button3:
                loadFragment(new ShareFragment()); // Testing UI for ShareFragment
                break;
            case R.id.done_button:
                displayProgressBar();
                task = new MemeDataTask(getApplicationContext(), this);
                task.execute(UrlBuilder.getInstanceCreateUrl(mShareArgs.getInt("genId"),
                        mShareArgs.getInt("imageId"), mShareArgs.getString("topText"), mShareArgs.getString("botText")));


                break;
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }
    @Override
    protected void onActivityResult(int requestCode , int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri  = data.getData();
            ImageView img = (ImageView)findViewById(R.id.item_image);
            img.setImageURI(imageUri);
//            img.setImageDrawable(drawable);
//            loadFragment(new CaptionFragment(img));

        }
    }

    @Override
    //callback for captionFragment
    public void setShareArgs(Bundle args) { mShareArgs = args;}

    // callback for search button from CatalogFragment
    @Override
    public void setSearch(String theSearch) {
        mSearch = theSearch;
    }

    // callback for Registration data
    @Override
    public void getRegistrationInformation(Bundle args) {
        mRegisterArgs = args;
    }

    // callback for Login data
    @Override
    public void getLogin(Bundle args) {
        mLoginArgs = args;
    }

    // callback for MemeDataTask AsyncTask data
    @Override
    public void onTaskComplete(ArrayList<Meme> theMemeData) {
        // dismiss the progress bar
        dismissProgressBar();

        // grab data from MemeDataTask AsyncTask
        mMemeData = theMemeData;

        if (!mMemeData.isEmpty()) {
            // package data for photofragment
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.photo_data_key), mMemeData);
            mPhotoFragment.setArguments(args);

            // load fragment
            loadFragment(mPhotoFragment);
        }
    }

    @Override
    public void onTaskCompleteCreate(Meme theMeme) {
        dismissProgressBar();

        if(theMeme != null)
        {
            Bundle b = new Bundle();
            b.putSerializable("meme", theMeme);
            ShareFragment sf = new ShareFragment();
            sf.setArguments(b);
            loadFragment(sf);

        }
    }

    // LoadingFragment interface methods
    @Override
    public void displayProgressBar() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, mLoadingFragment)
                .commit();
    }

    @Override
    public void dismissProgressBar() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(mLoadingFragment)
                .commit();
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

    @Override
    public void onPhotofragmentInteractionListener(Drawable draw, Meme m) {

        CaptionFragment cf = new CaptionFragment(draw);
        Bundle b = new Bundle();
        b.putSerializable("meme", m);
        Bitmap bit = ((BitmapDrawable)draw).getBitmap();
       // Bit
        if(m == null) Log.e(TAG, "Meme is null");
        //b.putSerializable("drawable", bit);
        cf.setArguments(b);
        loadFragment(cf);


    }



}
