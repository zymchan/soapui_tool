package soap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import soap.util.StringUtil;

import java.io.File;
import java.util.List;

public class ExportCase {


    @Test
    public void test() {
        String workProject = "C:\\Users\\q1425712\\Desktop\\soap\\projects\\20190624161852\\My Project 0624.xml";
        String destDir = "Q:\\CTAutomation\\JP_API\\TestCase";
        exportCase(workProject, destDir);
    }


    public static void exportCase(String workProject, String destDir) {
        try {
            //readFile the xml file of the work project
            SAXReader reader = new SAXReader();

            Document projectDoc = reader.read(new File(workProject));
            Element projectRoot = projectDoc.getRootElement();
            List testSuites = projectRoot.elements("testSuite");
            for (Object suiteObj : testSuites) {
                Element suite = (Element) suiteObj;
                String suiteName = suite.attributeValue("name");
                List testCases = suite.elements("testCase");
                for (Object caseObj : testCases) {
                    Element testCase = (Element) caseObj;
                    String caseName = testCase.attributeValue("name");
                    Attribute disabledAttr = testCase.attribute("disabled");
                    if (disabledAttr == null || disabledAttr.getValue().equals("false")) {
                        try {
                            String path = destDir + "\\" + suiteName + "\\" + caseName + ".xml";

                            Document document = DocumentHelper.createDocument();
                            Object testCaseClone = testCase.clone();
                            document.add((Element) testCaseClone);
                            StringUtil.writeXmlToFile(document, path);
                            System.out.println("Export " + suiteName + "\\" + caseName + ".xml successful!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("Export " + suiteName + "\\ " + caseName + ".xml failed! Error message:" + e.getMessage());
                        }
                    }
                }
            }
            System.out.println("Test case export to " + destDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
