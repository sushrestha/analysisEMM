package memphis.edu.deviceinspectorforemm;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.provider.Settings;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ApplicationInfo;

import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;



public class HomeScreen extends AppCompatActivity {

    //Button declaration
    Button btnUSB;
    Button btnUnknown;
    Button btnMockLocation;
    Button btnRoot;
    Button btnHooking;
    Button btnInstalledApplication;
//    private AppInfo appsList;

    private static final String TAG = HomeScreen.class.getSimpleName();
    public final static String EXTRA_MESSAGE = "memphis.edu.deviceinspectorforemm.MESSAGE";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //USB detection
        btnUSB = (Button) findViewById(R.id.btnUSB);
        btnUSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                boolean flag = detectUSB();
                if (flag) {
                    message = getString(R.string.warning_usbDebugging);
                } else {
                    message = getString(R.string.ok_usbDebugging);
                }
                displayResult(message, v);
            }
        });

        // for Unknown sources detection
        btnUnknown = (Button) findViewById(R.id.btnUnkown);
        btnUnknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                boolean flag = detectUnknown();
                if (flag) {
                    message = getString(R.string.warning_unknownSource);
                } else {
                    message = getString(R.string.ok_unknownSource);
                }
                displayResult(message, v);
            }
        });


        // for Mock Location detection
        btnMockLocation = (Button) findViewById(R.id.btnMockLocation);
        btnMockLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                boolean flag = detectMockLocation();
                if (flag) {
                    message = getString(R.string.warning_mockLocation);
                } else {
                    message = getString(R.string.ok_mockLocation);
                }
                displayResult(message, v);
            }
        });

        // for Root detection
        btnRoot = (Button) findViewById(R.id.btnRoot);
        btnRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                boolean flag = detectRoot();
                if (flag) {
                    message = getString(R.string.warning_rootDetection);
                } else {
                    message = getString(R.string.ok_rootDetection);
                }
                displayResult(message, v);
            }
        });

        // for Hooking detection
        btnHooking = (Button) findViewById(R.id.btnHooking);
        btnHooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                boolean flag = detectHooking();
                if (flag) {
                    message = getString(R.string.warning_hookingDetection);
                } else {
                    message = getString(R.string.ok_hookingDetection);
                }
                displayResult(message, v);
            }
        });

        // for getting all installed applications
        btnInstalledApplication = (Button) findViewById(R.id.btnInstalledApplication);
        btnInstalledApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                ArrayList<AppInfo> apps = new ArrayList<>(checkInstalledPackageLists().values());
                message = message + "Name---        Package Name \n";
                for (AppInfo app : apps) {
                    Log.i(TAG, "----------------------------------------------");
                    Log.i(TAG, app.getName() + "-------" + app.getPackageName());
                    message = message + app.getName() + "---" + app.getName() + "---" + app.getVersion() + "\n";
                    message = message + "Permission lists: \n";
                    for (String permission : app.getReqPermissions()) {
                        message = message + permission + "\n";
                        Log.i(TAG, permission);
                    }
                }
                displayResult(message, v);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /* Another Activity that displays the result  */
    public void displayResult(String result, View v) {

        Intent myIntent = new Intent(v.getContext(), DisplayMessageActivity.class);
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        myIntent.putExtra(EXTRA_MESSAGE, result);
        startActivity(myIntent);

    }

    /* a method for detecting unknown source */
    boolean detectUnknown() {
        Log.i(TAG, "Unknown Source Enable: " + Settings.Secure.getString(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS));
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS).equals("1");
    }

    /* a method for detecting USB */
    boolean detectUSB() {
        Log.i(TAG, "USB Enable: " + Settings.System.getString(getContentResolver(), Settings.System.ADB_ENABLED));
        return Settings.System.getString(getContentResolver(), Settings.System.ADB_ENABLED).equals("1");
    }

    /* a method for detecting mock location allow */
    boolean detectMockLocation() {
        Log.i(TAG, "Mock Location Enable: " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION));
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("1");
    }

    /* a method for detecting root */
    boolean detectRoot() {

        /* check for
            Build.tags
            Over The Air (OTA) certs
            Installed Files and Packages
                Superuser.apk
                Any Chainfire package
                Cyanogenmod.superuser
                Kingo Root
                SU Binaries
             Directory Permission
             Executing Commands su and id
             BusyBox installation
         */
        return checkBuildTags() | checkFileExists() | checkBlackListApp();
    }

    boolean checkBlackListApp() {

        String[] blackListApps = {
                "eu.chainfire.supersu",
                "com.noshufou.android.su",
                "com.thirdparty.superuser",
                "eu.chainfire.supersu",
                "com.koushikdutta.superuser",
                "com.zachspong.temprootremovejb",
                "com.ramdroid.appquarantine",
                "com.kingoapp.link"
        };

        //get all installed applications
        ArrayList<AppInfo> apps = new ArrayList<>(checkInstalledPackageLists().values());

        //filter the blacklist
        for(AppInfo app: apps){
            String packageName = app.getPackageName();
            for(int i =0; i<blackListApps.length; i++){
                if(packageName.equals(blackListApps[i])){
                    Log.i(TAG,packageName);
                    return true;
                }
            }
        }
        return false;
    }

    /* Returns the Hash map of Each application installed */
    Map<String, AppInfo> checkInstalledPackageLists() {
        Map<String, AppInfo> appList = new HashMap<>();
        AppInfo app;
        PackageManager pm = getPackageManager();
        final List<PackageInfo> packages = pm.getInstalledPackages(pm.GET_PERMISSIONS);
        Log.i(TAG, "Installed Application Information:");
        for (PackageInfo packageInfo : packages) {
            if (!isSystemPackage(packageInfo)) {
                app = new AppInfo();
                app.setName(packageInfo.applicationInfo.loadLabel(pm).toString());
                app.setPackageName(packageInfo.packageName);
                app.setVersion(packageInfo.versionName);
                String[] reqPermissions = packageInfo.requestedPermissions;
                if (packageInfo.requestedPermissions != null) {
                    for (int i = 0; i < reqPermissions.length; i++) {
                        app.setReqPermissions(reqPermissions[i]);
                    }
                }
                appList.put(packageInfo.packageName, app);
            }
        }
        return appList;
    }

    /*Check if app is system app */
    private boolean isSystemPackage(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }


    boolean checkBuildTags() {
        //build.tags = "test-keys" custom ROM
        boolean tagFlag = false;
        String buildTag = Build.TAGS;
        if (!(buildTag.isEmpty()) && (buildTag.equals("test-keys"))) {
            tagFlag = true;
            Log.i(TAG, "otacerts.zip not exists");
        }
        return tagFlag;
    }

    boolean checkFileExists() {

        String msg = "Root: by File Exists: ";

        //OTA certs must exist
        boolean otaCertsFlag = false;
        if (!(new File("/etc/security/otacerts.zip").exists())) {
            otaCertsFlag = true;
            msg = msg + " Ocerts.zip: " + otaCertsFlag;
        }
        Log.i(TAG, msg);
        return otaCertsFlag;
    }

    /* a method for detecting hooking */
    boolean detectHooking() {

        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "HomeScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://memphis.edu.deviceinspectorforemm/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "HomeScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://memphis.edu.deviceinspectorforemm/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
