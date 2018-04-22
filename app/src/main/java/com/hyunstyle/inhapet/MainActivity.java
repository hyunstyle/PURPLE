package com.hyunstyle.inhapet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hyunstyle.inhapet.adapter.MainViewPagerAdapter;
import com.hyunstyle.inhapet.dialog.LoadingDialog;
import com.hyunstyle.inhapet.fragment.InsideSchoolFragment;
import com.hyunstyle.inhapet.fragment.OutsideSchoolFragment;
import com.hyunstyle.inhapet.fragment.CalculationFragment;
import com.hyunstyle.inhapet.fragment.ResultFragment;
import com.hyunstyle.inhapet.fragment.SettingsFragment;
import com.hyunstyle.inhapet.interfaces.AsyncTaskResponse;
import com.hyunstyle.inhapet.model.Restaurant;
import com.hyunstyle.inhapet.service.ServiceView;
import com.hyunstyle.inhapet.thread.DataDownloadingThread;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.security.SecureRandom;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AsyncTaskResponse{

    private static final int OVERLAY_PERMISSION_CODE = 100;
    private static final int LOCATION_PERMISSION_CODE = 200;

    private Context context;
    private BottomNavigationView bottomNavigationView;

    private LinearLayout introLayout;

    private MainViewPager viewPager;
    private MainViewPagerAdapter adapter;

    // about back pressed.
    private Stack<Integer> pageStack = new Stack<>();
    private boolean isBackStackPage = false;
    private long backPressedTime;

    private Fragment foregroundFragment;
    private FragmentManager fragmentManager;

    private String uniqueId;

    private Realm realm;

    private LoadingDialog loadingDialog;


    public MainViewPager getViewPager() {
        return viewPager;
    }

    public MainViewPagerAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        viewPager = findViewById(R.id.main_view_pager);
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        introLayout = findViewById(R.id.intro_layout);

        Log.e("path", getFilesDir().toString());
//        SharedPreferences pref = getSharedPreferences(getResources().getString(R.string.first_execution), Context.MODE_PRIVATE);
//        boolean first = pref.getBoolean(getResources().getString(R.string.first_execution), false);

        if(getUniqueUUID(context)) {
            Log.e("great", "greaT~");

        } else { // 처음 실행시
            loadingDialog = new LoadingDialog(context);
            loadingDialog.show();

            new DataDownloadingThread(this).
                    execute(getResources().getString(R.string.downloadURL), getResources().getString(R.string.client), uniqueId);
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationAnimateHelper.disableShiftMode(bottomNavigationView);
        fragmentManager = getSupportFragmentManager();
        setViewPager(viewPager);

        Button button = findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimalService();
            }
        });
    }

    private Realm getRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(getResources().getString(R.string.data_folder))
                .encryptionKey(getKey().getBytes())
                .schemaVersion(1L)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        // Start with a clean slate every time
        Realm.deleteRealm(config);

        return Realm.getInstance(config);
    }

    private void setViewPager(MainViewPager pager) {

        //pager.setOffscreenPageLimit(2);

        OutsideSchoolFragment outsideSchoolFragment = new OutsideSchoolFragment();
        //ResultFragment resultFragment = new ResultFragment();
        CalculationFragment calculationFragment = new CalculationFragment();
        InsideSchoolFragment insideSchoolFragment = new InsideSchoolFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        adapter.addFragment(outsideSchoolFragment);
        //adapter.addFragment(resultFragment); // hidden fragment
        adapter.addFragment(calculationFragment);
        adapter.addFragment(insideSchoolFragment);
        adapter.addFragment(settingsFragment);

        pager.setAdapter(adapter);
    }

    private void fadeOutIntroView(View v) {
        final Animation fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("alive", "alive");
                v.startAnimation(fadeOutAnim);
            }
        }, 0);
    }

    @Override
    protected void onResume() {

//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        if(permissionCheck== PackageManager.PERMISSION_DENIED){
//            // 권한 없음
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
//        }else{
//            // 권한 있음
//            Log.d("auth", "already");
//        }


        //fadeOutIntroView(introLayout);

        // 처음 시작화면을 중앙 아이템으로
        //bottomNavigationView.setSelectedItemId(R.id.navigation_inside_school);
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOCATION_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if(grantResult == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "권한허용", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "권한 거절", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (pageStack.size() > 1) {
            pageStack.pop();
            isBackStackPage = true;
            bottomNavigationView.setSelectedItemId(pageStack.lastElement());
        } else {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && intervalTime <= 2000L) {
                super.onBackPressed();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "뒤로 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("ondestroy", "main");
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startAnimalService() {
        if ((Build.VERSION.SDK_INT >= 23)) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
            } else if (Settings.canDrawOverlays(context)) {
                startService(new Intent(context, ServiceView.class));
            }
        } else {
            startService(new Intent(context, ServiceView.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            startService(new Intent(context, ServiceView.class));
            Log.d("here", "activityresult");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_outside_school:
                viewPager.setCurrentItem(0);
                //foregroundFragment = new OutsideSchoolFragment();
                //bottomNavigationView.setSelectedItemId(0);
                break;
            case R.id.navigation_calculation:
                viewPager.setCurrentItem(1); // Entire List is hidden.
                //foregroundFragment = new CalculationFragment();
                break;
            case R.id.navigation_inside_school:
                viewPager.setCurrentItem(2);
                //foregroundFragment = new InsideSchoolFragment();
                break;
            case R.id.navigation_information:
                viewPager.setCurrentItem(3);
                //foregroundFragment = new ResultFragment();
                break;
        }

        //백스택으로 실행된 프래그먼트는 스택에 다시 안넣기 위해서
        if(!isBackStackPage)
            pageStack.push(item.getItemId());
        else
            isBackStackPage = false;

//        final FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragment_layout, foregroundFragment).commit();

        return true;
    }

    public synchronized boolean getUniqueUUID(Context context) {

        SharedPreferences sharedPrefs = context.getSharedPreferences(
                getResources().getString(R.string.first_execution), Context.MODE_PRIVATE);
        uniqueId = sharedPrefs.getString(getResources().getString(R.string.first_execution), null);
        String k = sharedPrefs.getString(getResources().getString(R.string.ks), null);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        if(k == null) {
            byte[] ka = new byte[32];
            new SecureRandom().nextBytes(ka);

            String ks = Util.bytesToHex(ka, getResources().getString(R.string.hex));
            //String ks = new String(ka, 0, ka.length);
            setKey(ks);
            Log.e("keyy", ks);
        }

        if (uniqueId == null) {
            uniqueId = UUID.randomUUID().toString();
            editor.putString(getResources().getString(R.string.first_execution), uniqueId);
            editor.apply();
            Log.d("null", "unique id null");
            return false;
        } else {
            Log.d("not null", "already have unique id");
            return true;
        }


    }


    private void setKey(String k) {

        SharedPreferences sharedPrefs = context.getSharedPreferences(
                getResources().getString(R.string.first_execution), Context.MODE_PRIVATE);

        sharedPrefs.edit().putString(getResources().getString(R.string.ks), k).apply();
    }

    private String getKey() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                getResources().getString(R.string.first_execution), Context.MODE_PRIVATE);

        return sharedPrefs.getString(getResources().getString(R.string.ks), null);
    }

    @Override
    public void finished(@NotNull JSONArray output) {

        Gson gson = new GsonBuilder().create();

        List<Restaurant> r = gson.fromJson(output.toString(), new TypeToken<List<Restaurant>>() {
        }.getType());

        Log.e("list", "" + r.get(1).getFamousMenu());

        // Open the Realm with encryption enabled
        realm = getRealm();
        realm.executeTransaction(t -> realm.insertOrUpdate(r));

        loadingDialog.dismiss();
    }
}