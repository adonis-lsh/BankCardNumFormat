package com.lsh.bankcardnumformat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.lsh.library.BankNumEditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        BankNumEditText bankNumEditText = (BankNumEditText) findViewById(R.id.bankCardNum);
        final EditText editText = (EditText) findViewById(R.id.bankName);

        bankNumEditText.setBankNameListener(new BankNumEditText.BankNameListener() {
            @Override
            public void success(String name) {
                editText.setText(name.toString());
            }

            @Override
            public void failure() {

            }
        });
    }
}
