package net.comeshare.firetvclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.comeshare.firetvclock.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = getLayoutInflater().inflate(R.layout.activity_main,null);
        v.setKeepScreenOn(true);
        setContentView(v);
    }
}
