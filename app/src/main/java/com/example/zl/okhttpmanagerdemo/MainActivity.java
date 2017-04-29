package com.example.zl.okhttpmanagerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        final TextView textView = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpManager okHttpManager = OkHttpManager.getInstance();
                okHttpManager.asyncJsonStringByURL("http://v.juhe.cn/toutiao/index?type=&key=6236cb786e3eacfb5089450980189bee", new OkHttpManager.Func1() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("sout",result);
                        textView.setText(result);
                    }
                });
            }
        });
    }
}
