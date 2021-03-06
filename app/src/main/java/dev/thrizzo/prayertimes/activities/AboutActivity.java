package dev.thrizzo.prayertimes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import dev.thrizzo.prayertimes.R;

import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar myToolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(myToolbar);
    }
}
