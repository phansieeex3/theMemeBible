package group3.tcss450.uw.edu.thememebible;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import group3.tcss450.uw.edu.thememebible.Model.Meme;

/**
 * Save or share memes that have been captioned by the user.
 *
 * @author Peter Phe
 */
public class ShareFragment extends Fragment {

    private static final String TAG = "ShareFragment";
    public static final String MEME_DIRECTORY = "/Memes";
    private Button mSaveButton;
    private ImageView mMemeImage;
    private Meme mMeme;
    private String mFilename;
    private OnFragmentInteractionListener mListener;
    private Bitmap mBitmap;

    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // from MainActivity (data coming in as Bitmap initiated by My Meme's openGallery())
            if (getArguments().containsKey(getString(R.string.my_meme_bitmap_key))) {
                byte[] ba = getArguments().getByteArray(getString(R.string.my_meme_bitmap_key));
                if (ba != null)
                    mBitmap = BitmapFactory.decodeByteArray(ba, 0, ba.length);
                else
                    mBitmap = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // enable/disable the Save button_enabled based on Storage permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            mSaveButton.setEnabled(false);
        } else {
            mSaveButton.setEnabled(true);
        }

        if (mBitmap != null) {
            mSaveButton.setEnabled(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_share, container, false);

        mSaveButton = (Button) v.findViewById(R.id.save_button);

        // get reference to ImageView containing the captioned meme for saving later
        mMemeImage = (ImageView) v.findViewById(R.id.meme_item2);

        mFilename = "tmb_" + getTimestamp() + ".jpg"; // create filename here for Save feature

        // update the ImageView to use the newly captioned image from CaptionFragment
        if (getArguments() != null && getArguments().containsKey(getString(R.string.captioned_meme_key))) {
            mMeme = (Meme) getArguments().getSerializable(getString(R.string.captioned_meme_key));
            Bitmap bitmap = ((BitmapDrawable) PhotoFragment.LoadImageFromWebOperations(
                    mMeme.getmInstanceImageUrl())).getBitmap();
            mMemeImage.setImageBitmap(bitmap);
        }

        // from My Meme fragment
        if (mBitmap != null) {
            mMemeImage.setImageBitmap(mBitmap);
            mSaveButton.setEnabled(false);
        } else {
            mSaveButton.setEnabled(true);
        }

        // listener for save button_enabled
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save image to a storage location (External preferred! Internal is backup...)
                if (isExternalStorageWriteable())
                    saveToExternal();
                else
                    saveToInternal();

                if (mMeme != null)
                    saveToFile(mMeme.getmInstanceUrl()); // save instance URL just in case
            }
        });

        // listener for share button_enabled
        v.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });

        // listener for main menu button_enabled
        v.findViewById(R.id.main_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(v.getId());
            }
        });

        return v;
    }

    /**
     * Helper method to Share the meme as an Intent to external activities.
     */
    private void shareImage() {
        // get imageview as bitmap
        Bitmap bitmap = ((BitmapDrawable) mMemeImage.getDrawable()).getBitmap();

        // create URL string for the meme image to parse as URI in intent
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                bitmap, "Meme", null);

        // create intent for sharing
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        intent.setType("image/jpeg");

        // launch intent chooser
        Intent chooser = Intent.createChooser(intent, "Share to...");
        if (intent.resolveActivity(getContext().getPackageManager()) != null)
            startActivity(chooser);
    }

    /**
     * Helper method for saving to Internal storage. Images cannot be viewed
     * outside of the application.
     */
    private void saveToInternal() {
        File path = new File(getActivity().getFilesDir(), MEME_DIRECTORY);
        writeToDevice(path);
    }

    /**
     * Helper method for saving to public External storage. Images can be viewed
     * outside of the application.
     */
    private void saveToExternal() {
        File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), MEME_DIRECTORY);
        writeToDevice(path);
    }

    /**
     * made for when the api is working. Saves the url of each picture
     * in order to reuse.
     * @param url of the picture.
     */
    private void saveToFile(String url) {
        FileOutputStream fos;
        try {
            fos = getContext().openFileOutput(getString(R.string.LINKS), Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.append(url + "\n");
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to write the image file to the desired path.
     * @param thePath destination
     */
    private void writeToDevice(File thePath) {
        // creates directory if needed
        if (!thePath.mkdirs())
            Log.e(TAG, "Directory not created!");

        File file;

        // try another filename based off timestamp in case current exists
        while ((file = new File(thePath, mFilename)).exists()) {
            mFilename = "tmb_" + getTimestamp() + ".jpg"; // grab new filename if already in use
        }

        FileOutputStream fos = null;
        try {
            // write image to device
            fos = new FileOutputStream(file);
            Bitmap bitmap = ((BitmapDrawable) mMemeImage.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // scan file to refresh android gallery
            MediaScannerConnection.scanFile(getContext(), new String[] { file.getPath() },
                    new String[] { "image/jpeg" }, null);

            showToast("Saved!");
        } catch (Exception e) {
            showToast("Could not open FileOutputStream");
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (Exception e) {
                showToast("Could not close FileOutputStream");
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Helper method to display a Toast to the user.
     *
     * @param theString the message to display
     */
    private void showToast(String theString) {
        Toast.makeText(getContext(), theString, Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to get the timestamp for the filenames of images.
     *
     * @return timestamp
     */
    private String getTimestamp() {

        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
    }

    /**
     * Helper method to check if there's an external storage device to save images to.
     *
     * @return true if external storage device is mounted, false otherwise
     */
    private Boolean isExternalStorageWriteable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Callback interface gets declared here.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int id);
    }
}
