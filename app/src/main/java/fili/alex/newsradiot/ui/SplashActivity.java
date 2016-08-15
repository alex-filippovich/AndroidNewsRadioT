package fili.alex.newsradiot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fili.alex.newsradiot.data.NewsDataRepository;
import fili.alex.newsradiot.ui.MainActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            NewsDataRepository.getInstance().initialize();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 300);

    }
}
