package com.hyunstyle.inhapet.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherData;
import com.github.matteobattilana.weather.WeatherView;
import com.hyunstyle.inhapet.MainActivity;
import com.hyunstyle.inhapet.R;
import com.hyunstyle.inhapet.thread.BackgroundThread;
import com.hyunstyle.inhapet.thread.NotificationHandler;

import java.util.Random;

/**
 * Created by SangHyeon on 2018-03-01.
 */

public class ServiceView extends Service implements View.OnTouchListener{
    // about clickable animal
    private ImageView animalView;
    private ImageView closeView;
    private WindowManager.LayoutParams animalViewParams;
    private WindowManager.LayoutParams closeViewParams;
    private int animalPosX;
    private int animalPosY;
    private float initialTouchPosX;
    private float initialTouchPosY;

    // about controlling window
    private LinearLayout tempLayout;
    private WindowManager windowManager;
    private WindowManager windowManagerClose;
    private int screenWidth;
    private int screenHeight;
    private Animation shakeAnimation;
    private Context context;
    private MoveToStartPointManager moveToStartPointManager;

    // about weather
    private LayoutInflater inflater;
    private ConstraintLayout weatherLayout;
    private WeatherView weatherView;
    private WindowManager.LayoutParams weatherViewParams;

    // about notification
    private NotificationManager notificationManager;
    private Notification notification;
    private NotificationHandler notificationHandler;
    private BackgroundThread backgroundThread;

    // about memo
    private RelativeLayout memoLayout;
    private EditText memoText;
    private Button memoCancelButton;
    private Button memoRegisterButton;
    private LinearLayout notificationMemoListLayout;

    private int startId;


    // 최초 한 번만
    @Override
    public void onCreate() {
        super.onCreate();
        init();
        getScreenSize();
        Log.d("oncreate", "call");
        addWindowView();
        initMemoView();

        Button addMemoButton = createButton(0, -300, Gravity.CENTER, "MEMO");
        addMemoButton.setOnClickListener(view -> showMemoView());


//        Button weatherInitButton = createButton(-320, 500, Gravity.CENTER, "weather");
//        weatherInitButton.setOnClickListener(view -> initWeather());
//
//        Button weatherChangeButton = createButton(320, 500, Gravity.CENTER, "change");
//        weatherChangeButton.setOnClickListener(view -> setWeather(weatherLayout, weatherViewParams,
//                weatherView, windowManager));


        //addMemoButton.setOnClickListener(view -> );

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Notification buildNotification(RemoteViews remoteViews, PendingIntent pendingIntent) {
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("adad")
                .setContentText("fdfsds")
                .setSmallIcon(R.drawable.cat)
                .setCustomBigContentView(new RemoteViews(getPackageName(), R.layout.content_big_status_bar))
                .setCustomContentView(remoteViews)
                .setStyle(new Notification.DecoratedCustomViewStyle())
                .setPriority(Notification.PRIORITY_MIN)
                .setOnlyAlertOnce(true)
                .build();

        notification.contentIntent = pendingIntent;
        return notification;
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(context, "onStartCommand", Toast.LENGTH_SHORT).show();

        // TODO : startForeground 더 연구할 것
        this.startId = startId;

        //Notification notification; //= new Notification(R.drawable.cat, getText(R.string.app_name), System.currentTimeMillis());

        Intent clickIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, clickIntent, 0);

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.content_status_bar);
            notification = buildNotification(remoteViews, pendingIntent);

        } else {
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("adad")
                    .setContentText("fdfsds")
                    .setSmallIcon(R.drawable.cat)
                    .setPriority(Notification.PRIORITY_MIN)
                    .setOnlyAlertOnce(true)
                    .build();
        }

        startForeground(1, notification);

        // foreground 서비스로 한 다음에, notification manager에 notify 하면 수정할 수 있음.

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(startId, notification);


        // 핸들러
