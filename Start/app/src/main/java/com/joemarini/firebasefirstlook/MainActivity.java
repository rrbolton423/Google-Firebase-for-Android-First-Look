package com.joemarini.firebasefirstlook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener {

    // Create TAG for logging
    private final String TAG = "FB_FIRSTLOOK";

    // Firebase Remote Config settings
    private final String CONFIG_PROMO_MESSAGE_KEY = "promo_message";
    private final String CONFIG_PROMO_ENABLED_KEY = "promo_enabled";

    // cache duration setting
    private long PROMO_CACHE_DURATION = 1800;

    // Firebase Analytics settings
    private final int MIN_SESSION_DURATION = 5000;

    // TODO: define analytics object
    private FirebaseAnalytics mFBAnalytics;

    // TODO: define Remote Config object
    private FirebaseRemoteConfig mFBConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Retrieve an instance of the Analytics package
        mFBAnalytics = FirebaseAnalytics.getInstance(this);

        // TODO: Get the Remote Config instance
        mFBConfig = FirebaseRemoteConfig.getInstance();

        // TODO: Wait 5 seconds before counting this as a session
        mFBAnalytics.setMinimumSessionDuration(MIN_SESSION_DURATION);

        // TODO: Add Remote Config Settings
        // Enable developer mode to perform more rapid testing.
        // Config fetches are normally limited to 5 per hour. This
        // enables many more requests to facilitate testing.
        FirebaseRemoteConfigSettings configSettings = new
                FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        // Set the new configuration settings
        mFBConfig.setConfigSettings(configSettings);

        // TODO: Get the default parameter settings from the XML file
        mFBConfig.setDefaults(R.xml.firstlook_config_params);

        // set up button click handlers
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btnAuthActivity).setOnClickListener(this);
        findViewById(R.id.btnPromo).setOnClickListener(this);

        // Check to see if the promo button should be enabled
        checkPromoEnabled();
    }

    private void checkPromoEnabled() {
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        // TODO: Set the cache duration for developer testing
        if (mFBConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {

            // Set the cache duration to 0 for developer mode
            PROMO_CACHE_DURATION = 0;
        }

        // TODO: fetch the values from the Remote Config service
        // Get the settings from the server
        mFBConfig.fetch(PROMO_CACHE_DURATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // If the fetch was successful...
                        if (task.isSuccessful()) {

                            // Log success
                            Log.i(TAG, "Promo check was successful");

                            // Activate the fetch values
                            // Take the values from the server and override the defaults
                            mFBConfig.activateFetched();

                            // If the fetch was NOT successful...
                        } else {

                            // Log failed
                            Log.e(TAG, "Promo check failed");
                        }

                        // Show or hide the promo button...
                        showPromoButton();
                    }
                });
    }

    private void showPromoButton() {

        // Determine whether the show the promo button and what
        // the promo message should be to give to the user
        boolean showBtn = false;
        String promoMsg = "";

        // TODO: get the promo setting from Remote Config
        showBtn = mFBConfig.getBoolean(CONFIG_PROMO_ENABLED_KEY);
        promoMsg = mFBConfig.getString(CONFIG_PROMO_MESSAGE_KEY);

        // Create an instance of the button
        Button btn = (Button)findViewById(R.id.btnPromo);

        // If showButton is true, make it visible, else, make it invisible
        btn.setVisibility(showBtn ? View.VISIBLE : View.INVISIBLE);

        // Set the text on the button
        btn.setText(promoMsg);
    }

    @Override
    public void onClick(View v) {

        // Create the Bundle that will hold the data sent to
        // the Analytics package

        // Create a Bundle object
        Bundle params = new Bundle();

        // Add the button clicked to the Bundle
        params.putInt("ButtonID", v.getId());

        // Declare a String to represent the button name that was clicked
        String btnName;

        // Get the Id of the clicked button
        switch (v.getId()) {

            // If btn1 was clicked...
            case R.id.btn1:
                // Assign the button name
                btnName = "Button1Click";
                // Set the appropriate status
                setStatus("btn1 clicked");
                // Break from the switch statement
                break;

            // If btn1 was clicked...
            case R.id.btn2:
                // Assign the button name
                btnName = "Button2Click";
                // Set the appropriate status
                setStatus("btn2 clicked");
                // Break from the switch statement
                break;

            // If btn1 was clicked...
            case R.id.btnAuthActivity:
                // Assign the button name
                btnName = "ButtonAuthClick";
                // Set the appropriate status
                setStatus("btnAuthActivity clicked");
                // Go to the SignInActivity
                startActivity(new Intent(this, SignInActivity.class));
                // Break from the switch statement
                break;

            // If btn1 was clicked...
            case R.id.btnPromo:
                // Assign the button name
                btnName = "ButtonPromoClick";
                // Set the appropriate status
                setStatus("btnPromo clicked");
                // Go to the PromoActivity
                startActivity(new Intent(this, PromoScreen.class));
                // Break from the switch statement
                break;

                // If default...
            default:
                // Assign the button name
                btnName = "OtherButton";
                // Break from the switch statement
                break;
        }

        // Log Button info
        Log.d(TAG, "Button click logged: " + btnName);

        // TODO: Log the button press as an analytics event
        mFBAnalytics.logEvent(btnName, params);

    }

    private void setStatus(String text) {

        // Get a reference to the status TextView
        TextView tvStat = (TextView)findViewById(R.id.tvStatus);

        // Set the status
        tvStat.setText(text);
    }
}
