package com.xujie.sla;

import java.text.NumberFormat;

/**
 * User: xujie
 * Date: 3/15/12
 * Time: 9:45 PM
 */
public class SLAAnalyzerDaily {
    Report report = new Report();

    public void add(Report serverReport) {
        if (serverReport == null) {
            return;
        }

        if (report.date != null && !report.date.equals(serverReport.date)) {
            return;
        }

        report.date = serverReport.date;
        report.volume += serverReport.volume;
        report.sla3s += serverReport.sla3s;
        report.sla5s += serverReport.sla5s;
    }

    public void computeSLA() {
        System.out.println("date" + ",," + "volume"
                + "," + "3s SLA Met" + "," + "3s SLA Failed" + "," + "% 3s SLA Met"
                + "," + "5s SLA Met" + "," + "5s SLA Failed" + "," + "% 5s SLA Met"
        );

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        if (report.volume > 0) {

            System.out.println(report.date
                    + "," + ""
                    + "," + report.volume
                    + "," + report.sla3s
                    + "," + (report.volume - report.sla3s)
                    + "," + nf.format(report.sla3s * 100.0 / report.volume)
                    + "," + report.sla5s
                    + "," + (report.volume - report.sla5s)
                    + "," + nf.format(report.sla5s * 100.0 / report.volume)
            );
        }
    }
}
