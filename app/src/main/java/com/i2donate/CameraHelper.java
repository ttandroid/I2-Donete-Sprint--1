package com.i2donate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



@SuppressLint({"StaticFieldLeak"})
public final class CameraHelper {
   public static final int REQUEST_TAKE_PHOTO = 11112;
   public static Context context;
   
   private static String imageFilePathStr;
   
   public static final CameraHelper INSTANCE;

   
   public final Context getContext() {
      Context var10000 = context;
      if (var10000 == null) {
      }

      return var10000;
   }

   public final void setContext( Context var1) {
      context = var1;
   }

   @Nullable
   public final Uri getOutputMediaFileUri( Context context) {
      CameraHelper.context = context;
      File photoFile = this.pickImageFromGallery();
      Uri photoURI = (Uri)null;
      photoURI = photoFile != null ? FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".fileprovider", photoFile) : Uri.fromFile(new File(imageFilePathStr));
      return photoURI;
   }

   
   public final String getImageFilePathStr() {
      return imageFilePathStr;
   }

   public final void setImageFilePathStr( String var1) {

      imageFilePathStr = var1;
   }

   @Nullable
   public final File pickImageFromGallery() {
      String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())).format(new Date());
      String imageFileName = "IMG_" + timeStamp + "_";
      Context var10000 = context;
      if (var10000 == null) {

      }

      File var7 = var10000.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      if (var7 == null) {
      }

      File storageDir = var7;
      File image = (File)null;

      try {
         image = File.createTempFile(imageFileName, ".jpg", storageDir);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      if (image == null) {

      }

      String var8 = image.getAbsolutePath();
      imageFilePathStr = var8;
      return image;
   }

   @SuppressLint({"StaticFieldLeak"})
   
   public final File getImageFile() {
      Options options = new Options();
      options.inSampleSize = 8;
      String picturePath = imageFilePathStr;
      File curFile = new File(picturePath);

      try {
         ExifInterface exif = new ExifInterface(curFile.getPath());
         int var5 = exif.getAttributeInt("Orientation", 1);
      } catch (IOException var6) {
         Log.e("TAG", "Failed to get Exif data", (Throwable)var6);
      }

      return curFile;
   }

   @Nullable
   public final String getPath( Activity activity, @Nullable Uri uri) {
      if (uri == null) {
         return null;
      } else {
         String[] projection = new String[]{"_data"};
         Cursor cursor = activity.managedQuery(uri, projection, (String)null, (String[])null, (String)null);
         if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return cursor.getString(column_index);
         } else {
            return uri.getPath();
         }
      }
   }

   private CameraHelper() {
   }

   static {
      CameraHelper var0 = new CameraHelper();
      INSTANCE = var0;
      imageFilePathStr = "";
   }
}
