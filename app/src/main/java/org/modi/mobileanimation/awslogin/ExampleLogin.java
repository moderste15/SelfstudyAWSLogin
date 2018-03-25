package org.modi.mobileanimation.awslogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class ExampleLogin extends AppCompatActivity {

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_login);

        // Everything regarding layout belongs here
        setupLayout();

        // Listeners
        setupListeners();

    }


    /**
     * Call from onCreate to setup relevant listeners
     */
    private void setupListeners() {

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExampleLogin.this, ExampleSuccess.class);
                startActivity(i);
            }
        });

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
