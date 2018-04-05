package org.modi.mobileanimation.awslogin.login;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stefan
 */

public class AWSCookieCutterTemplate {

    private Drawable image;
    private Color backgroundColor;


    /*
     * Singleton
     */
    private static  AWSCookieCutterTemplate instance;

    public static AWSCookieCutterTemplate getInstance() {
        if (instance == null)
            instance = new AWSCookieCutterTemplate();
        return instance;
    }

    private AWSCookieCutterTemplate() {

    }

}
