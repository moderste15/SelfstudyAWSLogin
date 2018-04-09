package org.modi.mobileanimation.awslogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;


import org.modi.mobileanimation.awslogin.login.AWSXMasTreeDecorator;

public class AWSXMasTreeLogin extends AppCompatActivity {


    private AWSXMasTreeDecorator decorator;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awschristmas);

        rootLayout = findViewById(R.id.test);

        decorator = AWSXMasTreeDecorator.getInstance();

        decorator.hangOrnaments(this, rootLayout);






    }
}
