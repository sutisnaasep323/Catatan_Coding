
### 1. Create XML

```
<androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar4"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="@color/primary"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <ImageButton
                android:id="@+id/backButton"
                android:layout_width="32dp"
                android:layout_height="32dp"
                android:src="@drawable/ic_back"
                android:clickable="true"
                android:contentDescription="@string/ic_back"
                android:focusable="true"
                android:background="?attr/selectableItemBackgroundBorderless" />

            <TextView
                android:id="@+id/tittle_toolbar"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:layout_marginStart="8dp"
                android:fontFamily="@font/open_sans_bold"
                android:gravity="center_vertical"
                android:text="@string/setting"
                android:textColor="@color/white"
                android:textSize="18sp"
                android:textStyle="bold" />

        </LinearLayout>

    </androidx.appcompat.widget.Toolbar>

```

### 2. Code Program
```
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
```
