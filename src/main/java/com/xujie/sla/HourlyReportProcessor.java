package com.xujie.sla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.zip.GZIPInputStream;

public class HourlyReportProcessor {

    /**
     *
     * @param folder folder to store epol-performance.date.log
     * @param date date to check performance
     * @param hour hour to check performance
     * @return report array, each report is for each hour
     * @throws Exception exception
     */
    public Report[] process(File folder, Date date, int hour) throws Exception {

        ActionMap am = ActionMap.getInstance();

        Report[] reports = new Report[24];

        String temp = folder.toString() + "/epol-performance.log." + SLAAnalyzer.dateFormat.format(date);

        File f = new File(temp + ".gz");

        if (f.length() == 0) {
            return null;
        }
//        System.out.println(f);
        boolean gzip = f.exists();

        if (!gzip) {
            return null;
//            f = new File(temp);
        }

//        System.out.println(temp + (gzip?" exists" : " not exists"));

        FileInputStream fis = new FileInputStream(f);

        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new GZIPInputStream(fis)));

            while (true) {
                String line = br.readLine();

                if (line == null) {
                    break;
                }

//                for (int i = 0; i < com.xujie.sla.ExceptionText.EXCEPTION_TEXT.length; i++) {
//                    if (line.indexOf(com.xujie.sla.ExceptionText.EXCEPTION_TEXT[i]) > 0) {
//                        continue;
//                    }
//                }
//                if (line.indexOf("resultOutputLst") > 0
//                        || line.indexOf("PHASES") > 0
//                        || line.indexOf("bg003NextAction.do") > 0
//                        || line.indexOf("EP_WARNING") > 0
//                        || line.indexOf()) {
//                    continue;
//                }

                if (line.indexOf(",") <= 0) {
                    continue;
                }

                String[] csv;
                try {
                    csv = line.split(",");
                    if (csv.length > 1) {
                        am.addAction(csv[1]);
                    }
                } catch (Exception e) {
                    continue;
                }


                Date recordTime;
                int recordHour;
                long speed;
                try {
                    recordTime = SLAAnalyzer.datetimeFormat.parse(csv[0]);
                    recordHour = SLAAnalyzer.decimalFormat.parse(SLAAnalyzer.hourFormat.format(recordTime)).intValue();
                    speed = Integer.parseInt(csv[2]);
                } catch (Exception e) {
                    continue;
                }

                if (hour != 24 && recordHour != hour) {
                    continue;
                }

                if (SLAAnalyzer.dateFormat.format(date).equals(SLAAnalyzer.dateFormat.format(recordTime))) {
                    if (reports[recordHour] == null) {
                        reports[recordHour] = new Report();
                        reports[recordHour].date = SLAAnalyzer.dateFormat.format(recordTime);
                        reports[recordHour].hour = SLAAnalyzer.decimalFormat.format(recordHour);
                    }

//                    if (line.indexOf("PEPOLAPPL009IncludeAction") >= 0
//                            || line.indexOf("PEPOLAPPL011DisplayAction") >= 0
//                            || line.indexOf("PEPOLCORR001DisplayAction") >= 0
//                            || line.indexOf("PEPOLCORR002DisplayAction") >= 0
//                            || line.indexOf("PEPOLCORRAppeManagerAction") >= 0
//                            || line.indexOf("PEPOLCORRDisplayAction") >= 0
//                            || line.indexOf("PEPOLCORRManagerAction") >= 0
//                            || line.indexOf("PEPOLREQC002DisplayAction") >= 0
//                            || line.indexOf("PEPOLESVPInfoBoxAction") >= 0
//                            || line.indexOf("FEPOLACKN001DisplayAction") >= 0
//                            || line.indexOf("FEPOLREQP001DisplayAction") >= 0) {
//                        System.out.println(line);
//                    }
                    if (line.indexOf("PEPOLESVP005ProcessAction") >= 0) {
                        System.out.println(line);
                    }

//                    if (line.indexOf("PEPOLCORR002DisplayAction") >= 0) {
//                        System.out.println(line);
//                    }


                    if (speed <= 3000) {
                        ++reports[recordHour].sla3s;
                        ++reports[recordHour].sla5s;
                    } else if (speed <= 5000) {
                        ++reports[recordHour].sla5s;
                    }

                    if (speed > 60000) {
                        //print actions more than 1mins
//                        System.out.println(line);
                    }

                    ++reports[recordHour].volume;
                }
            }
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return reports;
    }
}
