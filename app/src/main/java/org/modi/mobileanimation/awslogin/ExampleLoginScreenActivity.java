package org.modi.mobileanimation.awslogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.modi.mobileanimation.awslogin.login.AWSAuthentification;
import org.modi.mobileanimation.awslogin.login.AWSLoginHandler;
import org.modi.mobileanimation.awslogin.login.AWSUserAttributes;

public class ExampleLoginScreenActivity extends AppCompatActivity implements View.OnClickListener, AWSLoginHandler {

    AWSAuthentification awsLoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_login_screen);

        // instantiating AWSLoginModel(context, callback)
        awsLoginModel = AWSAuthentification.getInstance(this, this);

        if (awsLoginModel.isUserLogedIn()) {
            Intent success = new Intent(ExampleLoginScreenActivity.this, ExampleSuccess.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(success);
        }

        findViewById(R.id.registerButton).setOnClickListener(this);
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.confirmButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                registerAction();
                break;
            case R.id.confirmButton:
                confirmAction();
                break;
            case R.id.loginButton:
                loginAction();
                break;
        }
    }

    @Override
    public void onRegisterSuccess(boolean confirmToComplete) {
        if (confirmToComplete) {
            Toast.makeText(ExampleLoginScreenActivity.this, "Almost done! Confirm code to complete registration", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ExampleLoginScreenActivity.this, "Registered! Login Now!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRegisterConfirmed() {
        Toast.makeText(ExampleLoginScreenActivity.this, "Registered! Login Now!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignInSuccess() {
        ExampleLoginScreenActivity.this.startActivity(new Intent(ExampleLoginScreenActivity.this, ExampleSuccess.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public void onFailure(Exception exception) {
        exception.printStackTrace();
        Toast.makeText(ExampleLoginScreenActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void registerAction() {
        EditText userName = findViewById(R.id.registerUsername);
        EditText email = findViewById(R.id.registerEmail);
        EditText password = findViewById(R.id.registerPassword);

        // do register and handles on interface
        awsLoginModel.registerUser(userName.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                AWSUserAttributes.createBuilder());
    }

    private void confirmAction() {
        EditText confirmationCode = findViewById(R.id.confirmationCode);

        // do confirmation and handles on interface
        awsLoginModel.confirmRegistration(confirmationCode.getText().toString());
    }

    private void loginAction() {

        EditText userOrEmail = findViewById(R.id.loginUserOrEmail);
        EditText password = findViewById(R.id.loginPassword);

        // do sign in and handles on interface
        awsLoginModel.signInUser(userOrEmail.getText().toString(), password.getText().toString());
    }
}
