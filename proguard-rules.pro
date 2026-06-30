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

# TensorFlow Lite rules
-keep class org.tensorflow.** { *; }
-keep class org.tensorflow.lite.** { *; }

# Keep native methods for TensorFlow Lite
-keep class * extends org.tensorflow.lite.NativeInterpreterWrapper { *; }

# Keep NanoHTTPD server classes
-keep class fi.iki.elonen.** { *; }
-keep class org.nanohttpd.** { *; }

# Keep all activities and services
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

# Keep R classes
-keep class **.R$* { *; }

# Keep classes that have native methods
-keepclasseswithmembers class * {
    native <methods>;
}

# Keep setters in Views so that animations can still work.
-keepclassmembers class * extends android.view.View {
   void set*(***);
   *** get*();
}

# Keep classes for reflection
-keep class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private <fields>;
    private <methods>;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
}
