package org.modi.mobileanimation.awslogin.analytics;

import org.modi.mobileanimation.awslogin.AWSException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stefan
 */

public class AWSEvent {

    private String eventName;
    private Map<String, String> attributes;
    private Map<String, Double> metrics;

    private AWSEvent(){
        eventName = "No Event name";
        attributes = new HashMap<>();
        metrics = new HashMap<>();
    }

    // Minimum Factory

    /**
     * A minimal factory pattern, wich returns an AWSEvent
     * wich doubles as an DataObject a Builder for said DataObject
     * @return a new AWSEvent
     */
    public static AWSEvent initialise() {
        return new AWSEvent();
    }



    /*
     * Add metrics and attributes
     */

    // TODO Documentation
    public AWSEvent addMetrics(String key, Double value) {
        metrics.put(key, value);
        return this;
    }

    // TODO Documentation
    public AWSEvent addAttributes(String key, String value) {
        attributes.put(key, value);
        return this;
    }


    /*
     * Getter and Setters
     */

    // TODO Documentation
    public String getEventName() {
        return eventName;
    }

    // TODO Documentation
    public AWSEvent setEventName(String eventName) {
        if (eventName == null)
            throw new AWSException(EVENT_NULL);
        this.eventName = eventName;
        return this;
    }

    // TODO Documentation
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    // TODO Documentation
    public AWSEvent setAttributes(Map<String, String> attributes) {
        if (eventName == null)
            throw new AWSException(MAP_NULL);
        this.attributes = attributes;
        return this;
    }

    // TODO Documentation
    public Map<String, Double> getMetrics() {
        return Collections.unmodifiableMap(metrics);
    }

    // TODO Documentation
    public AWSEvent setMetrics(Map<String, Double> metrics) {
        if (eventName == null)
            throw new AWSException(MAP_NULL);
        this.metrics = metrics;
        return this;
    }


    /*
     * Errors
     */
    private static final java.lang.String EVENT_NULL = "The name of the event must not be null";
    private static final java.lang.String MAP_NULL = "The given map must not be null";
}
