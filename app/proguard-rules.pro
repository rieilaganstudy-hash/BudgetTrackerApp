# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
# For more information, see:
#   https://developer.android.com/studio/build/shrink-code.html

# Keep Room and Compose generated classes
-keep class androidx.room.** { *; }
-keep class androidx.compose.** { *; }
