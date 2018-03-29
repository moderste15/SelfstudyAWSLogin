package org.modi.mobileanimation.awslogin.login;

import android.app.Activity;
import android.util.Log;

import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.StartupAuthResult;
import com.amazonaws.mobile.auth.core.StartupAuthResultHandler;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

/**
 * Created by Stefan
 */

public class AWSAuthStartUpHandler implements AWSStartupHandler {

    private final String LOG_TAG = AWSAuthStartUpHandler.class.getSimpleName();
    private final Activity activity;


    @Override
    public void onComplete(AWSStartupResult awsStartupResult) {
        //Make a network call to retrieve the identity ID
        // using IdentityManager. onIdentityId happens UPon success.
        final IdentityManager manager = IdentityManager.getDefaultIdentityManager();
        manager.getUserID(new IdentityHandler() {

            @Override
            public void onIdentityId(String s) {
                /*
                 * What ids are cached
                 */
                Log.d(LOG_TAG, "Identity ID is: " + s);
                Log.d(LOG_TAG, "Cached Identity ID: " + IdentityManager.getDefaultIdentityManager().getCachedUserID());
            }

            @Override
            public void handleError(Exception e) {
                Log.e(LOG_TAG, "Error in retrieving Identity ID: " + e.getMessage());
            }
        });

        manager.resumeSession(activity, new StartupAuthResultHandler() {
            @Override
            public void onComplete(StartupAuthResult authResults) {
                AWSAuthentification.awsSetLogin(authResults.isUserSignedIn());
                Log.i(LOG_TAG, AWSAuthentification.isUserLogedIn() ? "Signed in" : "Not signed in");
            }
        });
    }

    public AWSAuthStartUpHandler(Activity callingActivity) {
        this.activity = callingActivity;
    }
}
