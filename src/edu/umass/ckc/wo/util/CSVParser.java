package edu.umass.ckc.wo.util;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Jun 24, 2004
 * Time: 2:38:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSVParser {



    public static String[] parse (String s) throws IOException {
        StreamTokenizer t = new StreamTokenizer(new StringReader(s));
        t.resetSyntax();
        t.wordChars('0','9');
        t.wordChars('a','z');
        t.wordChars('-','-');
        t.wordChars('_','_');
        t.wordChars('.','.');
        t.wordChars('A','Z');
        t.ordinaryChar(',');
        List l = new ArrayList();
        t.quoteChar('"');
        while (t.nextToken() != StreamTokenizer.TT_EOF) {
            if (t.ttype == StreamTokenizer.TT_WORD || t.ttype == '"')
                l.add(t.sval);

             else if (t.ttype == ',')
                 ;
        }
        return (String[]) l.toArray(new String[l.size()]);
    }

    public static String[] parseColors (String s) throws IOException {
        StreamTokenizer t = new StreamTokenizer(new StringReader(s));
        t.ordinaryChar(',');
        t.wordChars('#','#');
        List l = new ArrayList();
        while (t.nextToken() != StreamTokenizer.TT_EOF) {
            if (t.ttype == StreamTokenizer.TT_WORD)
                l.add(t.sval);

             else if (t.ttype == ',')
                 ;
        }
        return (String[]) l.toArray(new String[l.size()]);
    }


    public static Double[] parseNums (String s) throws IOException {
        StreamTokenizer t = new StreamTokenizer(new StringReader(s));
        t.ordinaryChar(',');
        List l = new ArrayList();
        while (t.nextToken() != StreamTokenizer.TT_EOF) {
            if (t.ttype == StreamTokenizer.TT_NUMBER)
            {
                Double d = new Double(t.nval);
                l.add(d);
            }

        }
        return (Double[]) l.toArray(new Double[l.size()]);
    }

    public static Integer[] parseInts (String s) throws IOException {
        StreamTokenizer t = new StreamTokenizer(new StringReader(s));
        t.ordinaryChar(',');
        List l = new ArrayList();
        while (t.nextToken() != StreamTokenizer.TT_EOF) {
            if (t.ttype == StreamTokenizer.TT_NUMBER)
            {
                Double d = new Double(t.nval);
                l.add(new Integer(d.intValue()));
            }

        }
        return (Integer[]) l.toArray(new Integer[l.size()]);
    }

    public static String createCSV (List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            String s = values.get(i);
            sb.append(i == 0 ? s : ("," + s));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            String[] res = CSVParser.parse("a,1,2,34,b,\"q r s\",c");
//            Integer[] res2 = new CSVParser().parseIntegers("10,1,32,13,45");

            for (int i = 0; i < res.length; i++) {
                String re = res[i];
                System.out.println(re);

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
