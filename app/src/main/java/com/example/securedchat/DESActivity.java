package com.example.securedchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DESActivity extends AppCompatActivity {
    private EditText messageBox;
    private EditText messageeBox_key;
    private Button encrypt;
    private Button decrypt;
    private Button clear;
    private TextView outputString;
    private String inputText;
    private ImageView voiceSearch;
    private ImageView voiceSearch_key;
    private String outString="";
    private static String pwdtext="This is key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_e_s);
        messageBox=(EditText)findViewById(R.id.messageBox);
        messageeBox_key=(EditText)findViewById(R.id.messageBox2);
        encrypt=(Button)findViewById(R.id.button_encrypt);
        decrypt=(Button)findViewById(R.id.button_decrypt);
        clear=(Button)findViewById(R.id.button_clear);
        outputString=(TextView)findViewById(R.id.output_text);
        voiceSearch=(ImageView)findViewById(R.id.voice_type);
        voiceSearch_key=(ImageView)findViewById(R.id.voice_type2);
        outputString.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String key_from_user=messageeBox_key.getText().toString();
                    if(key_from_user.length()>0)
                    {
                        pwdtext=key_from_user;
                    }
                    inputText=messageBox.getText().toString();
                    outString=encrypt(inputText,pwdtext);
                    outputString.setText(outString);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String key_from_user=messageeBox_key.getText().toString();
                    if(key_from_user.length()>0)
                    {
                        pwdtext=key_from_user;
                    }
                    inputText=messageBox.getText().toString();
                    outString=decrypt(inputText,pwdtext);
                    outputString.setText(outString);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    messageBox.setText("");
                    messageeBox_key.setText("");
                    outputString.setText("");
                    messageBox.setHint("Type a message...");
                    messageeBox_key.setHint("Enter The Key of Your Choice(which you can remeber for later)...");
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
                    Toast.makeText(DESActivity.this,"This feature is not supported in your device",Toast.LENGTH_SHORT).show();
                }
            }
        });
        voiceSearch_key.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(DESActivity.this,"This feature is not supported in your device",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void sendMessage(View view) {
        if(outputString.length()>0)
        {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,outString);
            if(intent.resolveActivity(getPackageManager())!=null)
            {
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(this,"you have not encrypted the message",Toast.LENGTH_SHORT).show();
        }


    }

    private String decrypt(String inputText, String pwdtext) {
        String result=null;
        try {
            SecretKey key=generateKey(pwdtext);
            byte[] value=Base64.decode(inputText.getBytes(),Base64.DEFAULT);
            Cipher cipher=Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] textDecrypted=cipher.doFinal(value);
            result=new String(textDecrypted,"UTF-8");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String encrypt(String inputText, String pwdtext) {
        String result=null;
        try {
            SecretKey key=generateKey(pwdtext);
            byte[] text=inputText.getBytes("UTF-8");
            Cipher cipher=Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] encryptedText=cipher.doFinal(text);
            result= Base64.encodeToString(encryptedText,Base64.DEFAULT);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private SecretKey generateKey(String pwdtext) {
        SecretKey result=null;
        try {
            DESKeySpec keySpec=new DESKeySpec(pwdtext.getBytes("UTF-8"));
            SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");
            SecretKey key=keyFactory.generateSecret(keySpec);
            result=key;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return result;
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
}
