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

#If the @SerializedName annotation is used
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep,allowobfuscation interface com.google.gson.annotations.SerializedName

# If the @SerializedName annotation is not used
-keepclassmembers class ooo.cron.delivery.data.network.models.City {
  !transient <fields>;
 }

 -keepclassmembers class ooo.cron.delivery.data.network.models.SuggestAddress {
  !transient <fields>;
 }

 -keepclassmembers class ooo.cron.delivery.data.network.models.Basket {
   !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.BasketDish {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.BasketDishAdditive {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.BasketItem {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.BasketPersonsReq {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.MarketCategory {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.Pagination {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.Partner {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.PartnerCategoryRes {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.PartnerProductsRes {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.PartnerResult {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.PartnersInfoRes {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.ProductCategoryModel {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.Promotion {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.PromotionsResponse {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.RefreshableToken {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.RemoveBasketItemReq {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.RequireAdditiveModel {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.Schedule {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.SuggestAddress {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.Tag {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.TagsResult {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.TimeSpan {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.User {
  !transient <fields>;
 }

-keepclassmembers class ooo.cron.delivery.data.network.models.UserResponse {
  !transient <fields>;
 }