//        notificationHandler = new NotificationHandler(context, notificationManager, getApplicationContext());
//        Log.d("handler", "created");
//        backgroundThread = new BackgroundThread(notificationHandler);
//        Log.d("thread", "created");
//        backgroundThread.start();

        /**
         * @ START_STICKY : Service가 강제종료 되었을 경우 시스템이 재시작시켜줌. intent 값은 null로 초기화시켜서 재시작
         * @ START_NOT_STICKY : 강제로 종료된 서비스가 재시작되지 않음. 시스템에 의해 강제종료 되어도 괜찮은 작업 시
         * @ START_REDELIVER_INTENT : 재시작시켜 주고, intent 값도 유지. intent value 사용하였다면 이것을 사용
         * */
        return START_NOT_STICKY;
    }

    // 서비스 -> 액티비티 실행
    private void executeActivity(Class targetClass) {
        Intent intent = new Intent(this.context, targetClass);
        startActivity(intent);
    }

    private void setWeather(View v, WindowManager.LayoutParams params,
                            WeatherView weatherView, WindowManager windowManager) {

        WeatherData weatherData = getRandomWeatherData();
        weatherView.setWeatherData(weatherData);

        windowManager.updateViewLayout(v, params);
    }

    private WeatherData getRandomWeatherData() {
        WeatherData weatherData;
        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0:
                weatherData = PrecipType.CLEAR;
                break;
            case 1:
                weatherData = PrecipType.RAIN;
                break;
            case 2:
                weatherData = PrecipType.SNOW;
                break;
            default:
                return PrecipType.CLEAR;
        }

        return weatherData;


    }

    private void initWeather() {

        weatherLayout = (ConstraintLayout) inflater.inflate(R.layout.content_weather, null);

        weatherViewParams = new WindowManager.LayoutParams(
                (int) ((0.4) * screenWidth),
                (int) (screenHeight),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        weatherViewParams.gravity = Gravity.TOP;

        windowManager.addView(weatherLayout, weatherViewParams);


        weatherView = weatherLayout.findViewById(R.id.weather_view);

        weatherView.setSpeed(1000);
        weatherView.setEmissionRate(50f);
        weatherView.setFadeOutPercent(1f);
        weatherView.setAngle(0);
        weatherView.setWeatherData(PrecipType.RAIN);

    }

    private void initMemoView() {
        memoLayout = (RelativeLayout) inflater.inflate(R.layout.content_memo_display, null);
        memoText = (EditText) memoLayout.findViewById(R.id.memo_text);
        memoCancelButton = memoLayout.findViewById(R.id.cancel);
        memoRegisterButton = memoLayout.findViewById(R.id.confirm);

        memoCancelButton.setOnClickListener(view -> closeMemo(memoLayout));
        memoRegisterButton.setOnClickListener(view -> {
            if(memoText.getText().toString().length() == 0) {
                Toast.makeText(context, "메모를 입력해주세요.", Toast.LENGTH_LONG).show();
            } else {
                // layout update
                registerMemo(memoText.getText().toString());
            }
        });
        String memoString = memoText.getText().toString();
        //memoLayout.setVisibility(View.GONE);
        WindowManager.LayoutParams memoParams = setLayoutParamsByScreenRatio(1f, 1f);
        windowManager.addView(memoLayout, memoParams);
    }

    private void showMemoView() {
        if(memoLayout != null && memoLayout.getVisibility() == View.GONE) {
            memoLayout.setVisibility(View.VISIBLE);
            memoLayout.setClickable(true);
        }
    }

    private void closeMemo(RelativeLayout l) {
        if(l != null) {
            l.setClickable(false);
            l.setVisibility(View.GONE);
        }
    }

    private void registerMemo(String sentence) {
        if(notificationMemoListLayout != null) {
            RelativeLayout rl = new RelativeLayout(context, null, R.style.horizontal_layout_style);
            TextView newMemoText = new TextView(context, null, R.style.text_style);
            ImageButton closeButton = new ImageButton(context, null, R.style.close_button_style);

            rl.addView(newMemoText);
            rl.addView(closeButton);

            notificationMemoListLayout.addView(rl);

            notificationMemoListLayout.invalidate();

            //TODO : 메모 Notification에 동적 추가 어떻게 할 지
//            Intent clickIntent = new Intent(this, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, clickIntent, 0);
//            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.content_status_bar);
//            notification = buildNotification(remoteViews, pendingIntent);

            notificationManager.notify(startId, notification);

            closeMemo(memoLayout);
            Log.e("memo", "added");

        }
    }

    private WindowManager.LayoutParams setLayoutParamsByScreenRatio(float widthRatio, float heightRatio) {

        if(screenWidth == 0 || screenHeight == 0)
            return new WindowManager.LayoutParams();


        return new WindowManager.LayoutParams(
                (int) (widthRatio * screenWidth),
                (int) (heightRatio * screenHeight),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
    }

    private Button createButton(int x, int y, int gravity, String text) {

        Button button = new Button(this);
        button.setText(text);
        button.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

        WindowManager.LayoutParams buttonParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        buttonParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

        buttonParams.gravity = gravity;//Gravity.CENTER;
        buttonParams.x = gravity + x; // 가운데에서 + 하면 오른쪽으로 감, - 하면 왼쪽으로 감
        buttonParams.y = gravity + y;//Gravity.CENTER + 400; // 가운데에서 + 하면 아래로 감, - 하면 위로 감

        Log.d("y coor", "" + buttonParams.y);

        windowManager.addView(button, buttonParams);

        return button;
    }


    // startService 호출될 때마다 실행.
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("onstartcommand", "call");
//        clearWindowView();
//        closeViewParams = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//        closeViewParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
//        closeViewParams.x = 0;
//        closeViewParams.y = 100;
//
//
//        animalViewParams = new WindowManager.LayoutParams(
//                (int) (0.18 * screenWidth),
//                (int) (0.18 * screenWidth),
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
//                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT
//        );
//
//        animalViewParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
//        animalViewParams.x = screenWidth / 2; // horizontal center for the image
//        animalViewParams.y = 0;
//        windowManager.addView(animalView, animalViewParams);
//
//        windowManagerClose.addView(tempLayout, closeViewParams);
//        tempLayout.setVisibility(View.INVISIBLE);
//        closeView.startAnimation(shakeAnimation);
//
//        return START_STICKY; //system will try to recreate the service if in case its killed
//    }

    @SuppressLint("ClickableViewAccessibility")
    private void addWindowView() {
        closeViewParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        closeViewParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        closeViewParams.x = 0;
        closeViewParams.y = 100;

        animalViewParams = new WindowManager.LayoutParams(
                (int) (0.18 * screenWidth),
                (int) (0.18 * screenWidth),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        animalViewParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        animalViewParams.x = screenWidth / 2; // horizontal center for the image
        animalViewParams.y = 0;
        //   animalViewParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(context)) {

                if (!animalView.isShown()) {
                    windowManager.addView(animalView, animalViewParams);
                    windowManagerClose.addView(tempLayout, closeViewParams);
                    tempLayout.setVisibility(View.INVISIBLE);
                    closeView.startAnimation(shakeAnimation);

                }
            }

        } else {
            Toast.makeText(context, "Build SDK VERSION is too low", Toast.LENGTH_LONG).show();
        }

        try {
            // for moving the picture on touch and slide
            animalView.setOnTouchListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearWindowView() {
        if (windowManager != null) {
            windowManager.removeViewImmediate(animalView);
            windowManager.removeViewImmediate(weatherLayout); // 이 라인 에러
            windowManagerClose.removeViewImmediate(tempLayout);
        }
    }

    @Override
    public void onDestroy() {

        backgroundThread.terminate();
        backgroundThread = null;
    }

    private void init() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManagerClose = (WindowManager) getSystemService(WINDOW_SERVICE);
        animalView = new ImageView(this);
        animalView.setImageResource(R.drawable.cat);
        closeView = new ImageView(this);
        closeView.setImageResource(R.drawable.close);
        context = ServiceView.this;
        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wiggle);
        shakeAnimation.setRepeatCount(Animation.INFINITE);
        tempLayout = new LinearLayout(this);
        tempLayout.addView(closeView);
        moveToStartPointManager = new MoveToStartPointManager();

        inflater = LayoutInflater.from(getApplicationContext());//(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationMemoListLayout = inflater.inflate(R.layout.content_big_status_bar, null).findViewById(R.id.notification_big_content_layout);

    }

    private void getScreenSize() {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 첫 클릭시
                animalPosX = animalViewParams.x;
                animalPosY = animalViewParams.y;
                initialTouchPosX = event.getRawX();
                initialTouchPosY = event.getRawY();
                moveToStartPointManager.stop();
                break;
            case MotionEvent.ACTION_MOVE: // 클릭한 상태로 움직일때
                tempLayout.setVisibility(View.VISIBLE);
                animalViewParams.x = animalPosX + (int) (event.getRawX() - initialTouchPosX);
                animalViewParams.y = animalPosY + (int) (event.getRawY() - initialTouchPosY);
                windowManager.updateViewLayout(v, animalViewParams);
                if (MathUtil.betweenExclusive((int) event.getRawX(), 0, screenWidth / 5) || MathUtil.betweenExclusive((int) event.getRawX(), screenWidth - (screenWidth / 5), screenWidth)) {
                    android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                    layoutParams.width = (int) (0.18 * screenWidth);
                    layoutParams.height = (int) (0.18 * screenWidth);
                    animalView.setLayoutParams(layoutParams);
                    windowManager.updateViewLayout(v, animalViewParams);
                } else if (MathUtil.betweenExclusive((int) event.getRawX(), 2 * (screenWidth / 5), 3 * (screenWidth / 5))) {
                    android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                    layoutParams.width = (int) (0.18 * screenWidth) + 100 + 100;
                    layoutParams.height = (int) (0.18 * screenWidth) + 100 + 100;
                    animalView.setLayoutParams(layoutParams);
                    windowManager.updateViewLayout(v, animalViewParams);
                } else if (MathUtil.betweenExclusive((int) event.getRawX(), screenWidth / 5, 2 * (screenWidth / 5)) || MathUtil.betweenExclusive((int) event.getRawX(), 3 * (screenWidth / 5), screenWidth)) {
                    android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                    layoutParams.width = (int) (0.18 * screenWidth) + 100;
                    layoutParams.height = (int) (0.18 * screenWidth) + 100;
                    animalView.setLayoutParams(layoutParams);
                    windowManager.updateViewLayout(v, animalViewParams);
                }

                break;
            case MotionEvent.ACTION_UP: // 뗐을 때
                if (MathUtil.betweenExclusive(animalViewParams.x, -100, 100) && !MathUtil.betweenExclusive(animalViewParams.y, screenHeight / 3, screenHeight / 2)) {

                    moveToStartPointManager.moveTo(screenWidth / 2, animalViewParams.y);
                    android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                    layoutParams.width = (int) (0.18 * screenWidth);
                    layoutParams.height = (int) (0.18 * screenWidth);
                    animalView.setLayoutParams(layoutParams);
                    windowManager.updateViewLayout(v, animalViewParams);
                    tempLayout.setVisibility(View.INVISIBLE);


                } else if (MathUtil.betweenExclusive((int) event.getRawX(), 0, screenWidth / 5)) {

                    if (MathUtil.betweenExclusive((int) event.getRawY(), 0, screenHeight / 10)) {

                        // animalViewParams.y = 0 ;
                        moveToStartPointManager.moveTo(-screenWidth / 2, -((screenHeight / 2) - 150));
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    } else if (MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screenHeight / 10), screenHeight)) {
                        moveToStartPointManager.moveTo(-screenWidth / 2, screenHeight / 2 - 150);
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    } else {
                        moveToStartPointManager.moveTo(-screenWidth / 2, animalViewParams.y);
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    }

                } else if (MathUtil.betweenExclusive((int) event.getRawX(), screenWidth - (screenWidth / 5), screenWidth)) {

                    if (MathUtil.betweenExclusive((int) event.getRawY(), 0, screenHeight / 10)) {

                        // animalViewParams.y = 0 ;
                        moveToStartPointManager.moveTo(screenWidth / 2, -((screenHeight / 2) - 150));
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    } else if (MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screenHeight / 10), screenHeight)) {
                        moveToStartPointManager.moveTo(screenWidth / 2, screenHeight / 2 - 150);
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    } else {
                        moveToStartPointManager.moveTo(screenWidth / 2, animalViewParams.y);
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    }

                } else if (MathUtil.betweenExclusive((int) event.getRawX(), screenWidth / 5, 2 * (screenWidth / 5))) {

                    if (MathUtil.betweenExclusive((int) event.getRawY(), 0, screenHeight / 10)) {

                        moveToStartPointManager.moveTo(-screenWidth / 2, -((screenHeight / 2) - 150));
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    } else if (MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screenHeight / 10), screenHeight)) {
                        moveToStartPointManager.moveTo(-screenWidth / 2, screenHeight / 2 - 150);
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    } else {
                        moveToStartPointManager.moveTo(-screenWidth / 2, animalViewParams.y);
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    }
                } else if (MathUtil.betweenExclusive((int) event.getRawX(), 3 * (screenWidth / 5), screenWidth)) {
                    //moveToStartPointManager to right of screen
                    if (MathUtil.betweenExclusive((int) event.getRawY(), 0, screenHeight / 10)) {

                        // animalViewParams.y = 0 ;
                        moveToStartPointManager.moveTo(screenWidth / 2, -((screenHeight / 2) - 150));
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    } else if (MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screenHeight / 10), screenHeight)) {
                        moveToStartPointManager.moveTo(screenWidth / 2, screenHeight / 2 - 150);
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    } else {
                        moveToStartPointManager.moveTo(screenWidth / 2, animalViewParams.y);
                        android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                        layoutParams.width = (int) (0.18 * screenWidth);
                        layoutParams.height = (int) (0.18 * screenWidth);
                        animalView.setLayoutParams(layoutParams);
                        windowManager.updateViewLayout(v, animalViewParams);
                        tempLayout.setVisibility(View.INVISIBLE);
                    }
                } else if (MathUtil.betweenExclusive(animalViewParams.x, -50, 50) && MathUtil.betweenExclusive(animalViewParams.y, screenHeight / 3, screenHeight / 2)) {

                    // 서비스 종료 ( X 주변으로 가면 )
                    clearWindowView();
                    stopSelf();

                } else {

                    //not in either of the above cases
                    moveToStartPointManager.moveTo(screenWidth / 2, animalViewParams.y);
                    android.view.ViewGroup.LayoutParams layoutParams = animalView.getLayoutParams();
                    layoutParams.width = (int) (0.18 * screenWidth);
                    layoutParams.height = (int) (0.18 * screenWidth);
                    animalView.setLayoutParams(layoutParams);
                    windowManager.updateViewLayout(v, animalViewParams);
                    tempLayout.setVisibility(View.INVISIBLE);

                }
                break;

        }
        return true;
    }

    private static class MathUtil {
        public static boolean betweenExclusive(int x, int min, int max) {
            return x > min && x < max;
        }
    }

    // 돌아가는 위치 움직임 처리 스레드
    private class MoveToStartPointManager implements Runnable {

        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;


        private void moveTo(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
        }

        @Override
        public void run() {
            if (animalView != null && animalView.getParent() != null) {
                float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);

                Log.d("run", "runnable");
                float deltaX = (destinationX - animalViewParams.x) * progress;
                float deltaY = (destinationY - animalViewParams.y) * progress;

                // 선형보간 ( progress 를 통한 값 조절로 )
                interpolate(deltaX, deltaY);
                if (progress < 1) {
                    handler.post(this);
                }
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
        }
    }

    protected void interpolate(float deltaX, float deltaY) {
        animalViewParams.x += deltaX;
        animalViewParams.y += deltaY;
        windowManager.updateViewLayout(animalView, animalViewParams);
    }



}
