package com.i2donate.utility;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static String getRealPath(Context context, Uri fileUri) {
        String realPath;
        realPath = FileUtils.getRealPathFromURI_API19(context, fileUri);
        return realPath;
    }

    @SuppressLint("NewApi")
    private static String getRealPathFromURI_API19(final Context context, final Uri uri) {

        if (DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                String path = null;
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else {
                        path = Environment.getExternalStorageDirectory() + "/";
                    }
                } else {
                    path = "storage" + "/" + docId.replace(":", "/");
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    try {
                        MediaStoreUtils mediaStoreUtils = new MediaStoreUtils();
                        InputStream in = context.getContentResolver().openInputStream(uri);
                        Uri uriValue = mediaStoreUtils.savePDFFile(context, in, getMimeTypeFromURI(context, uri), "UtilsClass.getDocFileName()");
                        Log.i("NEWURII = ", uriValue.toString());
                        if (uriValue != null) {
                            return getRealPathFromURI_API19(context, uriValue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    return path;

            } else if (isDownloadsDocument(uri)) {

                String fileName = getFilePath(context, uri);
                if (fileName != null) {
                    return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                }

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);

            } else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                if (contentUri == null)
                    contentUri = uri;

                return getDataColumn(context, contentUri, selection, selectionArgs);

            } else if (isGoogleDocsUri(uri) || isOneDriveUri(uri) || isBoxUri(uri)) {

                return getDriveFilePath(uri, context);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {

            return uri.getPath();
        }

        return null;
    }

    public static String getMimeTypeFromURI(Context mContext, Uri uri) {

        ContentResolver cR = mContext.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return cR.getType(uri);
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static String getFilePath(Context context, Uri uri) {

        Cursor cursor = null;
        final String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static boolean isGoogleDocsUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    private static boolean isOneDriveUri(Uri uri) {
        return "com.microsoft.skydrive.content.StorageAccessProvider".equals(uri.getAuthority());
    }

    private static boolean isBoxUri(Uri uri) {
        return "com.box.android.documents".equals(uri.getAuthority());
    }

    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    private static int computeSampleSize(int width, int height) {

        int inSampleSize = 1;

        if (height > 1000 || width > 1000) {

            while ((height / inSampleSize) >= 1000 || (width / inSampleSize) >= 1000) {

                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static void rotateImageAndReplaceTheSameFile(File localFile, int width, int height) {

        Bitmap bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());

        if (bmp.getHeight() < bmp.getWidth()) {

            try {

                Matrix matrix = new Matrix();

                matrix.postRotate(90);

                bmp = Bitmap.createBitmap(
                        bmp
                        , 0
                        , 0
                        , bmp.getWidth()
                        , bmp.getHeight()
                        , matrix
                        , true);

                bmp = Bitmap.createScaledBitmap(bmp, width, height, false);

            } catch (Exception e) {

                e.printStackTrace();

            }

            FileOutputStream fOut;

            try {

                fOut = new FileOutputStream(localFile);

                bmp.compress(Bitmap.CompressFormat.PNG, 0, fOut);

                fOut.flush();

                fOut.close();

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    public static void enableStrictMode(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public static void pdfViewer(String uri,  Context context){


        if ( uri!= null) {
            Uri path = Uri.fromFile(new File(uri));
            if (path.toString().contains(".pdf")) {
                Intent objIntent = new Intent(Intent.ACTION_VIEW);
                objIntent.setDataAndType(path, "application/pdf");
                objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    context.startActivity(objIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No Application Available to View PDF",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (path.toString().contains(".doc") || path.toString().contains(".docx") || path.toString().contains(".docs")) {
                Intent objIntent = new Intent(Intent.ACTION_VIEW);
                objIntent.setDataAndType(path, "application/doc");
                objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    context.startActivity(objIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No Application Available to View Documents",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (path.toString().contains(".xls") || path.toString().contains(".xlsx") || path.toString().contains(".csv")) {
                Intent objIntent = new Intent(Intent.ACTION_VIEW);
                objIntent.setDataAndType(path, "application/vnd.ms-excel");
                objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    context.startActivity(objIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No Application Available to View Excel",
                            Toast.LENGTH_SHORT).show();
                }

            }

        }


    }


    /*public static boolean isPdfEncrypted(String path) {

        boolean isEncrypted;

        PdfReader pdfReader = null;
        try {

            pdfReader = new PdfReader(path);

            isEncrypted = pdfReader.isEncrypted();

        } catch (IOException e) {

            isEncrypted = true;

        } finally {

            if (pdfReader != null) pdfReader.close();
        }
        return isEncrypted;
    }

    public static boolean isCorrectPdfPassword(String filePath, String inputPassword) {

        try {

            PdfReader reader = new PdfReader(filePath, inputPassword.getBytes());

            reader.close();

            return true;

        } catch (Exception e) {

            return false;
        }
    }*/
}
