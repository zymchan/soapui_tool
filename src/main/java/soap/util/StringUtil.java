package soap.util;

import org.dom4j.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class StringUtil {

    static ArrayList<String> ids = new ArrayList<>();

    public static String generateId() {
        String result = getId();
        while (ids.contains(result)) {
            result = getId();
        }
        ids.add(result);
        return result;
    }


    private static String getId() {
        return getRandomChar(8) + "-" +
                getRandomChar(4) + "-" +
                getRandomChar(4) + "-" +
                getRandomChar(4) + "-" +
                getRandomChar(12);
    }


    public static String getRandomChar(int length) {
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z'};
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(36)]);
        }
        return buffer.toString();
    }


    public static String getFileContent(String filePath) throws IOException {
        String str = "";
        InputStream is = new FileInputStream(filePath);
        int iAvail = is.available();
        byte[] bytes = new byte[iAvail];
        is.read(bytes);
        str = new String(bytes);
        is.close();
        return str;
    }

    public static void writeXmlToFile(Document document, String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        file.createNewFile();
        FileWriter out = new FileWriter(fileName);
        document.write(out);
        out.close();
    }
}
