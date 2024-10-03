# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile




# Keep Compose-related classes and methods
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep Jetpack Compose Compiler Plugin generated classes
-keep class * implements androidx.compose.runtime.Composer$Companion { *; }
-keep class * implements androidx.compose.runtime.Composer { *; }

# Keep classes with @Composable annotation
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep classes with @Preview annotation
-keep @androidx.compose.ui.tooling.preview.Preview class * { *; }
-keepclassmembers class * {
    @androidx.compose.ui.tooling.preview.Preview <methods>;
}

# Keep classes with @Stable, @Immutable, and @ReadOnlyComposable annotations
-keep @androidx.compose.runtime.Stable class * { *; }
-keep @androidx.compose.runtime.Immutable class * { *; }
-keep @androidx.compose.runtime.ReadOnlyComposable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Stable <methods>;
    @androidx.compose.runtime.Immutable <methods>;
    @androidx.compose.runtime.ReadOnlyComposable <methods>;
}

# Keep Compose Compiler Plugin generated classes
-keep class * implements androidx.compose.runtime.Composer$Companion { *; }
-keep class * implements androidx.compose.runtime.Composer { *; }
