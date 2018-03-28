package org.modi.mobileanimation.awslogin.analytics;

import org.modi.mobileanimation.awslogin.AWSUtility;

import java.util.Map;

/**
 * Created by Stefan
 */

public interface AWSAnalyticsDo {

    /**
     * Logs an event by its name with a map of attributes and a map of metrics
     * @param eventName simple name of the event to be recorded
     * @param attributes map of attributes (value String)
     * @param metrics map of metrics (value Double)
     * @return  singleton instance of {@link AWSAnalyticsDo}
     */
    AWSAnalyticsSubmit logEvent(String eventName, Map<String, String> attributes, Map<String, Double> metrics);

    /**
     * Logs an event as an AWSEvent
     * @param event {@link AWSEvent} to be logged
     * @return  singleton instance of {@link AWSAnalyticsDo}
     */
    AWSAnalyticsSubmit logEvent(AWSEvent event);


}
