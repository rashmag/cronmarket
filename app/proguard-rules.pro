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

#-keep class java.** { *; }

-keep class ooo.cron.delivery.data.** { *; }

-keep class ru.tinkoff.acquiring.sdk.localization.** { *; }

-keep class ru.tinkoff.acquiring.sdk.requests.** { *; }

-keep class ru.tinkoff.acquiring.sdk.models.** { *; }

#-keep class retrofit2.** { *; }

#-keep class okhttp3.** { *; }

#-keep class ooo.cron.delivery.screens.** { *; }

#-keep class ooo.cron.delivery.utils.** { *; }

#-keep class ooo.cron.delivery.App { *; }

#-keep class ooo.cron.delivery.databinding.** { *; }

#-keep class ooo.cron.delivery.di.** { *; }


