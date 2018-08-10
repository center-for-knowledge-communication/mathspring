package edu.umass.ckc.wo.util;
import java.io.*;
/**
 * Created by david on 11/29/2016.
 */
public class VersionFetcher {
        // TODO make this called by server at init time to fetch a string that is the version that can go into the footer
    // of pages.

        public static String getVersion() throws IOException
        {
            String command = "git describe --abbrev=0 --tags";
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            String text = command + "\n";
            System.out.println(text);
            while ((line = input.readLine()) != null)
            {
                text += line;
                System.out.println("Line: " + line);
            }
            return text;
        }

}
