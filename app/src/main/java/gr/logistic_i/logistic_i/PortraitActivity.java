package gr.logistic_i.logistic_i;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.facebook.drawee.backends.pipeline.Fresco;

public abstract class PortraitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        TypefaceProvider.registerDefaultIconSets();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}