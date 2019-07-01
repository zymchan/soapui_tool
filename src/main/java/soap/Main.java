package soap;


public class Main {


    static String helpInfo = "The format of your request is incorrect, please use the below format\n" +
            "\t-export \"{work project}\" \"{dest dir}\" \n" +
            "\t To export the enabled cases into the destination directory\n" +
            "\t\t\texample: -export \"C:\\My Project 0624.xml\" \"Q:\\CTAutomation\\JP_API\\TestCase\"\n" +
            "\t-excel \"{case path}\" \"{dest dir}\" \n" +
            "\t To generate a TestCaseSet.xlsx file in the destination directory\n" +
            "\t\t\texample: -excel \"Q:\\CTAutomation\\JP_API\\TestCase\" \"Q:\\CTAutomation\\JP_API\\RunSet\"\n" +
            "\t-execute \"{path of TestCaseSet.xlsx}\" \"{case path}\" \n" +
            "\t To execute the cases with the testCaseSet.xlsx\n" +
            "\t\t\texample: -export \"C:\\TestCaseSet.xlsx\" \"Q:\\CTAutomation\\JP_API\\TestCase\" \"projectName\" \n";

    public static void main(String[] args) {
        try {
            switch (args[0].toLowerCase()) {
                case "-export":
                    ExportCase.exportCase(args[1], args[2]);
                    break;
                case "-execute":
                    String projectName;
                    try {
                        projectName = args[3];
                    } catch (Exception e) {
                        projectName = "project";
                    }
                    ExecuteSoapUI.run(args[1], args[2], projectName);
                    break;
                case "-excel":
                    GenerateTestSet.generateTestSet(args[1], args[2]);
                    break;
                default:
                    System.out.println(helpInfo);
            }
        } catch (Exception e) {
            System.out.println(helpInfo);
        }
    }
}
