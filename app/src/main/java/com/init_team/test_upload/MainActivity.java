package com.init_team.test_upload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.UserInfo;
import com.robotemi.sdk.permission.OnRequestPermissionResultListener;
import com.robotemi.sdk.permission.Permission;
import com.robotemi.sdk.sequence.SequenceModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import kotlin.sequences.Sequence;

import static android.os.FileUtils.copy;
import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements
        OnRequestPermissionResultListener
{
    private Button show_button;
    private Button Change2_button;
    private Button change_button;
    private Button Clear_btn;
    private Button sqc_btn;
    private Button location_bt;

//    private int REQUEST_EXTERNAL_STORAGE = 1;
    private int REQUEST_CODE_NORMAL = 0;
    private int REQUEST_CODE_FACE_START = 1;
    private int REQUEST_CODE_FACE_STOP = 2;
    private int REQUEST_CODE_MAP = 3;
    private int REQUEST_CODE_SEQUENCE_FETCH_ALL = 4;
    private int REQUEST_CODE_SEQUENCE_PLAY = 5;
    private int REQUEST_CODE_START_DETECTION_WITH_DISTANCE = 6;
    private int REQUEST_CODE_SEQUENCE_PLAY_WITHOUT_PLAYER = 7;
    private int REQUEST_CODE_GET_MAP_LIST = 8;

    static SharedPreferences settings;
//    private List<Permission> all_permissions;
    private List<String> location;
    private List<SequenceModel> sqc;
    private List<UserInfo> user;
    static SharedPreferences.Editor editor;
    private String password = "123456";
    Robot robot;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String PACKAGE_NAME;
    private static final String[] PERMISSIONS_STORAGE =
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Change2_button = findViewById(R.id.change2_btn);
        sqc_btn = findViewById(R.id.sqc_btn);
        location_bt = findViewById(R.id.location_btn);
        settings = this.getPreferences(MODE_WORLD_WRITEABLE);
        show_button = findViewById(R.id.show_pass);
        Clear_btn = findViewById(R.id.clear_btn);
        change_button = findViewById(R.id.change_pass);
        editor = settings.edit();
//        Log.e("sequence",);
        location_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = robot.getLocations();
                Log.e("location",location.toString());
            }
        });
        sqc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqc = robot.getAllSequences();
                Log.e("Sequence",sqc.toString());
            }
        });

    show_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String val = settings.getString("password","0");
            Toast.makeText(MainActivity.this,(String)val,Toast.LENGTH_SHORT).show();
        }
    });
    change_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editor.putString("password", "999999").commit();
        }
    });
    Change2_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editor.putString("password", "111111").commit();
        }
    });
    Clear_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            editor.clear().commit();
            user = robot.getAllContact();
            Log.e("user",user.toString());
        }
    });


    }
    protected void onStart() {
        super.onStart();
        robot = robot.getInstance(); // get an instance of the robot in order to begin using its features.
        robot.hideTopBar();
        robot.addOnRequestPermissionResultListener(this);
        int result = robot.checkSelfPermission(Permission.SEQUENCE);
        Log.e("permission result", String.valueOf(result));
        robot.requestPermissions(Collections.singletonList(Permission.SEQUENCE),REQUEST_CODE_SEQUENCE_FETCH_ALL);
        sqc = robot.getAllSequences();

    }
    protected void onStop() {
        super.onStop();
        robot.removeOnRequestPermissionResultListener(this);
    }


    @Override
    public void onRequestPermissionResult(@NotNull Permission permission, int i, int i1) {
        Log.e("permission",permission.toString()+String.valueOf(i)+String.valueOf(i1));
    }
}
