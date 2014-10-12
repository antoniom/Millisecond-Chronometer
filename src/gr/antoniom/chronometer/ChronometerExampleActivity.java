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
        
        //IF you want to stop your chrono after X seconds or minutes.
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
			public void onChronometerTick(Chronometer chronometer) {
				if (chronometer.getText().toString().equalsIgnoreCase("00:05:0")) { //When reaches 5 seconds.
				    //Define here what happens when the Chronometer reaches the time above.
				    chronometer.stop();
				    Toast.makeText(getBaseContext(),"Reached the end.",
						Toast.LENGTH_LONG).show();
				}
			}
		});
    }
}
