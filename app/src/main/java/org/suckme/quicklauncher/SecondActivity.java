package org.suckme.quicklauncher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if(getIntent().hasExtra("org.suckme.quicklauncher.Something"))
        {
            /*tv is the text view that will be sitting there in the middle of the activity*/
            TextView tv = (TextView)findViewById(R.id.textView);
            /*get the value from the extras of the intent passed into this*/
            String text = getIntent().getExtras().getString("org.suckme.quicklauncher.Something");
            /*Set the text to whatever the key was*/
            tv.setText(text);
        }
    }
}
