package group3.tcss450.uw.edu.challengeapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // add listener for OK button
        Button b = (Button) v.findViewById(R.id.btnOK);
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            if (view.getId() == R.id.btnOK) {
                Log.i(TAG, "OK button pressed");

                // get edit text references
                EditText etUsername = (EditText) getActivity().findViewById(R.id.editTextUsername);
                EditText etPassword = (EditText) getActivity().findViewById(R.id.editTextPassword);
                String strUsername = etUsername.getText().toString();
                String strPassword = etPassword.getText().toString();

                // check if either are empty
                if (TextUtils.isEmpty(strUsername)) {
                    etUsername.setError("Please enter your username");
                    return;
                } else if (TextUtils.isEmpty(strPassword)) {
                    etPassword.setError("Please enter your password");
                    return;
                }

                // build arguments to pass through
                Bundle args = new Bundle();
                args.putString(getString(R.string.username_key_loginfragment), strUsername);
                args.putString(getString(R.string.password_key_loginfragment), strPassword);

                // clear edittext boxes
                etUsername.setText("");
                etPassword.setText("");

                // give MainActivity the args bundle and make callback
                mListener.getLogin(args);
                mListener.onFragmentInteraction(view.getId());
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
        void getLogin(Bundle args);
    }
}
