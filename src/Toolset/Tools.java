package Toolset;

import Graphs.NetworkGraph.RCGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Andrei-ch on 2016-02-29.
 */
public class Tools {

    /**
     * prints to screen using System.out.println
     * @param o
     */
    public static void print(Object o) {
        System.out.println(o);
    }

    /**
     * returns a string with a truncated value of a double (less sig figs)
     * @param d
     * @return
     */
    public static String smaller_double(double d, int sig_fig){
        return String.format("%." + sig_fig + "f", (float)d);
    }

    /**
     * builds a new RCGraph from a txt file. File must be in ./resources folder
     * @param filename
     * @param a_b
     * @param R_C
     * @return
     */
    public static RCGraph buildGraphFromFile(String filename, int[] a_b, double[] R_C) {
        String constraints = Tools.readConstrains("./resources/" + filename);
        return buildGraphFromString(constraints, a_b, R_C);
    }

    /**
     * builds a new RCGraph from a string of values
     * @param constraints
     * @param a_b
     * @param R_C
     * @return
     */
    private static RCGraph buildGraphFromString(String constraints, int[] a_b, double[] R_C) {
        if (constraints.isEmpty())
            return null;

        // separate input string into substrings
        String[] vals = null;
        try {
            vals = constraints.split("\\s+");
        } catch (PatternSyntaxException ex) {
            ex.printStackTrace();
        }

        // build an array of doubles from input substrings
        List<Double> d_vals = null;
        if (vals != null) {
            d_vals = new ArrayList<Double>();
            for (int i = 0; i < vals.length; i++) {
                if (!vals[i].equals("")) {
                    d_vals.add(Double.parseDouble(vals[i]));
                }
            }
        }

        // set control variables
        int index = d_vals.size() - 1;
        if (a_b != null)
            a_b[0] = (int) (d_vals.get(index-2)).doubleValue();
        if (R_C != null) {
            R_C[0] = d_vals.get(index - 1);
            R_C[1] = d_vals.get(index);
        }

        double[] d_vals_a = new double[d_vals.size()];
        int i = 0;
        for (Double d: d_vals){
            d_vals_a[i] = d;
            i++;
        }

        return GraphTools.buildRCGraphFromDoubleArray(d_vals_a);
    }


    /**
     * reads problem definition from a .txt file
     * @param filename
     * @return
     */
    public static String readConstrains(String filename) {
        BufferedReader br;
        String str = "";
        try {
            br = new BufferedReader(new FileReader(filename));
            try {
                String x;
                while ( (x = br.readLine()) != null ) {
                    // printing out each line in the file
                    str += x;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        str = str.replaceAll("[^.0-9 ]", "\t");
        return str;
    }
}
