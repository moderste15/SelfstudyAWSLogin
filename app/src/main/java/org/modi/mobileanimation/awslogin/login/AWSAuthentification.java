package org.modi.mobileanimation.awslogin.login;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import org.modi.mobileanimation.awslogin.AWSUtility;
import org.modi.mobileanimation.awslogin.ExampleSuccess;

/**
 * Created by Stefan
 */

public class AWSAuthentification {


    /**
     * Uses aws {@link SignInUI} to create a sign in screen
     * @param mainActivity
     * @param config
     */
    public void loginActivityUI(final Activity mainActivity,
                                final AuthUIConfiguration config,
                                final Class<? extends AppCompatActivity> next) {
        AWSMobileClient.getInstance().initialize(mainActivity, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(mainActivity, SignInUI.class);
                signin.login(mainActivity, next)
                        .authUIConfiguration(config)
                        .execute();
            }
        }).execute();


    }




    // Auto generated
    private static final AWSAuthentification ourInstance = new AWSAuthentification();

    public static AWSAuthentification getInstance() {
        return ourInstance;
    }

    private AWSAuthentification() {
    }
}
