package org.suckme.quicklauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button secondActivityBtn = (Button)findViewById(R.id.secondActivityBtn);
        Button googleActivityBtn = (Button)findViewById(R.id.googleActivityBtn);

        /*This represents attempts to launch an activity within our own app*/
        secondActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Linking the intent and the new activity together*/
                Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);
                startIntent.putExtra("org.suckme.quicklauncher.Something", "Hello, World!");
                /*Begins starting the activity with name startIntent*/
                startActivity(startIntent);
            }
        });

        /*This represents an attempt to launch an activity outside the app*/
        googleActivityBtn.setOnClickListener(new View.OnClickListener()  {
            @Override
            /*Creation of the onClick event*/
            public void onClick(View view)
            {
                String google = "http://www.google.com";
                Uri webaddress = Uri.parse(google);
                /*Sets a new intent called go to google and it basically asks the device if there are
                 *any apps to respond to the address to make this action intent*/
                Intent goToGoogle = new Intent(Intent.ACTION_VIEW, webaddress);
                /*Checks to see if the action manager allows us to go to google*/
                if(goToGoogle.resolveActivity(getPackageManager())!=null)
                {
                    startActivity(goToGoogle);
                }
            }
        });
    }
}
