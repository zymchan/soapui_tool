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


public class ExcelUtil {

    private static final Logger logger = Logger.getLogger(ExcelUtil.class.getName());

    public static HashMap<String, ArrayList<HashMap<String, String>>> readFile(String filePath) throws Exception {
        XSSFWorkbook curXssfWorkBook;
        HashMap<String, ArrayList<HashMap<String, String>>> dataObject = new HashMap<>();

        logger.info("Init: Excel file from: " + filePath);
        OPCPackage pkg = OPCPackage.open(filePath, PackageAccess.READ);
        curXssfWorkBook = new XSSFWorkbook(pkg);
        pkg.revert();

        for (int i = 0; i < curXssfWorkBook.getNumberOfSheets(); i++) {
            logger.debug("Read sheet : " + curXssfWorkBook.getSheetName(i));
            XSSFSheet tempSheet = curXssfWorkBook.getSheetAt(i);
            dataObject.put(curXssfWorkBook.getSheetName(i).toUpperCase(), readSheet(tempSheet));
        }
        logger.debug("finished loading test file");

        return dataObject;
    }

    private static ArrayList<HashMap<String, String>> readSheet(XSSFSheet workSheet) {
        ArrayList<HashMap<String, String>> curSheet = new ArrayList<>();
        XSSFRow headerRow = workSheet.getRow(0);
        XSSFRow tempRow;
        String key;

        for (int i = 1; i <= workSheet.getLastRowNum(); i++) {
            tempRow = workSheet.getRow(i);
            if (tempRow == null) {
                continue;
            }
            HashMap<String, String> hashMapRow = new HashMap<>();
            for (int j = 0; j < tempRow.getLastCellNum(); j++) {
                key = getCellValue(headerRow.getCell(j));
                if (key != null) {
                    String cellData = getCellValue(tempRow.getCell(j));
                    if (cellData != null) {
                        hashMapRow.put(key.toLowerCase(), cellData);
                    }
                }
            }
            if (hashMapRow.size() != 0) {
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
