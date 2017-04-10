package group3.tcss450.uw.edu.thememebible;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import group3.tcss450.uw.edu.challengeapp.R;

public class MainActivity extends AppCompatActivity implements InitialFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener,
        DisplayFragment.OnFragmentInteractionListener
{
    private static final String TAG = "MainActivity";
    private Bundle mLoginArgs;
    private Bundle mRegisterArgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate InitialFragment
        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new InitialFragment())
                        .commit();
            }
        }
    }

    // implements all button press callbacks from other Fragments
    @Override
    public void onFragmentInteraction(int buttonID) {
        FragmentTransaction transaction;
        DisplayFragment display;

        switch(buttonID) {
            case R.id.btnLogin:
                Log.i(TAG, "btnLogin buttonID");
                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new LoginFragment())
                        .addToBackStack(null);
                transaction.commit();
                break;

            case R.id.btnRegister:
                Log.i(TAG, "btnRegister buttonID");
                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new RegisterFragment())
                        .addToBackStack(null);
                transaction.commit();
                break;

            // redundant but oh well
            case R.id.btnOK:
                Log.i(TAG, "btnOK buttonID");

                display = new DisplayFragment();
                display.setArguments(mLoginArgs);

                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, display)
                        .addToBackStack(null);
                transaction.commit();
                break;

            // redundant but oh well
            case R.id.btnRegisterUser:
                Log.i(TAG, "btnRegister buttonID");

                display = new DisplayFragment();
                display.setArguments(mRegisterArgs);

                transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, display)
                        .addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    // callback for DisplayFragment data
    @Override
    public void getRegistrationInformation(Bundle args) {
        mRegisterArgs = args;
    }

    // callback for LoginFragment data
    @Override
    public void getLogin(Bundle args) {
        mLoginArgs = args;
    }
}
