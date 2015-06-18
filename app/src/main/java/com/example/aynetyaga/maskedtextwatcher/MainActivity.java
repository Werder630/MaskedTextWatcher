package com.example.aynetyaga.maskedtextwatcher;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText)findViewById(R.id.editText);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        new MaskedTextWatcher(et, "+*(***)***-**-**");
    }
}
