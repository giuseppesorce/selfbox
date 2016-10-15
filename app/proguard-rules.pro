# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/giuseppesorce/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**


# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Picasso
-dontwarn com.squareup.okhttp.**

-dontwarn okio.**
 #### -- OkHttp --
 -dontwarn com.squareup.okhttp.internal.**

 #### -- Apache Commons --
 -dontwarn org.apache.commons.logging.**
# Crashlytics
-keepattributes SourceFile,LineNumberTable,*Annotation*
-keep class com.crashlytics.android.**


# Rxjava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Autovalue
-dontwarn javax.lang.**
-dontwarn javax.tools.**
-dontwarn javax.annotation.**
-dontwarn autovalue.shaded.com.**
-dontwarn com.google.auto.value.**


-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

## Configuration for Guava 18.0
##
## disagrees with instructions provided by Guava project: https://code.google.com/p/guava-libraries/wiki/UsingProGuardWithGuava
#
#-keep class com.google.common.io.Resources {
#    public static <methods>;
#}
#-keep class com.google.common.collect.Lists {
#    public static ** reverse(**);
#}
#-keep class com.google.common.base.Charsets {
#    public static <fields>;
#}
#
#-keep class com.google.common.base.Joiner {
#    public static com.google.common.base.Joiner on(java.lang.String);
#    public ** join(...);
#}
#
#-keep class com.google.common.collect.MapMakerInternalMap$ReferenceEntry
#-keep class com.google.common.cache.LocalCache$ReferenceEntry
#
## http://stackoverflow.com/questions/9120338/proguard-configuration-for-guava-with-obfuscation-and-optimization
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

