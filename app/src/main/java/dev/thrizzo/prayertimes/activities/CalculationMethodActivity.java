package dev.thrizzo.prayertimes.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import dev.thrizzo.prayertimes.R;

public class CalculationMethodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_method);
        Toolbar myToolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(myToolbar);

        final SharedPreferences sp = getSharedPreferences("ABOODY", MODE_PRIVATE);

        int calcMethodIndex = sp.getInt("calcMethodIndex", 0);
        RadioGroup radioGCalcMethod = findViewById(R.id.radioGroupCalcMethod);
        radioGCalcMethod.check(radioGCalcMethod.getChildAt(calcMethodIndex).getId());
        radioGCalcMethod.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("calcMethodIndex", group.indexOfChild(findViewById(checkedId)));
//                    System.out.println(group.indexOfChild(findViewById(checkedId)));
                    edit.commit();
                    sendBroadcast(new Intent("TIMEUPDATE"));

                }
        );


        int asrAdjustIndex = sp.getInt("asrAdjustIndex", 0);
        RadioGroup radioGAsrAdjust = findViewById(R.id.radioGroupAsrAdjust);
        radioGAsrAdjust.check(radioGAsrAdjust.getChildAt(asrAdjustIndex).getId());
        radioGAsrAdjust.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("asrAdjustIndex", group.indexOfChild(findViewById(checkedId)));
//                    System.out.println(group.indexOfChild(findViewById(checkedId)));
                    edit.commit();
                    sendBroadcast(new Intent("TIMEUPDATE"));

                }
        );

        int higherLatAdjustIndex = sp.getInt("higherLatAdjustIndex", 0);
        RadioGroup radioGHigherLatAdjust = findViewById(R.id.radioGroupHigherLatAdjust);
        radioGHigherLatAdjust.check(radioGHigherLatAdjust.getChildAt(higherLatAdjustIndex).getId());
        radioGHigherLatAdjust.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("higherLatAdjustIndex", group.indexOfChild(findViewById(checkedId)));
//                    System.out.println(group.indexOfChild(findViewById(checkedId)));
                    edit.commit();
                    sendBroadcast(new Intent("TIMEUPDATE"));

                }
        );
    }
}