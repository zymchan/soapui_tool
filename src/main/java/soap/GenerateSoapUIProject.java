package soap;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import soap.util.ExcelReader;
import soap.util.StringUtil;
import soap.util.TestCaseSet;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class GenerateSoapUIProject {

    String projectPath = GenerateSoapUIProject.class.getClassLoader().getResource("demoProject.xml").getPath();
    String suitePath = GenerateSoapUIProject.class.getClassLoader().getResource("demoSuite.xml").getPath();
    String loadScriptPath = GenerateSoapUIProject.class.getClassLoader().getResource("ProjectLoadScript.groovy").getPath();
    String tearDownScriptPath = GenerateSoapUIProject.class.getClassLoader().getResource("ProjectTearDown.groovy").getPath();

    @Test
    public void test() throws Exception {
        String projectName = "RunProject";
        String caseSetPath = "Q:\\CTAutomation\\JP_API\\RunSet\\Yi-ming\\TestCaseSet.xlsx";
        String caseStorePath = "Q:\\CTAutomation\\JP_API\\TestCase";
        String destDir = "C:\\Users\\q1425712\\Desktop\\soap\\projects";

        generateSoapUIProject(projectName, caseSetPath, caseStorePath, destDir);
    }

    public void generateSoapUIProject(String projectName, String caseSetPath, String caseStorePath, String destDir) throws Exception {
        //make a folder
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(new Date());
        Path parentPath = Paths.get(destDir, date);
        parentPath.toFile().mkdirs();

        //generate project
        Document document = generateProject(projectName);
        Element root = document.getRootElement();

        //read excel
        Map<String, List<String>> suiteMap = getSuiteMap(caseSetPath);

        //add suites to project
        for (Map.Entry<String, List<String>> entry : suiteMap.entrySet()) {
            String suiteName = entry.getKey();
            List<String> cases = entry.getValue();
            root.add(generateSuite(suiteName, cases, caseStorePath));
        }

        //write project to file
        StringUtil.writeXmlToFile(document, parentPath.toString() + "\\" + projectName + ".xml");

        //get config.properties
        generateConfigFile(parentPath.toString());
        System.out.println("Generate project successful in " + parentPath.toString());
    }


    private Element generateSuite(String suiteName, List<String> cases, String caseStorePath) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document suiteDoc = reader.read(new File(suitePath));
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

    private void setIdAndNameToElement(Element element, String name) {
        Attribute caseIdAttr = element.attribute("id");
        Attribute caseNameAttr = element.attribute("name");
        caseIdAttr.setValue(StringUtil.generateId());
        caseNameAttr.setValue(name);
    }

    private Document generateProject(String projectName) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        Document projectDoc = reader.read(new File(projectPath));
        Element projectRoot = projectDoc.getRootElement();

        Document document = DocumentHelper.createDocument();
        Object projectClone = projectRoot.clone();
        document.add((Element) projectClone);
        Element root = document.getRootElement();

        root.addAttribute("name", projectName);
        root.addAttribute("id", StringUtil.generateId());

        Element afterLoadScript = root.element("afterLoadScript");
        afterLoadScript.setText(StringUtil.getFileContent(loadScriptPath));
        Element afterRunScript = root.element("afterRunScript");
        afterRunScript.setText(StringUtil.getFileContent(tearDownScriptPath));
        return document;
    }

    private Map<String, List<String>> getSuiteMap(String caseSetPath) throws Exception {
        Map<String, List<String>> suiteMap = new HashMap<>();
        HashMap<String, ArrayList<HashMap<String, String>>> excel = ExcelReader.loadFile(caseSetPath);
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


    private void generateConfigFile(String destDir) throws IOException {
        String configPath = GenerateSoapUIProject.class.getClassLoader().getResource("config.properties").getPath();

        File source = new File(configPath);
        File dest = new File(destDir + "\\config.properties");
        Files.copy(source.toPath(), dest.toPath());
    }

}
