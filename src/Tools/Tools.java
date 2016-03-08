package Tools;

import sun.misc.IOUtils;

import java.io.*;

/**
 * Created by Andrei-ch on 2016-02-29.
 */
public class Tools {
    public static void print(Object o) {
        System.out.println(o);
    }

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
        return str;
    }

    public static String smaller_double(double d){
        return String.format("%.2f", (float)d);
    }
}
