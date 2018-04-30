package com.joemarini.firebasefirstlook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

public class PromoScreen extends AppCompatActivity
    implements View.OnClickListener {

    // Create a TAG for logging
    private final String TAG = "FB_PROMO";

    // Declare a FirebaseAnalytics object
    private FirebaseAnalytics mFBAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_screen);

        // TODO: Get a reference to Firebase analytics
        mFBAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO: Record the user's view of this Activity as a VIEW_ITEM
        // analytics event, which is provided by Firebase

        // Create a Bundle object
        Bundle params = new Bundle();

        // Put the key and value of the event in the bundle
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "Promo Item");

        // Record the event as a ViewItem event
        mFBAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
    }

    @Override
    public void onClick(View v) {

        // Get theId of the button clicked
        if (v.getId() == R.id.btnBuyNow) {
            // TODO: Record a click on the Buy button as an ECOMMERCE_PURCHASE
            // Firebase analytics event

            // Create a Bundle object
            Bundle params = new Bundle();

            // Put the key and value of the event in the bundle
            params.putString(FirebaseAnalytics.Param.ITEM_ID, "Promo Item");

            // Record the event as a EcommercePurchase event
            mFBAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, params);

        }
    }
}
