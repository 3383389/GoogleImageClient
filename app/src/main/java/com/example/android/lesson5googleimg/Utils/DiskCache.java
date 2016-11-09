package com.example.android.lesson5googleimg.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import com.jakewharton.disklrucache.DiskLruCache;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DiskCache {

    private Bitmap.CompressFormat mCompressFormat;
    private int mCompressQuality;
    private DiskLruCache mDiskLruCache;
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50; // 50MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";


    public DiskCache(Context context) {
        try {
            final File diskCacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
            mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
            mCompressFormat = Bitmap.CompressFormat.JPEG;
            mCompressQuality = 100;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
            throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0), Utils.IO_BUFFER_SIZE);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void put(String key, Bitmap data) {

        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(key);
            if (editor == null) {
                return;
            }

            if (writeBitmapToFile(data, editor)) {
                mDiskLruCache.flush();
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException ignored) {
            }
        }
        Log.v("frag", "put img in cache");

    }


    public Bitmap getBitmapFromDiskCache(String key) {
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        Log.v("frag", "key = " + key);
        try {

            snapshot = mDiskLruCache.get(key);
            if (snapshot == null) {
                return null;
            }
            final InputStream in = snapshot.getInputStream(0);
            if (in != null) {
                final BufferedInputStream buffIn =
                        new BufferedInputStream(in);
                bitmap = BitmapFactory.decodeStream(buffIn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return bitmap;
    }

    // Creates a unique subdirectory of the designated app cache directory. Tries to use external
// but if not mounted, falls back on internal storage.
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !Utils.isExternalStorageRemovable() ?
                        Utils.getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }


}
