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
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAActivity extends AppCompatActivity {

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
    private KeyPairGenerator kpg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_s_a);
        messageBox=(EditText)findViewById(R.id.messageBox);
        messageeBox_key=(EditText)findViewById(R.id.messageBox2);
        encrypt=(Button)findViewById(R.id.button_encrypt);
        decrypt=(Button)findViewById(R.id.button_decrypt);
        clear=(Button)findViewById(R.id.button_clear);
        outputString=(TextView)findViewById(R.id.output_text);
        voiceSearch=(ImageView)findViewById(R.id.voice_type);
        voiceSearch_key=(ImageView)findViewById(R.id.voice_type2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        outputString.setText("");
        KeyPair kp=getKeyPair();
        PublicKey publicKey=kp.getPublic();
        final byte[] publicKeyBytes=publicKey.getEncoded();
        final String publicKeyBytesBase64=new String(Base64.encode(publicKeyBytes,Base64.DEFAULT));
        PrivateKey privateKey=kp.getPrivate();
        final byte[] privateKeyBytes=privateKey.getEncoded();
        final String privateKeyBytesBase64=new String(Base64.encode(privateKeyBytes,Base64.DEFAULT));
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
                    outString=encryptToRsa(inputText,publicKeyBytesBase64);
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
                    outString=decryptToRsa(inputText,privateKeyBytesBase64);
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
                    Toast.makeText(RSAActivity.this,"This feature is not supported in your device",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RSAActivity.this,"This feature is not supported in your device",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String decryptToRsa(String inputText, String privateKeyBytesBase64) {
        String decryptedString = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyBytesBase64.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePrivate(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encryptedBytes = Base64.decode(inputText, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }

    private String encryptToRsa(String inputText, String publicKeyBytesBase64) {
        String encryptedBytesBase64="";
       try {
           KeyFactory keyFac = KeyFactory.getInstance("RSA");
           KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKeyBytesBase64.trim().getBytes(), Base64.DEFAULT));
           Key key = keyFac.generatePublic(keySpec);
           final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
           // encrypt the plain text using the public key
           cipher.init(Cipher.ENCRYPT_MODE, key);

           byte[] encryptedBytes = cipher.doFinal(inputText.getBytes("UTF-8"));
           encryptedBytesBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
       } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
               | UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
           e.printStackTrace();
       }
        return encryptedBytesBase64.replaceAll("(\\r|\\n)", "");
    }

    private KeyPair getKeyPair() {
       KeyPair kp=null;
       try {
           kpg = KeyPairGenerator.getInstance("RSA");
           kpg.initialize(2048);
           kp = kpg.genKeyPair();
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return kp;
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
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,inputText);
            if(intent.resolveActivity(getPackageManager())!=null)
            {
                startActivity(intent);
            }
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