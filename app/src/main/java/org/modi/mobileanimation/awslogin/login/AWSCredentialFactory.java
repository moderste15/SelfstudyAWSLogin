package org.modi.mobileanimation.awslogin.login;

import android.app.Activity;
import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.config.AWSConfiguration;

/**
 * Created by Stefan
 */

public class AWSCredentialFactory {

    private CognitoCachingCredentialsProvider provider;


    /**
     * Gets a minimal single instance of {@link CognitoCachingCredentialsProvider}
     * @param context
     * @return provider
     */
    public CognitoCachingCredentialsProvider getProvider(Context context) {
        if (provider == null)
            provider = new CognitoCachingCredentialsProvider(context, new AWSConfiguration(context));
        return provider;
    }


    private AWSCredentialFactory() {};
    private static AWSCredentialFactory instance;

    public static AWSCredentialFactory getInstance() {
        if (instance == null)
            instance = new AWSCredentialFactory();
        return instance;
    }
}
