package capital.blockpower.tendermonitor.utils;

import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.util.ArrayList;

/**
 * @author tiger
 */
public class FileUtil {

    private FileUtil() {
 
    }

    public static boolean saveFileAs(String content, String path) {
        FileWriter fw = null;
        try {
            File destFile = new File(path);
            if(!(destFile.getParentFile().exists())){
                destFile.getParentFile().mkdirs();
            }
            fw = new FileWriter(destFile, false);
            if (content != null) {
                fw.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    public static File[] listAll(String filePath,String fileSuffix){
        File file = new File(filePath);
        return listAll(file, new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(fileSuffix);
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
    }

    public static File[] listAll(File file,
                                 javax.swing.filechooser.FileFilter filter) {
        ArrayList list = new ArrayList();
        File[] files;
        if (!file.exists() || file.isFile()) {
            return null;
        }
        list(list, file, filter);
        files = new File[list.size()];
        list.toArray(files);
        return files;
    }
    private static void list(ArrayList list, File file,
                             javax.swing.filechooser.FileFilter filter) {
        if (filter.accept(file)) {
            list.add(file);
            if (file.isFile()) {
                return;
            }
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                list(list, files[i], filter);
            }
        }

    }

    public static String readFileAll(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return "";
        }
    }
}