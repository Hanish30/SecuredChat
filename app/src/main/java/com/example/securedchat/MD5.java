package com.example.securedchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

public class MD5 extends AppCompatActivity {
    private EditText messageBox;
    private Button hash;
    private Button clear;
    private TextView outputString;
    private String inputText;
    private ImageView voiceSearch;
    private String outString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_d5);
        messageBox=(EditText)findViewById(R.id.messageBox);
        hash=(Button)findViewById(R.id.button_hash);
        clear=(Button)findViewById(R.id.button_clear);
        outputString=(TextView)findViewById(R.id.output_text);
        voiceSearch=(ImageView)findViewById(R.id.voice_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        outputString.setText("");
        hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             inputText=messageBox.getText().toString();
             if(inputText.length()>0)
             {
                 outputString.setText(getHash(inputText));
             }
             else
             {
                 Toast.makeText(MD5.this,"Empty Message",Toast.LENGTH_SHORT).show();
             }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    messageBox.setText("");
                    outputString.setText("");
                    messageBox.setHint("Type a message...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        voiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                if(intent.resolveActivity(getPackageManager())!=null)
                {
                    startActivityForResult(intent,10);
                }
                else
                {
                    Toast.makeText(MD5.this,"This feature is not supported in your device",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getHash(String inputText) {
        String generatedPassword=null;
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(inputText.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<messageDigest.length;i++)
            {
                sb.append(Integer.toString(( messageDigest[i]&0xff)+0x100,32).substring(1));
                //pehle widen krra h and baad me 0x100 add krra nd hexadecimal me convert krde
                // //and pehla character utha leta and builder me add krde
            }
            generatedPassword=sb.toString();
            Log.d("SecuraChat","generated password"+generatedPassword);
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 10:
                if(resultCode==RESULT_OK&&data!=null)
                {
                    ArrayList<String> res=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    messageBox.setText(res.get(0));
                }
                break;
        }
    }

    public void sendMessage(View view) {

        if (outputString.length() > 0) {
            Toast.makeText(MD5.this,"Cannot Send Message",Toast.LENGTH_SHORT).show();
        }
    }
}