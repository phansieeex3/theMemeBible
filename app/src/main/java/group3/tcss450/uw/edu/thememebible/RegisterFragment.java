package group3.tcss450.uw.edu.thememebible;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Allows new users to Register to use the application.
 *
 * @author Peter Phe
 * @version 1.0
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RegisterFragment";
    private OnFragmentInteractionListener mListener;
    private static final int MINIMUM_LENGTH = 4;
    private static final String USERNAME_INFO = "Username must be a minimum of "
            + MINIMUM_LENGTH + " alphanumeric characters";
    private static final String PASSWORD_INFO = "Password must be a minimum of "
            + MINIMUM_LENGTH + " alphanumeric characters";

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        // add listener for Register button_enabled
        Button b = (Button) v.findViewById(R.id.btnRegisterUser);
        b.setOnClickListener(this);

        ImageButton infoUsername = (ImageButton) v.findViewById(R.id.btnUsernameInfo);
        infoUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(USERNAME_INFO);
            }
        });

        ImageButton infoPassword = (ImageButton) v.findViewById(R.id.btnPasswordInfo);
        infoPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(PASSWORD_INFO);
            }
        });

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

                // get edit text references
                EditText etUsername = (EditText) getActivity().findViewById(R.id.editUsername);
                EditText etPassword = (EditText) getActivity().findViewById(R.id.editPassword);
                EditText etPasswordConf = (EditText) getActivity().findViewById(R.id.editPasswordConf);
                String strUsername = etUsername.getText().toString();
                String strPassword = etPassword.getText().toString();
                String strPasswordConf = etPasswordConf.getText().toString();

                // check lengths
                if (strUsername.length() < MINIMUM_LENGTH || !isAlphanumeric(strUsername)) {
                    showToast(USERNAME_INFO);
                    return;
                } else if (strPassword.length() < MINIMUM_LENGTH || !isAlphanumeric(strPassword)) {
                    showToast(PASSWORD_INFO);
                    return;
                } else if (TextUtils.isEmpty(strUsername)) { // check if any edittexts are empty
                    etUsername.setError("Please enter your username");
                    return;
                } else if (TextUtils.isEmpty(strPassword)) {
                    etPassword.setError("Please enter your password");
                    return;
                } else if (TextUtils.isEmpty(strPasswordConf)) {
                    etPasswordConf.setError("Please confirm your password");
                    return;
                } else if (!strPassword.equals(strPasswordConf)) { // check if passwords don't match
                    showToast("Passwords do not match!");
                    etPassword.setText("");
                    etPasswordConf.setText("");
                    return;
                }

                // initiate registration service
                new RegisterFragment.RegisterWebServiceTask(view.getId()).execute(
                        MainActivity.PARTIAL_URL, strUsername, strPassword);
            }
        }
    }

    /**
     * Helper method to check if a string contains alphanumeric characters only.
     *
     * @param theString the string to check
     * @return true if alphanumeric, false otherwise
     */
    private boolean isAlphanumeric(String theString) {
        return theString.matches("[a-zA-Z0-9]+");
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
     * This class contains the service required for registering as a new user in the application.
     */
    private class RegisterWebServiceTask extends AsyncTask<String, Void, String> {

        private final String SERVICE = "register.php";
        private final int mButtonID;

        public RegisterWebServiceTask(int theButtonID) {
            mButtonID = theButtonID;
        }

        @Override
        protected void onPreExecute() {
            // disable button_enabled task is running
            getActivity().findViewById(mButtonID).setEnabled(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) { // URL, login_name, login_pass
                throw new IllegalArgumentException("Two String arguments required.");
            }

            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];

            try {
                // build URL
                URL urlObject = new URL(url + SERVICE);

                // connect with URL (returns instance of HttpURLConnection)
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                // prep JSON object to send login info
                JSONObject json = new JSONObject();
                json.put("register_name", strings[1]);
                json.put("register_pass", strings[2]);
                Log.i(TAG, "json toString(): " + json.toString());

                // prep for POST
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("json", "UTF-8")
                        + "=" + URLEncoder.encode(json.toString(), "UTF-8");

                // send data to stream
                wr.write(data);
                wr.flush();

                // prep for response from server
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String chunk = "";

                // consume data to build response string
                while ((chunk = buffer.readLine()) != null) {
                    response += chunk;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: " + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // reenable button_enabled
            getActivity().findViewById(mButtonID).setEnabled(true);
            Log.i(TAG, "Response from server: " + response);

            // check if bad response (from exception thrown in doInBackground())
            if (response.startsWith("Unable to")) {
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                return;
            }

            // parse response (a JSON string) and check status code
            try {
                JSONObject json = new JSONObject(response);
                if (json.getInt("status") == 201) // registration successful
                {
                    Bundle args = new Bundle();
                    args.putString(getString(R.string.response_key_displayfragment),
                            json.getString("message"));

                    // give MainActivity the args bundle and make callback
                    mListener.getRegistrationInformation(args);
                    mListener.onFragmentInteraction(mButtonID);
                } else { // registration failed
                    String message = json.getString("message");
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
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
