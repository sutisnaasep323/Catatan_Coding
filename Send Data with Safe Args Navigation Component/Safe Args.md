
Menggunakan Safe Args dengan Navigation Component adalah cara aman untuk mengirimkan data agar tidak terjadi null yang membuat Error Null Pointer Exception (NPE), ini merupakan kelebihan daripada menggunkan Bundle

Steps:

1. Add arguments in Fragment/Class

2. Fill in the data as needed, for example, we will send 'name' with String data type and 'stock' with integer data type. here we can use the default value

3. Add library SafeArgs in the :

**build.gradle(Project)**

```
buildscript {
    dependencies {
        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.5.1"
    }
}

plugins {
    ...
}

```

**Build.gradle(Module: app)**

```
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'androidx.navigation.safeargs'
}
 
android {
    ...
}

```

4. Manipulate the Button to send Data

```
      val toDetailCategoryBinding =
                CategoryFragmentDirections.actionCategoryFragmentToDetailCategoryFragment().apply {
                    name = "Life Style"
                    stock = 77
                }
      view.findNavController().navigate(toDetailCategoryBinding)
```

5. Recive data in Destination Fragment

```

    val dataName = DetailCategoryFragmentArgs.fromBundle(arguments as Bundle).name
    val dataDescription = DetailCategoryFragmentArgs.fromBundle(arguments as Bundle).stock
 
    binding.tvCategoryName.text = dataName
    binding.tvCategoryDescription.text = "Stock : $dataDescription"

```

## How about Parcelable?

**Fisrt**, don't forget add plugin

```
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id "androidx.navigation.safeargs"
    id 'kotlin-parcelize'
}

```
**Second**, create Data Class

```
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val nama: String,
    val age: Int,
    val years: Int
):Parcelable

```
- Add Argument in main_navigation.xml

```
    <argument
            android:name="item"
            app:argType="com.asep.mynavigation.Profile" />
            
```

- Manipulate Button like this

```
      binding.YourButton.setOnClickListener{
            val data = Profile("Asep", 21, 2001)
            val action = HomeFragmentDirections.actionHomeFragmentToDetailProfileFragment(data)
            view.findNavController().navigate(action)
        }

```

- Recive data in Destination Fragment

```

        val data = DetailProfileFragmentArgs.fromBundle(arguments as Bundle).item
        binding.tvDetailName.text = data.nama
        binding.tvDetailAge.text = data.age.toString()
        binding.tvDetailYears.text = data.years.toString()

```

Thank you, I hope this little bit can be useful

