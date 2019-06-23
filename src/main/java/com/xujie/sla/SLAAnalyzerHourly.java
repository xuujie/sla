package com.xujie.sla;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * User: xujie
 * Date: 3/15/12
 * Time: 1:42 PM
 *
 * To compute hourly performance report for one day
 */
public class SLAAnalyzerHourly {

    private Report[] reports = new Report[24];

    public Report[] getReports() {
        return reports;
    }

    public void add(Report[]... serverReports) {
       for (Report[] report : serverReports ) {
           add(report);
       }
    }

    public void add(Report[] serverReports) {
        if (serverReports == null) {
            return;
        }
        for (int i = 0; i < reports.length; i ++) {
            if (reports[i] == null) {
                reports[i] = new Report();
            }
            if (serverReports[i] == null) {
                continue;
            }
            reports[i].date = serverReports[i].date;
            reports[i].hour = serverReports[i].hour;
            reports[i].volume += serverReports[i].volume;
            reports[i].sla3s += serverReports[i].sla3s;
            reports[i].sla5s += serverReports[i].sla5s;
        }
    }

    public void computeSLA() throws ParseException {
        System.out.println("date" + "," + "hour" + "," + "volume"
                + "," + "3s SLA Met" + "," + "3s SLA Failed" + "," + "% 3s SLA Met"
                + "," + "5s SLA Met" + "," + "5s SLA Failed" + "," + "% 5s SLA Met"
        );

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        for (int i = 0; i < reports.length; i++) {
            Report report = reports[i];
            if (report.volume > 0) {

                System.out.println(report.date
                        + "," + report.hour
                        + "," + report.volume
                        + "," + report.sla3s
                        + "," + (report.volume - report.sla3s)
                        + "," + nf.format(report.sla3s * 100.0 / report.volume)
                        + "," + report.sla5s
                        + "," + (report.volume - report.sla5s)
                        + "," + nf.format(report.sla5s * 100.0 / report.volume)
                );

                int hour = SLAAnalyzer.decimalFormat.parse(report.hour).intValue();
                if (reports[hour] == null) {
                    reports[hour] = new Report();
                    reports[hour].hour = report.hour;
                }
                reports[hour].volume += report.volume;
                reports[hour].sla3s += report.sla3s;
                reports[hour].sla5s += report.sla5s;
            }
        }
    }
}
