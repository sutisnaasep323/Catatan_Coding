## Toolbar
Toolbar merupakan merupakan pengganti dari action bar yang mempunyai kontrol dan fleksibilitas lebih. kita bisa mudah customisasi dengan mudah sesuai keinginan atau kebutuhan kita. Untuk menggunakan Toolbar kita harus mengatur theme aplikasi menjadi NoActionBar terlebih dahulu, jika tidak, akan terdapat dua app bar dalam satu halaman.

Example

### 1. Create XML

```
<com.google.android.material.appbar.AppBarLayout
        android:id="@+id/appBarLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <androidx.appcompat.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="@color/purple_500">

            <ImageView
                android:layout_width="40dp"
                android:layout_height="35dp"
                android:src="@drawable/logo_name" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="20dp"
                android:gravity="center_vertical"
                android:text="Asep Sutisna"
                android:textColor="@color/white"
                android:textSize="20sp"
                android:textStyle="bold" />

        </androidx.appcompat.widget.Toolbar>
    </com.google.android.material.appbar.AppBarLayout>
```

### 2. Write code in your Activity/Fragment

```
val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
setSupportActionBar(toolbar)
        
supportActionBar?.setDisplayShowTitleEnabled(false)
```

Note: don't forget change style to ```NoActionBar```

### Use Back Button

```
val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
setSupportActionBar(toolbar)
        
supportActionBar?.setDisplayShowTitleEnabled(false)
supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
toolbar.setNavigationOnClickListener{
    onBackPressedDispatcher.onBackPressed()
}
```

