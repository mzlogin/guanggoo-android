package org.mazhuang.guanggoo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.task.AuthCheckTask;

/**
 * @author mazhuang
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkTaskScheduler.getInstance().execute(new AuthCheckTask(new OnResponseListener<String>() {
            @Override
            public void onSucceed(String data) {
                startHome();
            }

            @Override
            public void onFailed(String msg) {
                startHome();
            }
        }));
    }

    private void startHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
