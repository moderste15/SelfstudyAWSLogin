package org.modi.mobileanimation.awslogin.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Stefan
 */

public class AWSAuthentification {


    // Members
    private CognitoUserPool cognitoUserPool;
    private CognitoUser cognitoUser;
    private AWSLoginHandler loginHandler;
    private Context context;

    // control variables
    private String userName, userPassword;

    private static boolean loggedIn = false;

    // Constants
    private static final String LOG_TAG = AWSAuthentification.class.getSimpleName();
    private static final String ATTR_EMAIL = "email";
    private static String SHARED_PREFERENCE;
    private static final String PREFERENCE_USER_NAME = "awsUserName";
    private static final String PREFERENCE_USER_EMAIL = "awsUserEmail";

    /*
     * Error and Info messages
     */
    private static final String NO_USERPOOL = "Could not create user pool due to JSON exception";
    private static final String AUTH_HANDLER = "Could not authenticate user";
    private static final String REGISTRATION_SUCCESS = "Registration was a success";
    private static final String REGISTRATION_FAIL = "Registration failed";


    private final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            // Get details of the logged user (in this case, only the e-mail)
            cognitoUser.getDetailsInBackground(new GetDetailsHandler() {
                @Override
                public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                    // Save in SharedPreferences
                    SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
                    String email = cognitoUserDetails.getAttributes().getAttributes().get(ATTR_EMAIL);
                    editor.putString(PREFERENCE_USER_EMAIL, email);
                    editor.apply();
                }

                @Override
                public void onFailure(Exception exception) {
                    exception.printStackTrace();
                }
            });

            // Save in SharedPreferences
            SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
            editor.putString(PREFERENCE_USER_NAME, userName);
            editor.apply();
            loginHandler.onSignInSuccess();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {

        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
            // No MFA
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            // No challenge needed
        }

        @Override
        public void onFailure(Exception exception) {
            error(AUTH_HANDLER);
            exception.printStackTrace();
        }
    };


    /**
     * Register an new user with username, email an d password
     * the password has to conform to password policy set in AWS -> MobileHub -> Cognito (or AWS -> Cognito)
     * @param userName userName to be used
     * @param userEmail email address as verification
     * @param userPassword password
     * @param moreAttributes additional attributes
     */
    public void registerUser(String userName, String userEmail, String userPassword, AWSUserAttributes moreAttributes) {
        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        userAttributes.addAttribute(ATTR_EMAIL, userEmail);

        for (Map.Entry<String, String> a : moreAttributes.getAttributes().entrySet())
            userAttributes.addAttribute(a.getKey(), a.getValue());

        final SignUpHandler signUpHandler = new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                cognitoUser = user;
                loginHandler.onRegisterSuccess(!signUpConfirmationState);
            }

            @Override
            public void onFailure(Exception exception) {
                loginHandler.onFailure(exception);
            }
        };

        cognitoUserPool.signUpInBackground(userName, userPassword, userAttributes, null, signUpHandler);
    }

    /**
     * Confirm the registration or replay why it failed
     * @param confirmationCode
     */
    public void confirmRegistration(String confirmationCode) {
        info(confirmationCode);
        final GenericHandler confirmationHandler = new GenericHandler() {
            @Override
            public void onSuccess() {
                info(REGISTRATION_SUCCESS);
                loginHandler.onRegisterConfirmed();
            }

            @Override
            public void onFailure(Exception exception) {
                error(REGISTRATION_FAIL);
                loginHandler.onFailure(exception);
            }
        };
        cognitoUser.confirmSignUpInBackground(confirmationCode, false, confirmationHandler);
    }


    /**
     * Log me in before you go go..
     * oh and save me to {@link SharedPreferences}
     * @param userNameOrEmail
     * @param userPassword
     */
    public void signInUser(String userNameOrEmail, String userPassword) {
        this.userName = userNameOrEmail;
        this.userPassword = userPassword;

        cognitoUser = cognitoUserPool.getUser(userName);

        // Opens session and puts the user in SharedPreferences
        cognitoUser.getSessionInBackground(authenticationHandler);
    }

    /**
     * Get the user name from {@link SharedPreferences}
     * @param context application context
     * @return
     */
    public static String getSavedUserName(Context context) {
        SharedPreferences savedValues = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return savedValues.getString(PREFERENCE_USER_NAME, "");
    }


    /**
     * Get email from {@link SharedPreferences}
     * @param context application context
     * @return
     */
    public static String getSavedUserEmail(Context context) {
        SharedPreferences savedValues = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return savedValues.getString(PREFERENCE_USER_EMAIL, "");
    }


    /**
     * Uses aws {@link SignInUI} to create a sign in screen
     * Easyer than the rest, but not that customisable
     * @param activity the activity from which this login is called, i.e. the activity on back
     * @param config {@link AuthUIConfiguration}
     * @param next the Activity.class as destination after successful login
     */
    public static void loginActivityUI(final Activity activity,
                                final AuthUIConfiguration config,
                                final Class<? extends AppCompatActivity> next) {
        AWSMobileClient.getInstance().initialize(activity, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(activity, SignInUI.class);
                signin.login(activity, next)
                        .authUIConfiguration(config)
                        .execute();
            }
        }).execute();


    }


    /**
     * Sets login state
     * @param b
     */
    public static void awsSetLogin(boolean b) {
        info("Logged in " + b);
        loggedIn = b;
    }

    /**
     * Gets login state
     * @return
     */
    public static boolean isUserLogedIn() {

        return loggedIn;
    }



        // Auto generated* Singleton
    private static AWSAuthentification instance;

    public static AWSAuthentification getInstance(Context context, AWSLoginHandler callback) {
        if ( instance == null)
            instance = new AWSAuthentification(context, callback);
        return instance;
    }

    /**
     * Constructor (private)
     * @param context application context
     * @param callback {@link AWSLoginHandler} called after login
     */
    private AWSAuthentification(Context context, AWSLoginHandler callback) {
        this.context = context;
        this.loginHandler = callback;

        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        SHARED_PREFERENCE = "AWS_SavedValue-"
                + (stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId));

        initUserPool();
    }





    private void initUserPool() {
        IdentityManager identityManager = IdentityManager.getDefaultIdentityManager();
        try{
            JSONObject myJSON = identityManager.getConfiguration().optJsonObject("CognitoUserPool");
            final String COGNITO_POOL_ID = myJSON.getString("PoolId");
            final String COGNITO_CLIENT_ID = myJSON.getString("AppClientId");
            final String COGNITO_CLIENT_SECRET = myJSON.getString("AppClientSecret");
            final String REGION = myJSON.getString("Region");
            cognitoUserPool = new CognitoUserPool(context, COGNITO_POOL_ID, COGNITO_CLIENT_ID, COGNITO_CLIENT_SECRET, Regions.fromName(REGION));
        } catch (JSONException e) {
            error(NO_USERPOOL);
            e.printStackTrace();
        }
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
}
