package org.modi.mobileanimation.awslogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.client.AWSMobileClient;

import org.modi.mobileanimation.awslogin.analytics.AWSAnalytics;
import org.modi.mobileanimation.awslogin.analytics.AWSAnalyticsDo;
import org.modi.mobileanimation.awslogin.login.AWSAuthStartUpHandler;
import org.modi.mobileanimation.awslogin.login.AWSAuthentification;


/**
 * Created by Stefan
 * AWSUtility should abstract some of the steps of setting up amazonaws
 */
public class AWSUtility {


    //Members
    private static String LOG_TAG = AWSUtility.class.getSimpleName();
    // errors @see eof


    /*
     * Start up
     */

    /**
     * Starts connection with AWS Services - static call
     * @param mainActivity of the application
     * @returnan singleton instance of AWSUtility
     */
    public static AWSUtility doStart(Activity mainActivity) {
        info(STARTING_AWS);
        AWSMobileClient.getInstance().initialize(mainActivity, new AWSAuthStartUpHandler(mainActivity)).execute();
        return getInstance();
    }
    /**
     * Starts connection with AWS Services - instance call
     * @param mainActivity of the application
     * @returnan singleton instance of AWSUtility
     */
    public AWSUtility start(Activity mainActivity) {
        return doStart(mainActivity);
    }



    /*
     * Analytics
     */

    /**
     * Setups and starts pinpoint analytics - static call
     * @param activity of the application
     * @return singleton instance of AWSUtility
     */
    public static  AWSAnalyticsDo doBeginAnalytics(Activity activity) {
        return AWSAnalytics.getInstance().begin(activity);
    }
    /**
     * Setups and starts pinpoint analytics - instance call
     * @param mainActivity of the application
     * @return singleton instance of AWSUtility
     */
    public AWSAnalyticsDo beginAnalytics(Activity mainActivity) {
        return doBeginAnalytics(mainActivity);
    }





    /*
     * Login
     */

    /**
     * Starts aws SignUI - static call
     * @return  singleton instance of {@link AWSUtility}
     */
    public static AWSUtility doLoginActivityUI(final Activity activity,
                                               final AuthUIConfiguration config,
                                               final Class<? extends AppCompatActivity> next) {
        info(START_LOGIN);
        AWSAuthentification.loginActivityUI(activity, config, next);
        return getInstance();
    }
    /**
     * Starts aws SignUI - instance call
     * @return  singleton instance of {@link AWSUtility}
     */
    public AWSUtility loginActivityUI(Activity mainActivity,
                                      AuthUIConfiguration config,
                                      final Class<? extends AppCompatActivity> next) {
        return doLoginActivityUI(mainActivity, config, next);
    }


    /**
     * Starts a CustomUI - static  call
     * @return
     */
    public static AWSUtility doLoginCustomUI(Context context) {
        // TODO just for test not real use
        Intent intent = new Intent(context, ExampleLoginScreenActivity.class);
        context.startActivity(intent);

        return getInstance();
    }
    /**
     * Starts a CustomUI -instance call
     * @return
     */
    public AWSUtility loginCustomUI(Context context) {
        return doLoginCustomUI(context);
    }


    /*
     * Shortend log
     */
    private static void info(String msg) {
        Log.i(LOG_TAG, msg);
    }

    /*
     * Singleton
     */
    private static AWSUtility Instance = null;
    private AWSUtility() {

    }

    /**
     * Basic singleton instance access
     * @return an singleton instance of {@link AWSUtility}
     */
    public static AWSUtility getInstance() {
        if (Instance == null)
            Instance = new AWSUtility();
        return Instance;
    }



    /*
     * Error messages
     */
    private static final String STARTING_AWS = "start - Starting up aws";
    private static final String START_LOGIN = "Starting aws SignUI login activity";

}
