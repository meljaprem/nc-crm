package com.netcracker.crm.excel;

import com.netcracker.crm.excel.additional.ExcelFormat;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by AN on 25.04.2017.
 */
public interface ReportService {
    Workbook getLastReportWorkbook();

    String getLastReportFileName();

    void createOrdersBetweenDatesOfCustomer_Report(ExcelFormat fileFormat, Long csr_id, Long customer_id, LocalDateTime date_finish_first, LocalDateTime date_finish_last);

    void createOrdersBetweenDatesOfCustomer_ReportChart(Long csr_id, Long customer_id, LocalDateTime date_finish_first, LocalDateTime date_finish_last);

    void createOrdersBetweenDatesOfArrayCustomer_Report(ExcelFormat fileFormat, Long csr_id, List<Long> customer_id, LocalDateTime date_finish_first, LocalDateTime date_finish_last);

    void createOrdersBetweenDatesOfArrayCustomer_ReportChart(Long csr_id, List<Long> customer_id, LocalDateTime date_finish_first, LocalDateTime date_finish_last);

    void createOrdersBetweenDatesAllCustomers_Report(ExcelFormat fileFormat, Long csr_id, LocalDateTime date_finish_first, LocalDateTime date_finish_last);




    void createOrdersOfCurrentWeekOfCustomer_Report(ExcelFormat fileFormat, Long csr_id, Long customer_id);

    void createOrdersOfCurrentWeekOfCustomer_ReportChart(Long csr_id, Long customer_id);

    void createOrdersOfCurrentWeekOfArrayCustomer_Report(ExcelFormat fileFormat, Long csr_id, List<Long> customer_id);

    void createOrdersOfCurrentWeekOfArrayCustomer_ReportChart(Long csr_id, List<Long> customer_id);

    void createOrdersOfCurrentWeekAllCustomers_Report(ExcelFormat fileFormat, Long csr_id);




    void createOrdersOfCurrentMonthOfCustomer_Report(ExcelFormat fileFormat, Long csr_id, Long customer_id);

    void createOrdersOfCurrentMonthOfCustomer_ReportChart(Long csr_id, Long customer_id);

    void createOrdersOfCurrentMonthOfArrayCustomer_Report(ExcelFormat fileFormat, Long csr_id, List<Long> customer_id);

    void createOrdersOfCurrentMonthOfArrayCustomer_ReportChart(Long csr_id, List<Long> customer_id);

    void createOrdersOfCurrentMonthAllCustomers_Report(ExcelFormat fileFormat, Long csr_id);



    void createOrdersOfCurrentYearOfCustomer_Report(ExcelFormat fileFormat, Long csr_id, Long customer_id);

    void createOrdersOfCurrentYearOfCustomer_ReportChart(Long csr_id, Long customer_id);

    void createOrdersOfCurrentYearOfArrayCustomer_Report(ExcelFormat fileFormat, Long csr_id, List<Long> customer_id);

    void createOrdersOfCurrentYearOfArrayCustomer_ReportChart(Long csr_id, List<Long> customer_id);

    void createOrdersOfCurrentYearAllCustomers_Report(ExcelFormat fileFormat, Long csr_id);


}
