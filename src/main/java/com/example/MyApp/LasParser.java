package com.example.MyApp;

import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
public class LasParser {

    private boolean wrap;
    private double deptStep;
    private double[] deptData;
    private ArrayList<String> methodsNames = new ArrayList<>();
    private LinkedHashMap<String, double[]> methodsData = new LinkedHashMap<>();
    private HashMap<String, double[]> stitchedMethodsData = new HashMap<>();


    public LinkedHashMap<String, double[]> getMethodsData() {
        return methodsData;
    }

    public ArrayList<String> getMethodsNames() {
        return methodsNames;
    }

    public HashMap<String, double[]> getStitchedMethodsData() {
        return stitchedMethodsData;
    }

    public double getDeptStep() {
        return deptStep;
    }

    public double[] getDeptData() {
        return deptData;
    }

    public void parsing(File file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while (reader.ready()) {
                String s = reader.readLine().trim();
                if (s.startsWith("#")) continue;
                if (s.startsWith("~V")) s = setVersionInformation(reader);
                if (s.startsWith("~W")) s = setWellInformation(reader);
                if (s.startsWith("~C")) s = setCurveInformation(reader);
                if (s.startsWith("~A")) setData(reader);
            }
            setStitchedMethodsData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String setVersionInformation(BufferedReader reader) throws IOException {
        String s = null;
        while (reader.ready()) {
            s = reader.readLine().trim();
            if (s.startsWith("#")) continue;
            if (s.matches("^~.+")) break;
            if (s.toLowerCase().startsWith("wrap")) {
                wrap = s.split(":")[0].toLowerCase().endsWith("yes");
            }
        }
        return s;
    }

    private String setWellInformation(BufferedReader reader) throws IOException {
        String s = null;
        while (reader.ready()) {
            s = reader.readLine().trim();
            if (s.startsWith("#")) continue;
            if (s.matches("^~.+")) break;
            if (s.toLowerCase().startsWith("step")) {
                String[] dp = s.split(":")[0].split("\\s+");
                deptStep = Double.parseDouble(dp[dp.length - 1]);
            }
        }
        return s;
    }


    private String setCurveInformation(BufferedReader reader) throws IOException {
        String s = null;
        while (reader.ready()) {
            s = reader.readLine().trim();
            if (s.startsWith("#")) continue;
            if (s.matches("^~.+")) break;
            if (s.toLowerCase().matches("^dept.*")) continue;
            methodsNames.add(s.split(":")[0].trim());
        }
        return s;
    }

    private void setData(BufferedReader reader) throws IOException {
        ArrayList<String[]> list = new ArrayList<>();

        while (reader.ready()) {
            String s = reader.readLine();
            if (wrap) {
                s += reader.readLine().trim();
            }
            list.add(s.trim().split("\\s+"));
        }

        deptData = new double[list.size()];
        for (int i = 0; i < methodsNames.size(); i++) {
            String s = methodsNames.get(i);
            methodsData.put(s, new double[list.size()]);
            for (int j = 0; j < list.size(); j++) {
                deptData[j] = Double.parseDouble(list.get(j)[0]);
                methodsData.get(s)[j] = Double.parseDouble(list.get(j)[i + 1]);
            }
        }
    }

    private void setStitchedMethodsData() {
        for (String s : methodsData.keySet()) {
            String ss = s.split("\\.")[0].trim();
            for (String s2 : methodsData.keySet()) {
                if (s2.startsWith(ss) && !s2.equals(s)) {
                    double[] d = methodsData.get(s);
                    double[] d2 = methodsData.get(s2);
                    String stitchedMethodName = s + " + " + s2;
                    stitchedMethodsData.put(stitchedMethodName, new double[d.length]);
                    for (int i = 0; i < d.length; i++) {
                        if (d[i] < 0 & d2[i] > 0) {
                            stitchedMethodsData.get(stitchedMethodName)[i] = d2[i];
                        } else stitchedMethodsData.get(stitchedMethodName)[i] = d[i];
                    }
                }
            }
        }
    }

}
