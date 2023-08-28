Save dark theme with Data Store use SwitchMaterialButton

Example,

1. Add Depencencies

```
   implementation "androidx.datastore:datastore-preferences:1.0.0"
   implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"
   implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.6.1"
   implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2"
   implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2"

```
2. Create SettingPreferences

```
import androidx.datastore.preferences.core.Preferences
 
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings") // settings is name file DataStore
 
class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {
 
   private val THEME_KEY = booleanPreferencesKey("theme_setting") //KEY

   //mengambil pengaturan tema
   fun getThemeSetting(): Flow<Boolean> {
       return dataStore.data.map { preferences ->
           preferences[THEME_KEY] ?: false
       }
   }

   //menyimpan pengaturan tema
   suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
       dataStore.edit { preferences ->
           preferences[THEME_KEY] = isDarkModeActive
       }
   }

   // membuat singleton dari SettingPreferences
   companion object {
       @Volatile
       private var INSTANCE: SettingPreferences? = null
 
       fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
           return INSTANCE ?: synchronized(this) {
               val instance = SettingPreferences(dataStore)
               INSTANCE = instance
               instance
           }
       }
   }
}

```

3. Create SettingViewModel

```
class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {
   fun getThemeSettings(): LiveData<Boolean> {
       return pref.getThemeSetting().asLiveData()
   }
 
   fun saveThemeSetting(isDarkModeActive: Boolean) {
       viewModelScope.launch {
           pref.saveThemeSetting(isDarkModeActive)
       }
   }
}

```

4. Create ViewModelFactory

```
class ViewModelFactory(private val pref: SettingPreferences) : NewInstanceFactory() {
 
   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
           return MainViewModel(pref) as T
       }
       throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
   }
}
```

6. Create Activity, example name like **SettingActivity**
7. Change **ActivitySetting.xml** add SwitchMaterial

```

    <com.google.android.material.switchmaterial.SwitchMaterial
        android:id="@+id/switch_dark_mode"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/_8sdp"
        android:fontFamily="@font/quicksand_medium"
        android:text="@string/dark_mode"
        android:textColor="@color/text_title"
        android:textSize="@dimen/_14ssp" />

```

8. Change **SettingActivity**

```

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDarkMode.isChecked = false
            }
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }
}

```

Done! but if you want using Dark Mode in All Activity/Fragment you paste code in first Activity. example I paste in Splash Screen Activity

```

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashTime: Long = 3000

        Handler(mainLooper).postDelayed({
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTime)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}

```

Thanks, hopefull useful
