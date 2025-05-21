package org.example.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.view.JasperViewer;

import org.example.dao.DepartmentDao;
import org.example.dao.EmployeeDao;
import org.example.model.Department;
import org.example.model.Employee;

import javax.swing.*;
import java.awt.Color;
import java.util.*;

public class EmployeesByDepartmentReport {

    public static void generateReport() {
        try {
            // Создаем объект JasperDesign
            JasperDesign jasperDesign = new JasperDesign();
            jasperDesign.setName("EmployeesByDepartmentReport");
            jasperDesign.setPageWidth(595);
            jasperDesign.setPageHeight(842);
            jasperDesign.setColumnWidth(555);
            jasperDesign.setColumnSpacing(0);
            jasperDesign.setLeftMargin(20);
            jasperDesign.setRightMargin(20);
            jasperDesign.setTopMargin(20);
            jasperDesign.setBottomMargin(20);

            // Определяем поля
            JRDesignField departmentIdField = new JRDesignField();
            departmentIdField.setName("departmentId");
            departmentIdField.setValueClass(Long.class);
            jasperDesign.addField(departmentIdField);

            JRDesignField departmentNameField = new JRDesignField();
            departmentNameField.setName("departmentName");
            departmentNameField.setValueClass(String.class);
            jasperDesign.addField(departmentNameField);

            JRDesignField employeeIdField = new JRDesignField();
            employeeIdField.setName("employeeId");
            employeeIdField.setValueClass(Integer.class);
            jasperDesign.addField(employeeIdField);

            JRDesignField employeeNameField = new JRDesignField();
            employeeNameField.setName("employeeName");
            employeeNameField.setValueClass(String.class);
            jasperDesign.addField(employeeNameField);

            JRDesignField phoneField = new JRDesignField();
            phoneField.setName("phoneNumber");
            phoneField.setValueClass(Long.class);
            jasperDesign.addField(phoneField);

            JRDesignField educationField = new JRDesignField();
            educationField.setName("education");
            educationField.setValueClass(String.class);
            jasperDesign.addField(educationField);

            // Создаем бэнд заголовок
            JRDesignBand titleBand = new JRDesignBand();
            titleBand.setHeight(50);

            JRDesignStaticText titleText = new JRDesignStaticText();
            titleText.setX(0);
            titleText.setY(10);
            titleText.setWidth(555);
            titleText.setHeight(30);
            titleText.setText("Список сотрудников по отделам");
            titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            titleText.setFontSize(16f);
            titleText.setBold(true);
            titleBand.addElement(titleText);
            jasperDesign.setTitle(titleBand);

            // Создаем бэнд заголовков столбцов
            JRDesignBand columnHeaderBand = new JRDesignBand();
            columnHeaderBand.setHeight(20);

            // Заголовок ID сотрудника
            JRDesignStaticText idHeader = new JRDesignStaticText();
            idHeader.setX(0);
            idHeader.setY(0);
            idHeader.setWidth(50);
            idHeader.setHeight(20);
            idHeader.setText("ID");
            idHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            idHeader.setBackcolor(new Color(220, 220, 220));
            idHeader.setMode(ModeEnum.OPAQUE);
            idHeader.setBold(true);
            columnHeaderBand.addElement(idHeader);

            JRDesignStaticText nameHeader = new JRDesignStaticText();
            nameHeader.setX(50);
            nameHeader.setY(0);
            nameHeader.setWidth(200);
            nameHeader.setHeight(20);
            nameHeader.setText("ФИО");
            nameHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            nameHeader.setBackcolor(new Color(220, 220, 220));
            nameHeader.setMode(ModeEnum.OPAQUE);
            nameHeader.setBold(true);
            columnHeaderBand.addElement(nameHeader);

            JRDesignStaticText phoneHeader = new JRDesignStaticText();
            phoneHeader.setX(250);
            phoneHeader.setY(0);
            phoneHeader.setWidth(150);
            phoneHeader.setHeight(20);
            phoneHeader.setText("Телефон");
            phoneHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            phoneHeader.setBackcolor(new Color(220, 220, 220));
            phoneHeader.setMode(ModeEnum.OPAQUE);
            phoneHeader.setBold(true);
            columnHeaderBand.addElement(phoneHeader);

            JRDesignStaticText educationHeader = new JRDesignStaticText();
            educationHeader.setX(400);
            educationHeader.setY(0);
            educationHeader.setWidth(155);
            educationHeader.setHeight(20);
            educationHeader.setText("Образование");
            educationHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            educationHeader.setBackcolor(new Color(220, 220, 220));
            educationHeader.setMode(ModeEnum.OPAQUE);
            educationHeader.setBold(true);
            columnHeaderBand.addElement(educationHeader);

            // Создаем группу для отделов
            JRDesignGroup departmentGroup = new JRDesignGroup();
            departmentGroup.setName("DepartmentGroup");

            JRDesignExpression groupExpression = new JRDesignExpression();
            groupExpression.setText("$F{departmentId}");
            departmentGroup.setExpression(groupExpression);

            // Заголовок группы отделов
            JRDesignBand groupHeaderBand = new JRDesignBand();
            groupHeaderBand.setHeight(30);

            JRDesignTextField departmentNameField2 = new JRDesignTextField();
            departmentNameField2.setX(0);
            departmentNameField2.setY(5);
            departmentNameField2.setWidth(555);
            departmentNameField2.setHeight(25);
            departmentNameField2.setFontSize(14f);
            departmentNameField2.setBold(true);

            JRDesignExpression departmentNameExpression = new JRDesignExpression();
            departmentNameExpression.setText("\"Отдел: \" + $F{departmentName}");
            departmentNameField2.setExpression(departmentNameExpression);

            groupHeaderBand.addElement(departmentNameField2);
            ((JRDesignSection)departmentGroup.getGroupHeaderSection()).addBand(groupHeaderBand);

            // Подвал группы отделов
            JRDesignBand groupFooterBand = new JRDesignBand();
            groupFooterBand.setHeight(20);

            JRDesignStaticText footerLine = new JRDesignStaticText();
            footerLine.setX(0);
            footerLine.setY(5);
            footerLine.setWidth(555);
            footerLine.setHeight(1);
            footerLine.setMode(ModeEnum.OPAQUE);
            footerLine.setBackcolor(Color.BLACK);
            groupFooterBand.addElement(footerLine);

            ((JRDesignSection)departmentGroup.getGroupFooterSection()).addBand(groupFooterBand);

            jasperDesign.addGroup(departmentGroup);
            jasperDesign.setColumnHeader(columnHeaderBand);

            // Создаем детальный бэнд
            JRDesignBand detailBand = new JRDesignBand();
            detailBand.setHeight(20);

            // Поле ID сотрудника
            JRDesignTextField idField = new JRDesignTextField();
            idField.setX(0);
            idField.setY(0);
            idField.setWidth(50);
            idField.setHeight(20);
            idField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);

            JRDesignExpression idExpression = new JRDesignExpression();
            idExpression.setText("$F{employeeId}");
            idField.setExpression(idExpression);
            detailBand.addElement(idField);

            JRDesignTextField nameField = new JRDesignTextField();
            nameField.setX(50);
            nameField.setY(0);
            nameField.setWidth(200);
            nameField.setHeight(20);

            JRDesignExpression nameExpression = new JRDesignExpression();
            nameExpression.setText("$F{employeeName}");
            nameField.setExpression(nameExpression);
            detailBand.addElement(nameField);

            JRDesignTextField phoneNumberField = new JRDesignTextField();
            phoneNumberField.setX(250);
            phoneNumberField.setY(0);
            phoneNumberField.setWidth(150);
            phoneNumberField.setHeight(20);

            JRDesignExpression phoneExpression = new JRDesignExpression();
            phoneExpression.setText("$F{phoneNumber}.toString()");
            phoneNumberField.setExpression(phoneExpression);
            detailBand.addElement(phoneNumberField);

            JRDesignTextField educationTextField = new JRDesignTextField();
            educationTextField.setX(400);
            educationTextField.setY(0);
            educationTextField.setWidth(155);
            educationTextField.setHeight(20);

            JRDesignExpression educationExpression = new JRDesignExpression();
            educationExpression.setText("$F{education}");
            educationTextField.setExpression(educationExpression);
            detailBand.addElement(educationTextField);

            // Добавляем детальный бэнд в дизайн
            ((JRDesignSection)jasperDesign.getDetailSection()).addBand(detailBand);

            // Компилируем отчет
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // Подготавливаем данные
            List<Map<String, Object>> dataList = prepareReportData();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

            // Заполняем отчет
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

            // Показываем отчет
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Ошибка при создании отчета: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static List<Map<String, Object>> prepareReportData() throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<>();

        DepartmentDao departmentDao = new DepartmentDao();
        EmployeeDao employeeDao = new EmployeeDao();

        List<Department> departments = departmentDao.getAllDepartments();

        for (Department department : departments) {
            List<Employee> employees = employeeDao.getEmployeesByDepartmentId(department.getId());

            for (Employee employee : employees) {
                Map<String, Object> data = new HashMap<>();
                data.put("departmentId", department.getId());
                data.put("departmentName", department.getName());
                data.put("employeeId", employee.getId());
                data.put("employeeName", employee.getFull_name());
                data.put("phoneNumber", employee.getPhone_number());
                data.put("education", employee.getEducation());

                dataList.add(data);
            }
        }

        return dataList;
    }
}




