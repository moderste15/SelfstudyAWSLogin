package org.modi.mobileanimation.awslogin.login;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.modi.mobileanimation.awslogin.R;

/**
 * Created by admin on 09.04.2018.
 */

public class AWSXMasTreeDecorator {



    // TODO
    public void hangOrnaments(Activity activity, LinearLayout rootLayout) {

        TextView text = (TextView) activity.getLayoutInflater().inflate(R.layout.test_textview, rootLayout, false);
        text.setText("This is a test");
        rootLayout.addView(text);
    }



    private static AWSXMasTreeDecorator instance;

    public static AWSXMasTreeDecorator getInstance() {
        if (instance == null)
            instance = new AWSXMasTreeDecorator();
        return instance;
    }

    protected AWSXMasTreeDecorator() {}

}
