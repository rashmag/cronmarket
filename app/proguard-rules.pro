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

-keep public class ooo.cron.delivery.data.network.models.**

# If the @SerializedName annotation is not used
-keep class ooo.cron.delivery.data.network.models.City {
  *;
}

-keep class ooo.cron.delivery.data.network.models.SuggestAddress {
  *;
}

 -keep class ooo.cron.delivery.data.network.models.Basket {
   *;
}

-keep class ooo.cron.delivery.data.network.models.BasketDish {
  *;
}

-keep class ooo.cron.delivery.data.network.models.BasketDishAdditive {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.BasketItem {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.BasketPersonsReq {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.MarketCategory {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.Pagination {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.Partner {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.PartnerCategoryRes {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.PartnerProductsRes {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.PartnerResult {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.PartnersInfoRes {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.ProductCategoryModel {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.Promotion {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.PromotionsResponse {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.RefreshableToken {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.RemoveBasketItemReq {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.RequireAdditiveModel {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.Schedule {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.SuggestAddress {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.Tag {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.TagsResult {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.TimeSpan {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.User {
  *;
 }

-keep class ooo.cron.delivery.data.network.models.UserResponse {
  *;
 }
