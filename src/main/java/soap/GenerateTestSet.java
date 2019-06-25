package soap;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Test;
import soap.util.TestCaseSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class GenerateTestSet {


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


    private void generateTestSet(String casePath, String destDir) throws IOException {

        List<Map<String, String>> maps = new ArrayList<>();
        for (File suiteFile : new File(casePath).listFiles()) {
            if (suiteFile.isDirectory()) {
                String suiteName = suiteFile.getName();
                for (File caseFile : suiteFile.listFiles()) {
                    if (caseFile.isFile() && caseFile.getName().endsWith(".xml")) {
                        String caseName = caseFile.getName().replace(".xml", "");
                        Map<String, String> map = new HashMap<>();
                        map.put(TestCaseSet.TEST_CASE, caseName);
                        map.put(TestCaseSet.TEST_SUITE, suiteName);
                        map.put(TestCaseSet.DISABLE, "");
                        maps.add(map);
                    }
                }
            }
        }
        writeDataToExcel(maps, destDir);
    }


    private void writeDataToExcel(List<Map<String, String>> maps, String destDir) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(new Date());
        Path parentPath = Paths.get(destDir, date);
        Path filePath = Paths.get(destDir, date, TestCaseSet.FILE_NAME);
        parentPath.toFile().mkdirs();

        XSSFWorkbook workbook = new XSSFWorkbook();
        try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
            XSSFSheet sheet = workbook.createSheet(TestCaseSet.TEST);
            XSSFRow titleRow = sheet.createRow(0);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            insertCell(titleRow, 0, TestCaseSet.TEST_CASE, style);
            insertCell(titleRow, 1, TestCaseSet.TEST_SUITE, style);
            insertCell(titleRow, 2, TestCaseSet.DISABLE, style);

            for (int i = 0; i < maps.size(); i++) {
                Map<String, String> map = maps.get(i);
                String caseName = map.get(TestCaseSet.TEST_CASE);
                String suiteName = map.get(TestCaseSet.TEST_SUITE);
                String disable = map.get(TestCaseSet.DISABLE);
                XSSFRow row = sheet.createRow(i + 1);
                insertCell(row, 0, caseName, null);
                insertCell(row, 1, suiteName, null);
                insertCell(row, 2, disable, null);
            }
            sheet.setColumnWidth(0, 30000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 3000);
            workbook.write(fileOut);
            System.out.println("Generate test case set successful in " + parentPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertCell(XSSFRow row, int columnIndex, String value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

}
