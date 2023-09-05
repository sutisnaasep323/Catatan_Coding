### Bismillah, pada kesempatan ini saya akan membagikan template code yang nantinya untuk kebutuhan proyek kedepannya. disini jika sobat ingin membuat Drawer Layout dan juga ingin menambahkan Bottom Navigation pada fragment maka ikuti langkah berikut ini:

Note: Sebelum itu baiknya sobat sesuaikan nama class, variable, dst.

## 1. Buat Fragment dan ubah seperti ini
---

```
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.asep.ruhkehidupan.R
import com.asep.ruhkehidupan.databinding.FragmentDoaHafalanBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class DoaHafalanFragment : Fragment() {
    private lateinit var binding: FragmentDoaHafalanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoaHafalanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_doa_hafalan) as NavHostFragment
        var navController = navHostFragment.navController

        navView.setupWithNavController(navController)
    }
}
```

## 2. Ubah XML Fragment tersebut menjadi seperti ini
---

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/nav_view"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="0dp"
        android:layout_marginEnd="0dp"
        android:background="?android:attr/windowBackground"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:menu="@menu/bottom_nav_menu" />

    <fragment
        android:id="@+id/nav_host_fragment_activity_doa_hafalan"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toTopOf="@id/nav_view"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/bottom_navigation" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## 3. buat Bottom_Navigation seperti ini
---

```
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/mobile_navigation2"
    app:startDestination="@id/navigation_home">

    <fragment
        android:id="@+id/navigation_home"
        android:name="com.asep.ruhkehidupan.ui.drawer.ui.home.HomeFragment"
        android:label="@string/title_home"
        tools:layout="@layout/fragment_home" />

    <fragment
        android:id="@+id/navigation_dashboard"
        android:name="com.asep.ruhkehidupan.ui.drawer.ui.dashboard.DashboardFragment"
        android:label="@string/title_dashboard"
        tools:layout="@layout/fragment_dashboard" />

    <fragment
        android:id="@+id/navigation_notifications"
        android:name="com.asep.ruhkehidupan.ui.drawer.ui.notifications.NotificationsFragment"
        android:label="@string/title_notifications"
        tools:layout="@layout/fragment_notifications" />
</navigation>
```

## 4. Buat bottom_nav_menu seperti ini
---

```
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/navigation_home"
        android:icon="@drawable/ic_home_black_24dp"
        android:title="@string/title_home" />

    <item
        android:id="@+id/navigation_dashboard"
        android:icon="@drawable/ic_dashboard_black_24dp"
        android:title="@string/title_dashboard" />

    <item
        android:id="@+id/navigation_notifications"
        android:icon="@drawable/ic_notifications_black_24dp"
        android:title="@string/title_notifications" />

</menu>
```

## Note: Jika ingin manipulasi bottom, Pastikan ID pada bottom_navigation dan bottom_nav_menu itu sama
