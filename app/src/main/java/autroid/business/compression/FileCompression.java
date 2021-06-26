package autroid.business.compression;

import android.net.Uri;

//import com.qiscus.sdk.chat.core.QiscusCore;
//import com.qiscus.sdk.chat.core.util.QiscusFileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import id.zelory.compressor.Compressor;

//import static com.qiscus.sdk.chat.core.util.QiscusFileUtil.getFileName;

public class FileCompression {


    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static File Compress(File file){

        File compressedFile = file;

//        if (QiscusFileUtil.isImage(file.getPath()) && !file.getName().endsWith(".gif")) {
//            try {
//                compressedFile = new Compressor(QiscusCore.getApps()).compressToFile(file);
//            } catch (NullPointerException | IOException e) {
//
//            }
//        } else {
//            compressedFile = QiscusFileUtil.saveFile(compressedFile);
//        }

        if (!file.exists()) { //File have been removed, so we can not upload it anymore
            //return "File removed, can't upload!";
        }

        return compressedFile;
    }

    //create a temp file by copying the content of original file into temp file

    public static File from(Uri uri) throws IOException {
//        InputStream inputStream = QiscusCore.getApps().getContentResolver().openInputStream(uri);
//        String fileName = getFileName(uri);
//        String[] splitName = splitFileName(fileName);
//        File tempFile = File.createTempFile(splitName[0], splitName[1]);
//        tempFile = rename(tempFile, fileName);
//        tempFile.deleteOnExit();
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream(tempFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (inputStream != null) {
//            copy(inputStream, out);
//            inputStream.close();
//        }
//
//        if (out != null) {
//            out.close();
//        }
        return null;
    }

    public static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    public static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists()) {
                newFile.delete();
            }
            file.renameTo(newFile);
        }
        return newFile;
    }

    private static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    private static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    private static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
