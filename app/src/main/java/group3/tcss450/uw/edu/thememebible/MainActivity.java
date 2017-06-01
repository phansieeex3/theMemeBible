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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private Meme mMeme;
    private Drawable mDrawable;
    private static final int PICK_IMAGE = 20;
    private Uri mImageUri;
    private Bitmap mBitmap;
    private String mQueryType;

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
                Toast.makeText(this, "Thanks for registering!", Toast.LENGTH_SHORT).show();
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
                mQueryType = "popular";
                task = new MemeDataTask(getApplicationContext(), this);
                task.execute(UrlBuilder.getGeneratorsSelectByPopularUrl());
                break;

            case R.id.search_button:
                displayProgressBar();
                mQueryType = "search";
                task = new MemeDataTask(getApplicationContext(), this);
                task.execute(UrlBuilder.getGeneratorSearchUrl(mSearch)); // mSearch set in CatalogFragment
                break;

            case R.id.trending_button:
                displayProgressBar();
                mQueryType = "trending";
                task = new MemeDataTask(getApplicationContext(), this);
                task.execute(UrlBuilder.getGeneratorsSelectByTrendingUrl());
                break;

            case R.id.recently_used:
                displayProgressBar();
                mQueryType = "recent";
                task = new MemeDataTask(getApplicationContext(), this);
                task.execute(UrlBuilder.getGeneratorsSelectByRecentlyCaptionedUrl());
                break;

            case R.id.done_button:
                displayProgressBar();
                task = new MemeDataTask(getApplicationContext(), this);
                task.execute(UrlBuilder.getInstanceCreateUrl(mShareArgs.getInt("genId"),
                        mShareArgs.getInt("imageId"), mShareArgs.getString("topText"), mShareArgs.getString("botText")));
                break;

            case R.id.main_menu:
                // pops backstack all the way back to the main menu
                getSupportFragmentManager().popBackStackImmediate(MainMenuFragment.class.getSimpleName(), 0);
                Log.e(TAG, "main menu clicked - functions correctly once we put LoginFragment back");
                break;
        }
    }

    /**
     * Helper method to open the gallery of saved memes for selection.
     */
    private  void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Open image from...");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { pickIntent });

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    /**
     * Handles the results of the Gallery intent when viewing saved memes via My Memes.
     */
    @Override
    protected void onActivityResult(int requestCode , int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            mImageUri  = data.getData();

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);

                ShareFragment sf = new ShareFragment();

                // prep arguments
                if (mBitmap != null) {
                    Bundle args = new Bundle();
                    args.putByteArray(getString(R.string.my_meme_bitmap_key), bitmapToByteArray(mBitmap));
                    sf.setArguments(args);
                }

                loadFragment(sf);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    //callback for captionFragment
    public void setShareArgs(Bundle args) {
        mShareArgs = args;
    }

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

            // for endless scrolling feature in PhotoFragment
            args.putString(getString(R.string.query_type_key), mQueryType);
            if (mSearch.length() > 0)
                args.putString(getString(R.string.query_string_key), mSearch);

            // set bundle as argument
            mPhotoFragment.setArguments(args);

            // load fragment
            loadFragment(mPhotoFragment);
        }
    }

    @Override
    public void onTaskCompleteCreate(Meme theMeme) {
        dismissProgressBar();

        // R.string.my_meme_bitmap_key
        //

        if (theMeme != null) {
            mShareArgs.putSerializable(getString(R.string.captioned_meme_key), theMeme);
            ShareFragment sf = new ShareFragment();
            sf.setArguments(mShareArgs);
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
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentContainer, theFragment)
                .addToBackStack(theFragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void setDrawableArgs(Drawable d, Meme m) {
        mDrawable = d;
        mMeme = m;
    }

    @Override // callback from PhotoFragment - frees onPostExecute() from drawing inserted views (unblocks UI thread)
    public void updateRecyclerAdapterData(final RecyclerAdapter theAdapter, final int start, final int end) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                theAdapter.notifyItemRangeInserted(start, end);
            }
        });
    }

    @Override
    public void onPhotofragmentInteractionListener() {
        // prep arguments
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.meme_obj_to_caption_key), mMeme);
        Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
        args.putByteArray(getString(R.string.drawable_to_caption_key), bitmapToByteArray(bitmap));

        // load fragment
        CaptionFragment cf = new CaptionFragment();
        cf.setArguments(args);
        loadFragment(cf);
    }

    /**
     * Helper method to convert a Bitmap to a Byte Array for passing as argument to a fragment.
     *
     * @param theBitmap the bitmap to convert
     * @return byte array of the bitmap
     */
    private byte[] bitmapToByteArray(Bitmap theBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        theBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
