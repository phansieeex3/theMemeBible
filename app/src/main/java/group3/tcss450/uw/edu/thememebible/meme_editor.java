package group3.tcss450.uw.edu.thememebible;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class meme_editor extends Fragment implements View.OnClickListener {


    public meme_editor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meme_editor, container, false);
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnFragmentInteractionListener {
    }
}