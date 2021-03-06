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

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar myToolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(myToolbar);


        SharedPreferences sp = getSharedPreferences("ABOODY", MODE_PRIVATE);

        Switch switchNotification = findViewById(R.id.switchNotifications);
        switchNotification.setChecked(sp.getBoolean("notificationEnabled", true));
        switchNotification.setOnCheckedChangeListener((compoundButton, b) -> sp.edit().putBoolean("notificationEnabled", b).commit());

        EditText editTextFajrBef = findViewById(R.id.editTextNumberFajrBefore);
        editTextFajrBef.setText("" + sp.getInt("notificationFajrBefore", 5));
        editTextFajrBef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationFajrBefore", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        EditText editTextFajrAft = findViewById(R.id.editTextNumberFajrAfter);
        editTextFajrAft.setText("" + sp.getInt("notificationFajrAfter", 15));
        editTextFajrAft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationFajrAfter", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        EditText editTextDhohrBef = findViewById(R.id.editTextNumberDhohrBefore);
        editTextDhohrBef.setText("" + sp.getInt("notificationDhohrBefore", 5));
        editTextDhohrBef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationDhohrBefore", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        EditText editTextDhohrAft = findViewById(R.id.editTextNumberDhohrAfter);
        editTextDhohrAft.setText("" + sp.getInt("notificationDhohrAfter", 15));
        editTextDhohrAft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationDhohrAfter", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        EditText editTextAsrBef = findViewById(R.id.editTextNumberAsrBefore);
        editTextAsrBef.setText("" + sp.getInt("notificationAsrBefore", 5));
        editTextAsrBef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationAsrBefore", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        EditText editTextAsrAft = findViewById(R.id.editTextNumberAsrAfter);
        editTextAsrAft.setText("" + sp.getInt("notificationAsrAfter", 15));
        editTextAsrAft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationAsrAfter", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        EditText editTextMaghrebBef = findViewById(R.id.editTextNumberMaghrebBefore);
        editTextMaghrebBef.setText("" + sp.getInt("notificationMaghrebBefore", 5));
        editTextMaghrebBef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationMaghrebBefore", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        EditText editTextMaghrebAft = findViewById(R.id.editTextNumberMaghrebAfter);
        editTextMaghrebAft.setText("" + sp.getInt("notificationMaghrebAfter", 15));
        editTextMaghrebAft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationMaghrebAfter", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        EditText editTextIshaBef = findViewById(R.id.editTextNumberIshaBefore);
        editTextIshaBef.setText("" + sp.getInt("notificationIshaBefore", 5));
        editTextIshaBef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationIshaBefore", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        EditText editTextIshaAft = findViewById(R.id.editTextNumberIshaAfter);
        editTextIshaAft.setText("" + sp.getInt("notificationIshaAfter", 15));
        editTextIshaAft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sp.edit().putInt("notificationIshaAfter", Integer.parseInt(String.valueOf(charSequence.length() == 0 ? "0" : charSequence))).commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }


}