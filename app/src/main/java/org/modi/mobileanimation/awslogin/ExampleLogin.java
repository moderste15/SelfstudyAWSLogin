package org.modi.mobileanimation.awslogin;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.modi.mobileanimation.awslogin.analytics.AWSEvent;

import java.util.HashMap;
import java.util.Map;

public class ExampleLogin extends AppCompatActivity {

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_login);

        // Everything regarding layout belongs here
        setupLayout();

        // Listeners (SignUI is set here)
        setupListeners();

        // Amazonaws
        AWSUtility.doStart(this);

        exampleAnalytics1();
        exampleAnalytics2();


    }





    /**
     * Called from on create to create example event and submit it to pinpoint analytics
     */
    private void exampleAnalytics1() {
        final String en = "ExampleEvent";
        Map<String, String> a = new HashMap<String, String>();
        a.put("FirstDemoAttribute", "My Value is a String");
        a.put("SecondDemoAttribute", "42");
        Map<String, Double> m = new HashMap<String, Double>();
        m.put("Value1", 42.00);
        m.put("Value2", 3.14157);


        AWSUtility.doBeginAnalytics(this)
                .logEvent(en, a, m)
                .submit();
    }

    /**
     * Better version of exampleAnalytics1
     */
    private void exampleAnalytics2() {

        AWSUtility.doBeginAnalytics(this)
                .logEvent(
                        AWSEvent.initialise()
                        .setEventName("BetterEvent")
                        .addAttributes("Very", "Easy")
                        .addMetrics("Grade", 1.0))
                .submit();
    }


    /**
     * Call from onCreate to setup relevant listeners
     */
    private void setupListeners() {
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    /**
     * Called from listern makes SignUi screen
     */
    private void login() {
        final AuthUIConfiguration config = new AuthUIConfiguration.Builder()
                .userPools(true)
                .logoResId(R.drawable.ic_aws_logo_smile_reversed)
                .backgroundColor(Color.parseColor("#222e3c"))
                .isBackgroundColorFullScreen(true)
                .canCancel(true)
                .build();
        AWSUtility.doLoginActivityUI(this, config, ExampleSuccess.class);
    }


    /**
     * Call from on onCreate to setup layout
     * further abstract onCreate and enhance readability
     */
    private void setupLayout() {
        constraintLayout = findViewById(R.id.example_login_cl);

        // Gilde background animation
        Glide.with(this)
                .asGif()
                .load(R.drawable.dark_bg)
                .into(new SimpleTarget<GifDrawable>() {
                    @Override
                    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                        constraintLayout.setBackground(resource);
                        resource.start();
                    }
                });
    }
}
