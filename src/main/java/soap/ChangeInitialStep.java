package soap;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import soap.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ChangeInitialStep {

    String initialScriptPath = GenerateSoapUIProject.class.getClassLoader().getResource("Initial.groovy").getPath();

    @Test
    public void test() {
        String caseStorePath = "C:\\Users\\q1425712\\Desktop\\soap\\testcases";
        try {
            change(caseStorePath);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }


    public void change(String caseStorePath) throws IOException, DocumentException {
        //readFile the xml file of the work project
        for (File suiteFile : new File(caseStorePath).listFiles()) {
            if (suiteFile.isDirectory()) {
                String suiteName = suiteFile.getName();
                for (File caseFile : suiteFile.listFiles()) {
                    if (caseFile.isFile() && caseFile.getName().endsWith(".xml")) {
                        String casePath = caseFile.getName();
                        changeInitialStep(Paths.get(caseStorePath,suiteName,casePath).toString());
                    }
                }
            }
        }

    }


    public void changeInitialStep(String casePath) throws DocumentException, IOException {
        //readFile the xml file of the work project
        SAXReader reader = new SAXReader();
        Document caseDoc = reader.read(new File(casePath));
        Element caseRoot = caseDoc.getRootElement();
        List testSteps = caseRoot.elements("testStep");
        for (Object stepObj : testSteps) {
            Element testStep = (Element) stepObj;
            String stepName = testStep.attributeValue("name");
            if (stepName.equals("Initial")) {
                Element scriptElement = testStep.element("config").element("script");
                scriptElement.setText(StringUtil.getFileContent(initialScriptPath));
                break;
            }
        }

        Document document = DocumentHelper.createDocument();
        Object testCaseClone = caseRoot.clone();
        document.add((Element) testCaseClone);
        StringUtil.writeXmlToFile(document, casePath);
        System.out.println("Test case export to " + casePath);

    }
}
