package com.xujie.sla;

import java.text.NumberFormat;

/**
 * User: xujie
 * Date: 3/17/12
 * Time: 8:01 PM
 */
public class SLAAnalyzerMonthly {

    Report report = new Report();

    public void add(Report dailyReport) {
        if (dailyReport == null) {
            return;
        }

        report.volume += dailyReport.volume;
        report.sla3s += dailyReport.sla3s;
        report.sla5s += dailyReport.sla5s;
    }

    public void computeSLA() {
        System.out.println("month" + ",," + "volume"
                + "," + "3s SLA Met" + "," + "3s SLA Failed" + "," + "% 3s SLA Met"
                + "," + "5s SLA Met" + "," + "5s SLA Failed" + "," + "% 5s SLA Met"
        );

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        if (report.volume > 0) {

            System.out.println(report.month
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
