package org.modi.mobileanimation.awslogin.analytics;

import android.app.Activity;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;

import org.modi.mobileanimation.awslogin.AWSException;
import org.modi.mobileanimation.awslogin.AWSUtility;

import java.util.Map;

/**
 * Created by Stefan
 *
 * {@link AWSAnalytics} helps with Pinpoint analytics (AWS)
 * {@link AWSAnalyticsStart}, {@link AWSAnalyticsDo} and {@link AWSAnalyticsSubmit} help with
 * viewing this class from the outside.
 *
 */

public class AWSAnalytics implements AWSAnalyticsStart, AWSAnalyticsDo, AWSAnalyticsSubmit {

    // Members
    private static PinpointManager pinpointManager;

    final static String LOG_TAG = AWSAnalytics.class.getSimpleName();


    @Override
    public AWSAnalyticsDo begin(Activity mainActivity) {
        info(BEGIN_ANALYTICS);
        PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                mainActivity.getApplicationContext(),
                AWSMobileClient.getInstance().getCredentialsProvider(),
                AWSMobileClient.getInstance().getConfiguration());
        pinpointManager = new PinpointManager(pinpointConfig);
        // Start a session with Pinpoint
        pinpointManager.getSessionClient().startSession();
        return this;
    }

    @Override
    public AWSAnalyticsSubmit logEvent(String eventName, Map<String, String> attributes, Map<String, Double> metrics) {
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
        return this;
    }

    @Override
    public AWSAnalyticsSubmit logEvent(AWSEvent event) {
        if (pinpointManager == null) {
            error(MANAGER_NULL_LOG);
            throw new AWSException(MANAGER_NULL_LOG);
        }
        return logEvent(event.getEventName(), event.getAttributes(), event.getMetrics());
    }

    @Override
    public AWSUtility submit() {
        if (pinpointManager == null) {
            error(MANAGER_NULL_SUBMIT);
            throw new AWSException(MANAGER_NULL_SUBMIT);
        }

        info(SUBMIT_ANALYTICS);
        pinpointManager.getSessionClient().stopSession();
        pinpointManager.getAnalyticsClient().submitEvents();
        return AWSUtility.getInstance();
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
    private static AWSAnalytics instance;
    private AWSAnalytics(){  }
    /**
     * Basic singleton instance access
     * @return an singleton instance of {@link AWSAnalytics}
     */
    public static AWSAnalytics getInstance() {
        if (instance == null)
            instance = new AWSAnalytics();
        return instance;
    }


    /*
     * Error messages
     */
    private static final String BEGIN_ANALYTICS = "Starting aws Pinpoint analytics";
    private static final String SUBMIT_ANALYTICS = "Submitting events to Pinpoint analytics";
    private static final String MANAGER_NULL_SUBMIT = "PinpointManager is null, this suggests call submitAnalytics before beginAnalytics";
    private static final String MANAGER_NULL_LOG = "Cannot log events, because PinpointMangaer is null, this suggests beginAnalytics was never called";
    private static final String RECORD_EVENT = "Recording event for PinpointAnalytics";

}
