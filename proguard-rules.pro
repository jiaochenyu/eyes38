# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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
#如果有引用v4包可以添加下面这行
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment


#如果引用了v4或者v7包，可以忽略警告，因为用不到android.support
-dontwarn android.support.**


#保持自定义组件不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}


#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
 static final long serialVersionUID;
 private static final java.io.ObjectStreamField[] serialPersistentFields;
 private void writeObject(java.io.ObjectOutputStream);
 private void readObject(java.io.ObjectInputStream);
 java.lang.Object writeReplace();
 java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
 public static **[] values();
public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
 public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
public static <fields>;
}


#xUtils(保持注解，及使用注解的Activity不被混淆，不然会影响Activity中你使用注解相关的代码无法使用)&nbsp;
-keep class * extends java.lang.annotation.Annotation {*;}
-keep class com.otb.designerassist.activity.** {*;}


#自己项目特殊处理代码（这些地方我使用了Gson类库和注解，所以不希望被混淆，以免影响程序）
-keep class com.otb.designerassist.entity.** {*;}
-keep class com.otb.designerassist.http.rspdata.** {*;}
-keep class com.otb.designerassist.service.** {*;}


##混淆保护自己项目的部分代码以及引用的第三方jar包library（想混淆去掉"#"）
#-libraryjars libs/umeng-analytics-v5.2.4.jar
#-libraryjars libs/alipaysecsdk.jar
#-libraryjars libs/alipayutdid.jar
#-libraryjars libs/weibosdkcore.jar&nbsp;


# 以libaray的形式引用的图片加载框架,不想混淆（注意，此处不是jar包形式，想混淆去掉"#"）
#-keep class com.nostra13.universalimageloader.** { *; }


###--------&nbsp;Gson 相关的混淆配置--------
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }







