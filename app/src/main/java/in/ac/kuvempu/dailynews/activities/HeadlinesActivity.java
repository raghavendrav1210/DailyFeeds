package in.ac.kuvempu.dailynews.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.ac.kuvempu.dailynews.R;

public class HeadlinesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Test");
        }
    }
}
