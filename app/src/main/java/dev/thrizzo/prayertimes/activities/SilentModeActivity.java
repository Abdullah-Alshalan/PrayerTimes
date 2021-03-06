package dev.thrizzo.prayertimes.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import dev.thrizzo.prayertimes.R;

public class SilentModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silent_mode);
        Toolbar myToolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(myToolbar);

        SharedPreferences sp = getSharedPreferences("ABOODY", MODE_PRIVATE);

        Switch switchSilent = findViewById(R.id.switchSilentMode);
        switchSilent.setChecked(sp.getBoolean("silentModeEnabled", false));
        switchSilent.setOnCheckedChangeListener((compoundButton, b) -> sp.edit().putBoolean("silentModeEnabled", b).commit());

        EditText editTextDelay = findViewById(R.id.editTextNumberDelay);
        editTextDelay.setText("" + sp.getInt("silentModeDelay", 0));
        editTextDelay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("silentModeDelay", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText editTextLength = findViewById(R.id.editTextNumberLength);
        editTextLength.setText("" + sp.getInt("silentModeLength", 30));
        editTextLength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("silentModeLength", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}