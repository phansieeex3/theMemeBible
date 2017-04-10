package group3.tcss450.uw.edu.thememebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import group3.tcss450.uw.edu.challengeapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RegisterFragment";
    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        // add listener for Register button
        Button b = (Button) v.findViewById(R.id.btnRegisterUser);
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
    public void onClick(View view) {
        if (mListener != null) {
            if (view.getId() == R.id.btnRegisterUser) {
                Log.i(TAG, "btnRegister button pressed");

                // get edit text references
                EditText etUsername = (EditText) getActivity().findViewById(R.id.editUsername);
                EditText etPassword = (EditText) getActivity().findViewById(R.id.editPassword);
                EditText etPasswordConf = (EditText) getActivity().findViewById(R.id.editPasswordConf);
                String strUsername = etUsername.getText().toString();
                String strPassword = etPassword.getText().toString();
                String strPasswordConf = etPasswordConf.getText().toString();

                // check if any are empty
                if (TextUtils.isEmpty(strUsername)) {
                    etUsername.setError("Please enter your username");
                    return;
                } else if (TextUtils.isEmpty(strPassword)) {
                    etPassword.setError("Please enter your password");
                    return;
                } else if (TextUtils.isEmpty(strPasswordConf)) {
                    etPasswordConf.setError("Please confirm your password");
                    return;
                }

                if (!strPassword.equals(strPasswordConf)) {
                    Toast.makeText(getContext(), "Passwords do not match!",Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    etPasswordConf.setText("");
                    return;
                }

                Log.e(TAG, "strUser: " + strUsername + ", strPassword: " + strPassword + ", "
                + strPasswordConf);

                // build arguments to pass through
                Bundle args = new Bundle();
                args.putString(getString(R.string.username_key_loginfragment), strUsername);
                args.putString(getString(R.string.password_key_loginfragment), strPassword);
                args.putString(getString(R.string.confirm_password_key_loginfragment), strPasswordConf);

                // should the edittext boxes be cleared at this point?

                // give MainActivity the args bundle and make callback
                mListener.getRegistrationInformation(args);
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
        void getRegistrationInformation(Bundle args);
    }
}
