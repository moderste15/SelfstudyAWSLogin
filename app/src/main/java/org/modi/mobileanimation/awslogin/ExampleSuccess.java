package org.modi.mobileanimation.awslogin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class ExampleSuccess extends AppCompatActivity {

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_success);


        // Everything regarding layout belongs here
        setupLayout();

    }




    /**
     * Call from on onCreate to setup layout
     * further abstract onCreate and enhance readability
     */
    private void setupLayout() {

        constraintLayout = findViewById(R.id.example_success_cl);

        Glide.with(this)
                .asGif()
                .load(R.drawable.light_bg)
                .into(new SimpleTarget<GifDrawable>() {
                    @Override
                    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                        constraintLayout.setBackground(resource);
                        resource.start();
                    }
                });

    }
}
