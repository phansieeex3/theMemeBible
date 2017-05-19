package group3.tcss450.uw.edu.thememebible;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class share_meme extends Fragment {

    private Button mSaveButton;
    private ImageView mMemeImage;

    public share_meme() {
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
        View v = inflater.inflate(R.layout.fragment_share_meme, container, false);

        // wtf is up with the naming convention on this? ...
        mMemeImage = (ImageView) v.findViewById(R.id.memecreate__activity__image);

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

        return v;
    }

    /**
     * Helper method for saving to Internal storage.
     */
    private void saveToInternal() {

    }

    /**
     * Helper method for saving to External storage.
     */
    private void saveToExternal() {

    }

    public interface OnFragmentInteractionListener {
    }

    /**
     * Helper method to check if there's an external storage device to save images to.
     *
     * @return true if external storage device is mounted, false otherwise
     */
    private Boolean isExternalStorageWriteable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
