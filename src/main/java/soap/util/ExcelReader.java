package soap.util;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.util.ArrayList;
import java.util.HashMap;



public class ExcelReader {

    static final Logger logger = Logger.getLogger(ExcelReader.class
            .getName());

    public static HashMap<String, ArrayList<HashMap<String, String>>> loadFile(String filePath) throws Exception {

        XSSFWorkbook curXssfWorkBook;
        HashMap<String, ArrayList<HashMap<String, String>>> dataObject = new HashMap<String, ArrayList<HashMap<String, String>>>();
        OPCPackage pkg;
        try {
            logger.debug("Init: Get Excel file from: " + filePath);
            pkg = OPCPackage.open(filePath, PackageAccess.READ);
            logger.debug("Init: pkg file from: " + filePath);
            curXssfWorkBook = new XSSFWorkbook(pkg);
            logger.debug("Init: work file from: " + filePath);
            pkg.revert();
        } catch (Exception e) {
            logger.error("Exception when opening file : " + filePath, e);
            throw e;
        }


        for (int i = 0; i < curXssfWorkBook.getNumberOfSheets(); i++) {
            XSSFSheet tempSheet = curXssfWorkBook.getSheetAt(i);
            logger.debug("sheet Name : " + curXssfWorkBook.getSheetName(i));
            dataObject.put(curXssfWorkBook.getSheetName(i).toUpperCase(), populateSheet(tempSheet));
        }
        logger.debug("finished loading test file");

        return dataObject;
    }

    private static ArrayList<HashMap<String, String>> populateSheet(
            XSSFSheet workSheet) {
        ArrayList<HashMap<String, String>> curSheet = new ArrayList<HashMap<String, String>>();
        XSSFRow headerRow = workSheet.getRow(0);
        XSSFRow tempRow;
        String key;

        for (int i = 1; i <= workSheet.getLastRowNum(); i++) {
            tempRow = workSheet.getRow(i);
            if (tempRow == null) {
                continue;
            }
            HashMap<String, String> hashMapRow = new HashMap<String, String>();
            for (int j = 0; j < tempRow.getLastCellNum(); j++) {
                key = getCellValue(headerRow.getCell(j));
                if (key != null) {
                    String cellData = getCellValue(tempRow.getCell(j));
                    if (cellData != null) {
                        hashMapRow.put(key.toLowerCase(), cellData);
                    }
                }
            }
            if (hashMapRow.size() != 0){
                curSheet.add(hashMapRow);
            }
        }
        return curSheet;
    }

    private static String getCellValue(XSSFCell cell) {
        String cellValue = null;
        if (cell != null) {
            switch (cell.getCellTypeEnum()) {
                case STRING:
                    cellValue = cell.getStringCellValue().trim();
                    break;
                case NUMERIC:
                    cellValue = String.format("%1$.0f", cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    cellValue = Boolean.toString(cell.getBooleanCellValue());
                    break;
            }
        }
        return cellValue;
    }
}
