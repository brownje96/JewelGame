package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class Utility {

    public static String readEntireFileAsStr(InputStream f) {
        StringBuilder x = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(f))) {
            String s;
            while((s = br.readLine())!=null) x.append(s);
        } catch (IOException ex) { ex.printStackTrace(System.err); }
        return x.toString();
    }

}