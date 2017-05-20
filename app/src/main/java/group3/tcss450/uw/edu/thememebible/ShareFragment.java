package group3.tcss450.uw.edu.thememebible;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Save or share memes memes that have been captioned by the user.
 *
 * @author Peter Phe
 */
public class ShareFragment extends Fragment {

    private static final String TAG = "ShareFragment";
    private static final String MEME_DIRECTORY = "/Memes";
    private Button mSaveButton;
    private ImageView mMemeImage;
    private String mFilename;

    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        // enable/disable the Save button based on Storage permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            mSaveButton.findViewById(R.id.save_button).setEnabled(false);
        } else {
            mSaveButton.setEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_share, container, false);

        // get reference to ImageView containing the captioned meme for saving later
        //todo the R.id.memecreate__activity__image might need to get changed later
        mMemeImage = (ImageView) v.findViewById(R.id.memecreate__activity__image);
        mFilename = "tmb_" + getTimestamp() + ".jpg"; // create filename here for Save feature

        // listener for save button
        mSaveButton = (Button) v.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save image to a storage location (External preferred! Internal is backup...)
                if (isExternalStorageWriteable())
                    saveToExternal();
                else
                    saveToInternal();
            }
        });

        // listener for share button
        v.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
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

        // convert bitmap to byte array for creating path
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        // create URL for the meme image to parse as URI in intent
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                bitmap, "Meme", null);

        // create intent for sharing
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        intent.setType("image/jpeg");

        // launch Intent chooser
        Intent chooser = Intent.createChooser(intent, "Share to");
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
     * Helper method to write the image file to the desired path.
     * @param thePath destination
     */
    private void writeToDevice(File thePath) {
        // creates directory if needed
        if (!thePath.mkdirs())
            Log.e(TAG, "Directory not created!");

        File file;

        // try another filename (based off timestamp) in case current exists
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

    }
}
