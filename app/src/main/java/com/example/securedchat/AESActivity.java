package com.example.securedchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESActivity extends AppCompatActivity {
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
    private static String pwdtext="qwerty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_e_s);
        messageBox=(EditText)findViewById(R.id.messageBox);
        messageeBox_key=(EditText)findViewById(R.id.messageBox2);
        encrypt=(Button)findViewById(R.id.button_encrypt);
        decrypt=(Button)findViewById(R.id.button_decrypt);
        clear=(Button)findViewById(R.id.button_clear);
        outputString=(TextView)findViewById(R.id.output_text);
        outputString.setText("");
        voiceSearch=(ImageView)findViewById(R.id.voice_type);
        voiceSearch_key=(ImageView)findViewById(R.id.voice_type2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //
                    String key_from_user=messageeBox_key.getText().toString();
                    if(key_from_user.length()>0)
                    {
                        pwdtext=key_from_user;
                    }
                    //
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
                    //
                    String key_from_user=messageeBox_key.getText().toString();
                    if(key_from_user.length()>0)
                    {
                        pwdtext=key_from_user;
                    }
                    //
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
                    Toast.makeText(AESActivity.this,"This feature is not supported in your device",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AESActivity.this,"This feature is not supported in your device",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String decrypt(String inputText, String pwdtext) {
        String result="";
        try {
            SecretKeySpec keySpec=generateKey(pwdtext);
            Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE,keySpec);
            byte[] decryptedBytes=Base64.decode(inputText,Base64.DEFAULT);
            byte[] cipherText=cipher.doFinal(decryptedBytes);
            String decryptValue=new String(cipherText,"UTF-8");
            result=decryptValue;
            return result;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String encrypt(String input,String pwdString) {
        String result="";
        try {
            SecretKeySpec key = generateKey(pwdString);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] cipherText=cipher.doFinal(input.getBytes());
            result=Base64.encodeToString(cipherText,Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static SecretKeySpec generateKey(String pwdString) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes=pwdString.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length); //It will process the array bytes
        byte[] key=digest.digest();  //completes the hash computation
        SecretKeySpec keySpec=new SecretKeySpec(key,"AES");
        return keySpec;
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