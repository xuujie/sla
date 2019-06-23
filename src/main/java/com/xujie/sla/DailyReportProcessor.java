package com.xujie.sla;

import java.io.File;
import java.util.Date;

public class DailyReportProcessor {

    public Report process(File folder, Date date) throws Exception {
        Report dailyReport = new Report();
        Report[] reports = new HourlyReportProcessor().process(folder, date, 24);

        if (reports == null) {
            return null;
        }

        for (Report report : reports) {
            if (report == null) {
                continue;
            }
            dailyReport.date = report.date;
            dailyReport.volume += report.volume;
            dailyReport.sla3s += report.sla3s;
            dailyReport.sla5s += report.sla5s;
        }

        return dailyReport;
    }
}
