package com.healthrx.webhooksqlsolver.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SqlSolutionService {
    
    /**
     * Solves Question 1:
     * Find the highest salary that was credited to an employee, but only for transactions that were not
     * made on the 1st day of any month. Returns employee details along with the salary.
     */
    public String solveQuestion1() {
        return "WITH EmployeeAges AS (\n" +
               "    SELECT \n" +
               "        EMP_ID, \n" +
               "        FIRST_NAME, \n" +
               "        LAST_NAME, \n" +
               "        DATEDIFF(YEAR, DOB, CURRENT_DATE) - \n" +
               "            CASE \n" +
               "                WHEN MONTH(CURRENT_DATE) < MONTH(DOB) OR \n" +
               "                     (MONTH(CURRENT_DATE) = MONTH(DOB) AND DAY(CURRENT_DATE) < DAY(DOB)) \n" +
               "                THEN 1 \n" +
               "                ELSE 0 \n" +
               "            END AS AGE,\n" +
               "        d.DEPARTMENT_NAME\n" +
               "    FROM EMPLOYEE e\n" +
               "    JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID\n" +
               "),\n" +
               "NonFirstDayPayments AS (\n" +
               "    SELECT \n" +
               "        p.AMOUNT AS SALARY,\n" +
               "        CONCAT(ea.FIRST_NAME, ' ', ea.LAST_NAME) AS NAME,\n" +
               "        ea.AGE,\n" +
               "        ea.DEPARTMENT_NAME\n" +
               "    FROM PAYMENTS p\n" +
               "    JOIN EmployeeAges ea ON p.EMP_ID = ea.EMP_ID\n" +
               "    WHERE DAY(p.PAYMENT_TIME) != 1\n" +
               "    ORDER BY p.AMOUNT DESC\n" +
               "    LIMIT 1\n" +
               ")\n" +
               "SELECT * FROM NonFirstDayPayments;";
    }

    /**
     * Solves Question 2:
     * Calculate the number of employees who are younger than each employee, grouped by their respective departments.
     */
    public String solveQuestion2() {
        return "WITH EmployeeAges AS (\n" +
               "    SELECT \n" +
               "        e.EMP_ID,\n" +
               "        e.FIRST_NAME,\n" +
               "        e.LAST_NAME,\n" +
               "        d.DEPARTMENT_NAME,\n" +
               "        e.DOB,\n" +
               "        e.DEPARTMENT,\n" +
               "        DATEDIFF(YEAR, e.DOB, CURRENT_DATE) - \n" +
               "            CASE \n" +
               "                WHEN MONTH(CURRENT_DATE) < MONTH(e.DOB) OR \n" +
               "                     (MONTH(CURRENT_DATE) = MONTH(e.DOB) AND DAY(CURRENT_DATE) < DAY(e.DOB)) \n" +
               "                THEN 1 \n" +
               "                ELSE 0 \n" +
               "            END AS AGE\n" +
               "    FROM EMPLOYEE e\n" +
               "    JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID\n" +
               "),\n" +
               "EmployeeAgeRanks AS (\n" +
               "    SELECT \n" +
               "        e1.EMP_ID,\n" +
               "        e1.FIRST_NAME,\n" +
               "        e1.LAST_NAME,\n" +
               "        e1.DEPARTMENT_NAME,\n" +
               "        e1.AGE,\n" +
               "        COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT\n" +
               "    FROM EmployeeAges e1\n" +
               "    LEFT JOIN EmployeeAges e2 ON e1.DEPARTMENT = e2.DEPARTMENT \n" +
               "        AND e2.AGE < e1.AGE\n" +
               "    GROUP BY \n" +
               "        e1.EMP_ID, \n" +
               "        e1.FIRST_NAME, \n" +
               "        e1.LAST_NAME, \n" +
               "        e1.DEPARTMENT_NAME, \n" +
               "        e1.AGE\n" +
               ")\n" +
               "SELECT \n" +
               "    EMP_ID, \n" +
               "    FIRST_NAME, \n" +
               "    LAST_NAME, \n" +
               "    DEPARTMENT_NAME, \n" +
               "    YOUNGER_EMPLOYEES_COUNT\n" +
               "FROM EmployeeAgeRanks\n" +
               "ORDER BY EMP_ID DESC;";
    }
}
