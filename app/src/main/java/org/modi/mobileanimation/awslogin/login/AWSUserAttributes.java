package org.modi.mobileanimation.awslogin.login;

import org.modi.mobileanimation.awslogin.AWSUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stefan
 */

public class AWSUserAttributes {


    private Map<String, String> attributes = new HashMap<String, String>();


    /**
     * A Map containing the previously set attributes
     * @return
     */
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    /**
     * Add more attributes to user
     * @param key how it will be saved
     * @param value what will be saved
     * @return in instance of {@link AWSUserAttributes}, like a builder pattern
     */
    public AWSUserAttributes add(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    /**
     * Factory method
     * @return a new instance of {@link AWSUserAttributes}
     */
    public static AWSUserAttributes createBuilder() {
        return new AWSUserAttributes();
    }

    private AWSUserAttributes(){ }
}
