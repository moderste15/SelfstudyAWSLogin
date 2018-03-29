package org.modi.mobileanimation.awslogin.login;

/**
 * Created by Stefan
 * Interface for logins
 * With thanks to wtmimura.com
 */

public interface AWSLoginHandler {


    void onRegisterSuccess(boolean confirmToComplete);


    void onRegisterConfirmed();


    void onSignInSuccess();


    void onFailure(Exception exception);

}
