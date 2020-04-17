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
        String str;
        InputStream is = new FileInputStream(filePath);
        return streamToString(is);
    }

    private static String streamToString(InputStream inputStream) throws IOException {
        String str;
        int iAvail = inputStream.available();
        byte[] bytes = new byte[iAvail];
        inputStream.read(bytes);
        str = new String(bytes);
        inputStream.close();
        return str;
    }

    public static String getFileContent(InputStream inputStream) throws IOException {
        String str;
        return streamToString(inputStream);
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

    public static void runCMD(String cmd) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        System.out.println(cmd);
        Process process = runtime.exec("cmd /c start cmd.exe /K \" " + cmd + "&& exit\"");
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line = null;

        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        runCMD("testrunner.bat C:\\Users\\q1425712\\Desktop\\workspace\\soapui_tool\\target\\20190701143852\\project.xml");
    }
}
