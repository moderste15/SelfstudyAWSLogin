package org.modi.mobileanimation.awslogin;

/**
 * Created by Stefan
 */

public class AWSException extends RuntimeException {

    public AWSException(String message) {
        super(message);
    }

    public AWSException(String message, Throwable cause) {
        super(message, cause);
    }


}
