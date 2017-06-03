package group3.tcss450.uw.edu.thememebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * The Main Menu for the application.
 *
 * @author Vu Hoang
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        // add listener for OK button_enabled
        Button b = (Button) v.findViewById(R.id.meme_catalog);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.my_memes);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.logout_button);
        b.setOnClickListener(this);

        return v;
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
    public void onClick(View v) {

        if (mListener != null) {
            switch (v.getId()) {
                case R.id.meme_catalog:
                    mListener.onFragmentInteraction(v.getId());
                    break;

                case R.id.my_memes:
                    mListener.onFragmentInteraction(v.getId());
                    break;

                case R.id.logout_button:
                    mListener.onFragmentInteraction(v.getId());
                    break;
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int buttonID);
    }
}
