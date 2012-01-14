package gr.antoniom.chronometer;

import android.app.Activity;
import android.os.Bundle;

public class ChronometerExampleActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Chronometer mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.start();        
    }
}