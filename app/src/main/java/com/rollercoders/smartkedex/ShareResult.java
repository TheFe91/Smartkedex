/*package com.rollercoders.smartkedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.rollercoders.smartkedex.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TheFe on 01/12/16.
 *

public class ShareResult extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareresult);

        FacebookSdk.sdkInitialize(this);

        getActionBar();

        //TextView textView = (TextView)findViewById(R.id.titleshareresult);
        //textView.setText("Ecco il risultato dei tuoi filtri");

        CallbackManager callbackManager = CallbackManager.Factory.create();
        LoginManager loginManager = LoginManager.getInstance();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        loginManager.logInWithPublishPermissions(this, permissionNeeds);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.err.println("login manager success");
            }

            @Override
            public void onCancel() {
                System.err.println("login manager on cangel");
            }

            @Override
            public void onError(FacebookException error) {
                System.err.println("login manaeger on errro dio !!!");
            }
        });
        Button button = (Button)findViewById(R.id.provaimg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.smartkedex_ic);
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .setCaption("hello everybody from AandL")
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        System.err.println("success " + result.getPostId());
                    }

                    @Override
                    public void onCancel() {
                        System.err.println("on Cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        System.err.println(error.getMessage());
                    }
                });
            }
        });
    }
}*/