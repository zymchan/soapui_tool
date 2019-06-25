# soapui_tool
a tool for export test case from the project.xml file,  can also generate the test case set.xlsx spreadsheet to mange the cases. and generate a new project.xml that can be executed by soapui.  



<h2>Usage:</h2>

<h5>Export test cases:</h5>
<p>
Open ExportCase.java
</p>
<pre>
    @Test
    public void test() throws Exception {
        String workProject = "C:\\Users\\q1425712\\Desktop\\soap\\projects\\20190624161852\\My Project 0624.xml";
        String destDir = "Q:\\CTAutomation\\JP_API\\TestCase";
        exportCase(workProject, destDir);
    }
</pre>
    
  
<h5>Generate a test case set :</h5>
<p>
Open GenerateTestSet.java
</p>
<pre>
    @Test
    public void test() {
        String casePath = "Q:\\CTAutomation\\JP_API\\TestCase";
        String destDir = "Q:\\CTAutomation\\JP_API\\RunSet";
        try {
            generateTestSet(casePath, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
</pre>


<h5>Generate project.xml with the test case set:</h5>
<p>
Open GenerateSoapUIPorject.java
</p>
<pre>
    @Test
    public void test() throws Exception {
        String projectName = "RunProject";
        String caseSetPath = "Q:\\CTAutomation\\JP_API\\RunSet\\Yi-ming\\TestCaseSet.xlsx";
        String caseStorePath = "Q:\\CTAutomation\\JP_API\\TestCase";
        String destDir = "C:\\Users\\q1425712\\Desktop\\soap\\projects";

        generateSoapUIProject(projectName, caseSetPath, caseStorePath, destDir);
    }
</pre>



<h5>repalce the initial step of the cases:</h5>
<p>
Open ChangeInitialStep.java
</p>
<pre>
    @Test
    public void test() {
        String caseStorePath = "C:\\Users\\q1425712\\Desktop\\soap\\testcases";
        try {
            change(caseStorePath);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
</pre>
