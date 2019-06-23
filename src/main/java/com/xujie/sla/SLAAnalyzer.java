package com.xujie.sla;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: xujie
 * Date: 3/8/12
 * Time: 1:48 PM
 * 
 * main class
 */
public class SLAAnalyzer {
    
    public static final String LEXUS = "lexus";
    public static final String PORSCHE = "porsche";
    public static final String GDHXAS07 = "gdhxas07";
    public static final String LAWLEY = "lawley";
    public static final String SORAK = "sorak";
    public static final String FUJI = "fuji";

    public static SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
    public static DecimalFormat decimalFormat = new DecimalFormat("00");


    public static void main(String[] args) throws Exception {

//        if (args.length  == 0) {
//            System.out.println("Usage:");
//            System.out.println("com.xujie.sla.SLAAnalyzer root=? type=monthly period=2012-03");
//            return;
//        }

//        System.out.println("root folder: " + args[0]);


        String month = "2013-07";
        String root;
//        if (args[0] == null)  {
            root = "d:\\MOMA\\Tools\\performance\\";
//        } else {
//            root = args[0];
//        }
        String lexus = root + LEXUS + "\\" + month;
        String porsche = root + PORSCHE + "\\" + month;
        String gdhxas07 = root + GDHXAS07 + "\\" + month;

        String fuji = root + FUJI + "\\" + month;
        String sorak = root + SORAK + "\\" + month;
        String lawley = root + LAWLEY + "\\" + month;

        String[] servers = new String[]{lexus, porsche, gdhxas07};
//        String[] servers = new String[]{fuji,sorak, lawley};

        SLAAnalyzer analyzer = new SLAAnalyzer();
        analyzer.computeSLAHourly(servers, month + "-" + "12", 24);
        analyzer.computeSLADaily(servers, month + "-12");

//        analyzer.computeSLAMonthly(servers, month);
//        com.xujie.sla.ActionMap.getInstance().listActionCount();

    }

    public void computeSLAMonthly(String[] servers, String month) throws Exception {
        
        SLAAnalyzerMonthly analyzer = new SLAAnalyzerMonthly();
        Date dt = monthFormat.parse(month);
        System.out.println(dt);

        Calendar dayOfMonth = Calendar.getInstance();
        dayOfMonth.setTime(dt);
        int lastDay = dayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH) ;

        for (int i = 1; i <= lastDay; i++) {
            dayOfMonth.set(Calendar.DAY_OF_MONTH, i);

            Report report = computeSLADaily(servers, dateFormat.format(dayOfMonth.getTime()));

            analyzer.add(report);
        }

        analyzer.report.month = month;
        analyzer.computeSLA();
    }
    
    public Report computeSLADaily(String[] servers, String date) throws Exception {
        DailyReportProcessor processor = new DailyReportProcessor();
        SLAAnalyzerDaily analyzer = new SLAAnalyzerDaily();
        Date dt = dateFormat.parse(date);
        for (String server : servers) {
            analyzer.add(processor.process(new File(server), dt));
        }

        analyzer.computeSLA();
        return analyzer.report;
    }
    
    public Report[] computeSLAHourly(String[] servers, String date, int hour) throws Exception {
        HourlyReportProcessor processor = new HourlyReportProcessor();
        SLAAnalyzerHourly analyzer = new SLAAnalyzerHourly();
        Date dt = dateFormat.parse(date);
        for (String server: servers) {
            analyzer.add(processor.process(new File(server), dt, hour));
        }

        analyzer.computeSLA();
        return analyzer.getReports();
    }
}
