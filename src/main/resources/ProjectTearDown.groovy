import com.aventstack.extentreports.ExtentReports
import com.aventstack.extentreports.reporter.ExtentHtmlReporter


//Create folder
String projectName = runner.project.name
def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)

String projectReportPath = groovyUtils.projectPath + "\\report\\" + projectName

File reportDir = new File(projectReportPath)
if (!reportDir.exists()) {
    reportDir.mkdirs()
}

Date date = new Date()
String now = date.format("yyyy-MM-dd-HH-mm-ss")
String reportFileName = projectName + "_AutomationTestReport_" + now
String reportFilePath = projectReportPath + "\\" + reportFileName + ".html"


ExtentHtmlReporter html = new ExtentHtmlReporter(reportFilePath)
ExtentReports extent = new ExtentReports()
extent.attachReporter(html)


runner.results.each { testSuiteResult ->
    testSuiteResult.getResults().each {
        testCaseResult ->
            String name = testCaseResult.getTestCase().name
            String tag = testCaseResult.getTestCase().testSuite.name

            def testEx = extent.createTest(name)
            testEx.assignCategory(tag)

            int intLoop = 0
            String pre = ""
            for (testStepResult in testCaseResult.getResults()) {
                // Get step
                def testStep = testStepResult.getTestStep()
                long timeStamp = testStepResult.getTimeStamp()
                def stepTime = new Date(timeStamp)
                String stepName = testStep.name
                String type = testStep.config.type
                boolean failedResult = testStepResult.status.toString() == 'FAILED'

                if (stepName == "LoopStart") {
                    intLoop++
                    pre = "Loop " + intLoop + ":  "
                }
                if (stepName == "LoopEnd") {
                    pre = ""
                }
                if (!failedResult && (stepName == "LoopStart" || stepName == "LoopEnd" || stepName == "Initial")) {
                    continue
                }

                stepName = "<b>" + stepName + "</b>"
                //create node
                def node = testEx.createNode(pre + stepName)
                node.getModel().setStartTime(stepTime)

                String prefix = "<div style='max-height:200px;overflow-y:scroll'><pre>"
                String suffix = "</pre></div>"
                switch (type) {
                    case "httprequest":
                        byte[] rawRequest = testStepResult.getRawRequestData()
                        byte[] rawResponse = testStepResult.getRawResponseData()
                        String request = new String(rawRequest)
                        String response = new String(rawResponse).replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
                        String detail = "Request Raw: " + prefix + request + suffix + "Response Raw" + prefix + response + suffix
                        node.info(detail)

                        testStep.getAssertions().each {
                            entry ->
                                String assertName = entry.key

                                String errorMsg = ""
                                for (msg in testStepResult.getMessages()) {
                                    if (msg.startsWith("[" + assertName + "]")) {
                                        errorMsg = msg
                                    }
                                }
                                if (errorMsg != "") {
                                    def failMsg = prefix + errorMsg + suffix
                                    node.fail("Assertion -- " + assertName + failMsg)
                                } else {
                                    node.pass("Assertion -- " + assertName)
                                }
                        }
                        break
                    case "groovy":
                        String message = ""
                        testStepResult.messages.each() { msg -> message = message + msg + "<br>" }
                        if (failedResult) {
                            node.fail(stepName + prefix + message + suffix)
                        } else {
                            node.pass(stepName)
                        }
                        break
                    case "jdbc"://the same as httprequest
                        byte[] rawRequest = testStepResult.getRawRequestData()
                        String request = new String(rawRequest)
                        String response = testStepResult.getResponseContentAsXml()
                        response = response.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
                        String detail = "Request Raw: " + prefix + request + suffix + "Response Raw" + prefix + response + suffix
                        node.info(detail)

                        testStep.getAssertions().each {
                            entry ->
                                String assertName = entry.key

                                String errorMsg = ""
                                for (msg in testStepResult.getMessages()) {
                                    if (msg.startsWith("[" + assertName + "]")) {
                                        errorMsg = msg
                                    }
                                }
                                if (errorMsg != "") {
                                    def failMsg = prefix + errorMsg + suffix
                                    node.fail("Assertion -- " + assertName + failMsg)
                                } else {
                                    node.pass("Assertion -- " + assertName)
                                }
                        }
                        break
                    default:
                        String message = ""
                        testStepResult.messages.each() { msg -> message = message + msg + "<br>" }
                        if (failedResult) {
                            node.fail(stepName + prefix + message + suffix)
                        } else {
                            node.pass(stepName)
                        }
                }

            }
    }
}

extent.flush()
log.info "Report path: " + reportFilePath