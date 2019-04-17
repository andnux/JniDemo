package top.andnux.jnidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JniNatice jniNatice = new JniNatice();
        jniNatice.accessStaticField();
        TextView view = findViewById(R.id.sample_text);
        view.setText(String.valueOf(jniNatice.chineseChars()));
        jniNatice.accessNonvirtualMethod();
        int[] array = jniNatice.getArray();
        for (int i : array) {
            Log.e("TAG", " " + i);
        }
        jniNatice.localReference();
        jniNatice.createGlobalReference();
        Log.e("TAG", jniNatice.getGlobalReference());
        jniNatice.releaseGlobalReference();
//        Log.e("TAG", jniNatice.getGlobalReference());
        try {
            Log.e("TAG", jniNatice.exception());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("TAG", "===================");
    }
}
