package soap;


import soap.util.StringUtil;

import java.io.File;

public class ExecuteSoapUI {


    public static void run(String excelPath, String casePath, String projectName) {
        try {

            File jarFile = new File(ExecuteSoapUI.class.getProtectionDomain().getCodeSource().getLocation().toString());
            //generate project.xml
            String destDir = jarFile.getParentFile().getPath();
            if (destDir.contains("file:")) {
                destDir = destDir.substring(6);
            }

            String projectFilePath = GenerateSoapUIProject.generateSoapUIProject(projectName, excelPath, casePath, destDir);
            String cmd = "testrunner.bat " + projectFilePath;
            //run the project.xml
            StringUtil.runCMD(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
