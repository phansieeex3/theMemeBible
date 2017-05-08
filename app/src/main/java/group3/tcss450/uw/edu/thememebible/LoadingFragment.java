package group3.tcss450.uw.edu.thememebible;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Simple Fragment that displays a Progress Bar while AsyncTask runs.
 *
 * @author Peter Phe
 * @version 1.0
 */
public final class LoadingFragment extends Fragment {

    public LoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    /**
     * Simple callback interface for showing and dismissing the progress bar.
     */
    interface DisplayInterface {
        void displayProgressBar();
        void dismissProgressBar();
    }
}
