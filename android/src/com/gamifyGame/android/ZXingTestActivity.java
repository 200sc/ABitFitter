package com.gamifyGame.android;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.client.android.camera.CameraConfigurationUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public final class ZXingTestActivity extends Activity {

    private static final String TAG = ZXingTestActivity.class.getSimpleName();
    private static final String PACKAGE_NAME = ZXingTestActivity.class.getPackage().getName();


    Context context;
    Bundle thisBundle;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.thisBundle = icicle;
        this.context = context;
        IntentIntegrator integrator = new IntentIntegrator(ZXingTestActivity.this);

        setContentView(R.layout.scaninter);
        findViewById(R.id.get_camera_parameters).setOnClickListener(getCameraParameters);
        findViewById(R.id.restart_app).setOnClickListener(restartApp);
        findViewById(R.id.scan_anything).setOnClickListener(scanAnything);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            int versionCode;
            String versionName;
            try {
                PackageInfo info = getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
                versionCode = info.versionCode;
                versionName = info.versionName;
            } catch (PackageManager.NameNotFoundException ignored) {
                versionCode = 0;
                versionName = "unknown";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(
                    getString(R.string.app_name) + ' ' + versionName + " (" + versionCode + ')');
            builder.setMessage(getString(R.string.about_message));
            builder.setPositiveButton(R.string.ok_button, null);
            builder.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                showDialog(R.string.result_succeeded, result.toString());
                //showDialog(R.string.result_succeeded, "WHERE IS THIS");
            } else {
                showDialog(R.string.result_failed, getString(R.string.result_failed_why));
            }
        }
    }


    private final View.OnClickListener getCameraParameters = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String stats = CameraConfigurationUtils.collectStats(getFlattenedParams());
            writeStats(stats);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, "zxing-external@google.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Camera parameters report");
            intent.putExtra(Intent.EXTRA_TEXT, stats);
            intent.setType("text/plain");
            startActivity(intent);
        }
    };

    private final View.OnClickListener restartApp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), AndroidLauncher.class);
            startActivity(intent);
        }
    };



    private final View.OnClickListener scanAnything = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntentIntegrator integrator = new IntentIntegrator(ZXingTestActivity.this);
            integrator.initiateScan();
        }
    };

    private void showDialog(int title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        final CharSequence tmpMessage = message;
        //builder.setMessage("HI HERE I AM!");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), tmpMessage,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AndroidLauncher.class);
                intent.putExtra("Message", tmpMessage);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Rescan", null);
        builder.show();
    }

    private static CharSequence getFlattenedParams() {
        Camera camera = Camera.open();
        if (camera == null) {
            return null;
        }
        try {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters == null) {
                return null;
            }
            return parameters.flatten();
        } finally {
            camera.release();
        }
    }

    private static void writeStats(String resultString) {
        File cameraParamsFile = new File(Environment.getExternalStorageDirectory().getPath() + "/CameraParameters.txt");
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(cameraParamsFile), Charset.forName("UTF-8"));
            out.write(resultString);
        } catch (IOException e) {
            Log.e(TAG, "Cannot write parameters file ", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.w(TAG, e);
                }
            }
        }
    }

}
