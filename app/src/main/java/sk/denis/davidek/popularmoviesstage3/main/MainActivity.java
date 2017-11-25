package sk.denis.davidek.popularmoviesstage3.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import sk.denis.davidek.popularmoviesstage3.R;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    private String TAG = "PERMISSIONS TAG";

    public static boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isAllPermissionsGranted();

        if (findViewById(R.id.rl_recipe_step_instruction) != null) {
            isTwoPane = true;
            Log.e("TWO PANE ACTIVITY ", String.valueOf(isTwoPane));
            if (savedInstanceState == null) {

                Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
            }
        }

    }


    public boolean isAllPermissionsGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.
                    permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted > 23");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v(TAG, "Permission is granted < 23");
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);

        } else {
            isAllPermissionsGranted();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(int position) {
        if (isTwoPane) {
            Toast.makeText(getApplicationContext(),"It is two pane UI + " + position, Toast.LENGTH_SHORT).show();
        } else{

        }
    }
}
