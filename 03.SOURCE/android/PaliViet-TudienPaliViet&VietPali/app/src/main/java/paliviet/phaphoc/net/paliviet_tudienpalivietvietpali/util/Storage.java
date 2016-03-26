package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.util;

/**
 * Created by huytr on 25-03-2016.
 */

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by huytr on 07-01-2016.
 */
public class Storage {

    public static final String DEBUG = "Storage";

    private static Storage storage = null;
    private Context context;

    private Storage(Context context) {
        this.context = context.getApplicationContext();
    }

    public static Storage help(Context context) {
        if (storage == null) {
            storage = new Storage(context);
        }
        return storage;
    }

    public boolean copyAsset(String source ,
                             String target) throws Exception {
        InputStream inputStream = context.getAssets().open(source);
        if ((new File(target)).getParentFile().mkdir()) {
            return false;
        }
        OutputStream outputStream = new FileOutputStream(target);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer , 0 , length);
        }
        return true;
    }

    public boolean copyFile(String source ,
                            String target) throws Exception {
        FileChannel inputChannel = new FileInputStream(source).getChannel();
        FileChannel outputChannel = new FileOutputStream(target).getChannel();
        boolean result = true;
        try {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        } catch (Exception exception) {
            Log.d(DEBUG, exception.getMessage());
            result = false;
        }
        finally {
            if (inputChannel != null)
                inputChannel.close();
            if (outputChannel != null)
                outputChannel.close();
        }
        return result;
    }

    public boolean deleteFile(String fileName) {
        File directory = context.getFilesDir();
        File file = new File(directory , fileName);
        return file.delete();
    }

    public boolean moveFile(String source ,
                            String target) throws Exception{
        return copyFile(source , target) && deleteFile(source);
    }

    public String getInternalPath(String fileName) {
        return context.getFilesDir() + "/" + fileName;
    }

    public String getDatabasePath(String fileName) {
        return context.getDatabasePath(fileName).getAbsolutePath();
    }

    public boolean exists(String path) {
        return (new File(path)).exists();
    }

    public String[] getAssetFiles(String root) throws Exception{
        return context.getAssets().list(root);
    }
}
