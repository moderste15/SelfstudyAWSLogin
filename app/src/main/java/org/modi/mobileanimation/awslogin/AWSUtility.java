package org.modi.mobileanimation.awslogin;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;

import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;

import java.util.Map;

/**
 * Created by Stefan
 * AWSUtility should abstract some of the steps of setting up amazonaws
 */
public class AWSUtility {


    //Members
    private static String LOG_TAG = AWSUtility.class.getSimpleName();
    private static PinpointManager pinpointManager;
    // errors @see eof

    /**
     * Starts connection with AWS Services - static call
     * @param mainActivity of the application
     * @returnan singleton instance of AWSUtility
     */
    public static AWSUtility doStart(Activity mainActivity) {
        info(STARTING_AWS);
        AWSMobileClient.getInstance().initialize(mainActivity).execute();
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


    /**
     * Setups and starts pinpoint analytics - static call
     * @param mainActivity of the application
     * @return singleton instance of AWSUtility
     */
    public static  AWSUtility doBeginAnalytics(Activity mainActivity) {
        info(BEGIN_ANALYTICS);
        PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                mainActivity.getApplicationContext(),
                AWSMobileClient.getInstance().getCredentialsProvider(),
                AWSMobileClient.getInstance().getConfiguration());
        pinpointManager = new PinpointManager(pinpointConfig);
        // Start a session with Pinpoint
        pinpointManager.getSessionClient().startSession();
        return getInstance();
    }
    /**
     * Setups and starts pinpoint analytics - instance call
     * @param mainActivity of the application
     * @return singleton instance of AWSUtility
     */
    public AWSUtility beginAnalytics(Activity mainActivity) {
        return doBeginAnalytics(mainActivity);
    }


    /**
     * Submits all logged events to Pinpoint analytics - static call
     * @return  singleton instance of AWSUtility
     */
    public static AWSUtility doSubmitAnalytics() {
        if (pinpointManager == null) {
            error(MANAGER_NULL_SUBMIT);
            throw new AWSException(MANAGER_NULL_SUBMIT);
        }

        info(SUBMIT_ANALYTICS);
        pinpointManager.getSessionClient().stopSession();
        pinpointManager.getAnalyticsClient().submitEvents();
        return getInstance();
    }
    /**
     * Submits all logged events to Pinpoint analytics - instance call
     * @return  singleton instance of AWSUtility
     */
    public AWSUtility submitAnalytics() {
        return doSubmitAnalytics();
    }


    /**
     * Logs an event by its name with a map of attributes and a map of metrics - static call
     * @param eventName simple name of the event to be recorded
     * @param attributes map of attributes (value String)
     * @param metrics map of metrics (value Double)
     * @return  singleton instance of AWSUtility
     */
    public  static AWSUtility doLogEvent(String eventName, Map<String, String> attributes, Map<String, Double> metrics) {
        if (pinpointManager == null) {
            error(MANAGER_NULL_LOG);
            throw new AWSException(MANAGER_NULL_LOG);
        }
        pinpointManager.getSessionClient().startSession();
        AnalyticsEvent event = pinpointManager.getAnalyticsClient().createEvent(eventName);
        for (Map.Entry<String, String> a : attributes.entrySet())
            event = event.withAttribute(a.getKey(), a.getValue());
        for (Map.Entry<String, Double> m : metrics.entrySet())
            event = event.withMetric(m.getKey(), m.getValue());
        info(RECORD_EVENT);
        pinpointManager.getAnalyticsClient().recordEvent(event);
        return getInstance();
    }
    /**
     * Logs an event by its name with a map of attributes and a map of metrics - instance call
     * @param eventName simple name of the event to be recorded
     * @param attributes map of attributes (value String)
     * @param metrics map of metrics (value Double)
     * @return  singleton instance of AWSUtility
     */
    public AWSUtility logEvent(String eventName, Map<String, String> attributes, Map<String, Double> metrics) {
        return doLogEvent(eventName, attributes ,metrics);
    }

    /**
     * Starts aws SignUI - static call
     * @return  singleton instance of AWSUtility
     */
    public static AWSUtility doLoginActivityUI(final Activity mainActivity, final AuthUIConfiguration config) {
        info(START_LOGIN);
        AWSMobileClient.getInstance().initialize(mainActivity, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(mainActivity, SignInUI.class);
                signin.login(mainActivity, ExampleSuccess.class)
                        .authUIConfiguration(config)
                        .execute();
            }
        }).execute();
        return getInstance();
    }
    /**
     * Starts aws SignUI - instance call
     * @return  singleton instance of AWSUtility
     */
    public AWSUtility loginActivityUI(Activity mainActivity, AuthUIConfiguration config) {
        return doLoginActivityUI(mainActivity, config);
    }


    /*
     * Shortend log
     */
    private static void info(String msg) {
        Log.i(LOG_TAG, msg);
    }
    private static void error(String msg) {
        Log.e(LOG_TAG, msg);
    }

    /*
     * Singleton
     */
    private static AWSUtility Instance = null;

    private AWSUtility() {

    }

    /**
     * Basic singleton instance access
     * @return an singleton instance of AWSUtility
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
    private static final String BEGIN_ANALYTICS = "Starting aws Pinpoint analytics";
    private static final String SUBMIT_ANALYTICS = "Submitting events to Pinpoint analytics";
    private static final String MANAGER_NULL_SUBMIT = "PinpointManager is null, this suggests call submitAnalytics before beginAnalytics";
    private static final String MANAGER_NULL_LOG = "Cannot log events, because PinpointMangaer is null, this suggests beginAnalytics was never called";
    private static final String RECORD_EVENT = "Recording event for PinpointAnalytics";
    private static final String START_LOGIN = "Starting aws SignUI login activity";

}
