package vn.com.gojobs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    private static int SPLAT_TIME_OUT=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(IntroActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLAT_TIME_OUT);
    }
}
