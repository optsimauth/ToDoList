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

# 保留 TasksBackup 数据类
-keep class pers.optsimauth.todolist.backup.BackupManagerKt { *; }

# 保留 Task 及其内部类
-keep class pers.optsimauth.todolist.entity.Task { *; }
-keep class pers.optsimauth.todolist.entity.Task$* { *; }

# 保留 NoteEntity 类及其成员
-keep class pers.optsimauth.todolist.entity.NoteEntity { *; }

# GSON 相关规则
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# 保留构造函数
-keepclassmembers class * {
    public <init>(...);
}

# 保留字段名
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}




# 保留所有类��字段名
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# 保留特定包下的类及其字段名
-keep class pers.optsimauth.todolist.entity.** { *; }
-keepclassmembers class pers.optsimauth.todolist.entity.** {
    <fields>;
}


-keep class pers.optsimauth.todolist.backup.** { *; }
-keepclassmembers class pers.optsimauth.todolist.backup.** {
    <fields>;
}
