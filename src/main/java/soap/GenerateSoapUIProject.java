package soap;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import soap.util.ExcelUtil;
import soap.util.StringUtil;
import soap.util.TestCaseSet;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class GenerateSoapUIProject {

    static InputStream projectStream = GenerateSoapUIProject.class.getClassLoader().getResourceAsStream("demoProject.xml");
    static InputStream suiteStream = GenerateSoapUIProject.class.getClassLoader().getResourceAsStream("demoSuite.xml");
    static InputStream loadScriptStream = GenerateSoapUIProject.class.getClassLoader().getResourceAsStream("ProjectLoadScript.groovy");
    static InputStream tearDownScriptStream = GenerateSoapUIProject.class.getClassLoader().getResourceAsStream("ProjectTearDown.groovy");
    static InputStream configStream = GenerateSoapUIProject.class.getClassLoader().getResourceAsStream("config.properties");


    @Test
    public void test() throws Exception {
        String projectName = "RunProject";
        String caseSetPath = "Q:\\CTAutomation\\JP_API\\RunSet\\Yi-ming\\TestCaseSet.xlsx";
        String caseStorePath = "Q:\\CTAutomation\\JP_API\\TestCase";
        String destDir = "C:\\Users\\q1425712\\Desktop\\soap\\projects";

        generateSoapUIProject(projectName, caseSetPath, caseStorePath, destDir);
    }

    public static String generateSoapUIProject(String projectName, String caseSetPath, String caseStorePath, String destDir) throws Exception {
        //make a folder
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(new Date());
        Path parentPath = Paths.get(destDir, date);
        parentPath.toFile().mkdirs();

        //generate project
        Document document = generateProject(projectName);
        Element root = document.getRootElement();

        //readFile excel
        Map<String, List<String>> suiteMap = getSuiteMap(caseSetPath);

        //add suites to project
        for (Map.Entry<String, List<String>> entry : suiteMap.entrySet()) {
            String suiteName = entry.getKey();
            List<String> cases = entry.getValue();
            root.add(generateSuite(suiteName, cases, caseStorePath));
        }

        //write project to file
        String projectFilePath = parentPath.toString() + "\\" + projectName + ".xml";
        StringUtil.writeXmlToFile(document, projectFilePath);

        //get config.properties
//        generateConfigFile(parentPath.toString());
        System.out.println("Generate project successful in " + parentPath.toString());
        return projectFilePath;
    }


    private static Element generateSuite(String suiteName, List<String> cases, String caseStorePath) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document suiteDoc = reader.read(suiteStream);
        Element suiteRoot = suiteDoc.getRootElement();
        Element suiteClone = (Element) suiteRoot.clone();

        setIdAndNameToElement(suiteClone, suiteName);

        for (String testCase : cases) {
            String casePath = caseStorePath + "\\" + suiteName + "\\" + testCase + ".xml";
            Document caseDoc = reader.read(new File(casePath));
            Element caseClone = (Element) caseDoc.getRootElement().clone();

            setIdAndNameToElement(caseClone, testCase);
            suiteClone.add(caseClone);
        }
        return suiteClone;
    }

    private static void setIdAndNameToElement(Element element, String name) {
        Attribute caseIdAttr = element.attribute("id");
        Attribute caseNameAttr = element.attribute("name");
        caseIdAttr.setValue(StringUtil.generateId());
        caseNameAttr.setValue(name);
    }

    private static Document generateProject(String projectName) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        Document projectDoc = reader.read(projectStream);
        Element projectRoot = projectDoc.getRootElement();

        Document document = DocumentHelper.createDocument();
        Object projectClone = projectRoot.clone();
        document.add((Element) projectClone);
        Element root = document.getRootElement();

        root.addAttribute("name", projectName);
        root.addAttribute("id", StringUtil.generateId());

        Element afterLoadScript = root.element("afterLoadScript");
        afterLoadScript.setText(StringUtil.getFileContent(loadScriptStream));
        Element afterRunScript = root.element("afterRunScript");
        afterRunScript.setText(StringUtil.getFileContent(tearDownScriptStream));
        return document;
    }

    private static Map<String, List<String>> getSuiteMap(String caseSetPath) throws Exception {
        Map<String, List<String>> suiteMap = new HashMap<>();
        HashMap<String, ArrayList<HashMap<String, String>>> excel = ExcelUtil.readFile(caseSetPath);
        ArrayList<HashMap<String, String>> caseList = excel.get(TestCaseSet.TEST);
        for (HashMap<String, String> caseMap : caseList) {
            String suiteName = caseMap.get(TestCaseSet.TEST_SUITE.toLowerCase());
            String caseName = caseMap.get(TestCaseSet.TEST_CASE.toLowerCase());
            boolean enable = caseMap.get(TestCaseSet.DISABLE.toLowerCase()) == null || !caseMap.get(TestCaseSet.DISABLE.toLowerCase()).toLowerCase().equals("y");
            if (!enable) {
                continue;
            }
            if (suiteMap.containsKey(suiteName)) {
                suiteMap.get(suiteName).add(caseName);
            } else {
                List<String> cases = new ArrayList<>();
                cases.add(caseName);
                suiteMap.put(suiteName, cases);
            }
        }
        return suiteMap;
    }


    private static void generateConfigFile(String destDir) throws IOException {
        File targetFile = new File(destDir + "\\config.properties");
        OutputStream outStream = new FileOutputStream(targetFile);

        byte[] buffer = new byte[configStream.available()];
        configStream.read(buffer);
        outStream.write(buffer);
    }

}
