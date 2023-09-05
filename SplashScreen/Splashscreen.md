Splash Screen adalah tampilan awal aplikasi yang muncul beberapa detik sebelum masuk aplikasi utama

It's so easy to make,

1. Create New Activity, Example here I give the name **SplashScreenActivity**
2. Change code **AcitvitySplashScreen** like this,

```

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background"
    tools:context=".ui.SplashScreenActivity">

    <ImageView
        android:layout_width="350dp"
        android:layout_height="300dp"
        android:background="@drawable/img_splash"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>

```

3. Change **SplashScreenActivity** like this,

```

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashTime: Long = 3000

        Handler(mainLooper).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTime)
}

```

4. And Finally, change intent-filter to SplashScreenActivity

```
        <activity
            android:name=".ui.SplashScreenActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <meta-data
                android:name="android.app.lib_name"
                android:value="" />
        </activity>
```
