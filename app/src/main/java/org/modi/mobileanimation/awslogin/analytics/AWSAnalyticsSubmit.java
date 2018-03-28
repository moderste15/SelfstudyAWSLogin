package org.modi.mobileanimation.awslogin.analytics;

import org.modi.mobileanimation.awslogin.AWSUtility;

/**
 * Created by Stefan
 */

public interface AWSAnalyticsSubmit {


    /**
     * Submits the current Event to AWSPinpoint
     * @return A sigleton of AWSUtility
     */
    AWSUtility submit();
}
