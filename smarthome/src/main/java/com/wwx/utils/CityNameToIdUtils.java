package com.wwx.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CityNameToIdUtils {
    private static final String FILE_NAME = "static/city_data.xlsx";
    private static Map<String, String> cityMap = new HashMap<>();

    static {
        try (InputStream is = CityNameToIdUtils.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                System.err.println("File not found: " + FILE_NAME);

            }
            else System.out.println("File found: " + FILE_NAME);
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    Cell cityIdCell = row.getCell(0);
                    Cell cityNameCell = row.getCell(1);
                    if (cityIdCell != null && cityNameCell != null) {
                        String cityId = getCellValueAsString(cityIdCell);
                        String cityName = getCellValueAsString(cityNameCell);
                        cityMap.put(cityName.trim(), cityId.trim());
                        System.out.println("Loaded city: " + cityName + " with ID: " + cityId); // 添加调试信息
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public static String getCityId(String cityName) {
        String cityId = cityMap.get(cityName.trim());
        if (cityId == null) {
            System.err.println("City not found: " + cityName);
        } else {
            System.out.println("Found city: " + cityName + " with ID: " + cityId);
        }
        return cityId;
    }
}