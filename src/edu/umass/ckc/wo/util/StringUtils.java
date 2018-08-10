/*
** PREfBLE
**
** (c) Copyright University of Massachusetts
**
** $Workfile: StringUtils.java$
** $Subproject: util$
** $Project: JavaSource$
** $Date: 2013/10/18 16:13:08 $
**
*/
package edu.umass.ckc.wo.util;

import java.lang.String;
import java.lang.StringBuffer;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class StringUtils
{
  /**
  * substitute all occurences of the find arg with the replace arg in the
  * source arg.
  * @param source the source string to replace values in.
  * @param find the substring to replace
  * @param replace the replacement substring
  * @return String the source string with substitutions
  */
  public static String substitute(String source, String find, String replace)
  {
    if(source == null || find == null || replace == null)
    {
      return null;
    }

    StringBuffer buffer = new StringBuffer();
    int index = 0;
    int rc = source.indexOf(find, index);
    final int size = find.length();
    while(rc >= 0)
    {
      if (index != rc)
      {
        buffer.append(source.substring(index, rc));
      }
      buffer.append(replace);
      index = rc + size;
      rc = source.indexOf(find, index);
    }
    // concatenate the last piece
    buffer.append(source.substring(index));
    return buffer.toString();
  }


  /**
  * substitute all occurences of the find arg with the replace arg in the
  * source arg.
  * @param source the source string to replace values in.
  * @param find the character to replace
  * @param replace the replacement substring
  * @return String the source string with substitutions
  */
  public static String substitute(String source, char find, String replace)
  {
    if(source == null || replace == null)
    {
      return null;
    }

    StringBuffer buffer = new StringBuffer();
    int index = 0;
    int rc = source.indexOf(find, index);
    final int size = 1;//find.length();
    while(rc >= 0)
    {
      if (index != rc)
      {
        buffer.append(source.substring(index, rc));
      }
      buffer.append(replace);
      index = rc + size;
      rc = source.indexOf(find, index);
    }
    // concatenate the last piece
    buffer.append(source.substring(index));
    return buffer.toString();
  }

  /**
  * substitute all occurences of the find arg with the replaceFind arg in the
  * source arg.  if the find arg is preceded by the escape char, then substitute
  * the replaceEscape value instead.
  * @param source the source string to replace values in.
  * @param find the character to replace
  * @param escape the escape character for the find character
  * @param replaceFind the replacement substring when find is found and not preceded by
  * the escape character
  * @param replaceEscape the replacement substring when find is found preceded by the
  * escape character
  * @return String the source string with substitutions
  */
  public static String substitute(String source,
                                  char find,
                                  char escape,
                                  String replaceFind,
                                  String replaceEscape)
  {
    if(source == null || replaceFind == null || replaceEscape == null)
    {
      return null;
    }
    StringBuffer buffer = new StringBuffer();
    for(int i=0; i<source.length(); ++i)
    {
      char c = source.charAt(i);
      if(c == find)
      {
        if(i>0)
        {
          char previous = source.charAt(i-1);
          if(previous == escape)
          {
            buffer.append(replaceEscape);
          }
          else
          {
            buffer.append(replaceFind);
          }
        }
        else
        {
          buffer.append(replaceFind);
        }
      }
      else
      {
        buffer.append(c);
      }
    }
    return buffer.toString();
  }


  /** formats string for inclusion in URL by replacing certain
   *  characters with escape character equivalents
   */
  public static String formatURLText(String src)
  {
    String str=new String(src);
    str=substitute(str, "%", "%25");
    str=substitute(str, "+", "%2B");
    str=substitute(str, " ", "%20");
    str=substitute(str, "/", "%2F");
    str=substitute(str, "?", "%3F");
    str=substitute(str, ":", "%3A");
    str=substitute(str, ";", "%3B");
    str=substitute(str, "&", "%26");
    str=substitute(str, "@", "%40");
    str=substitute(str, "=", "%3D");
    str=substitute(str, "#", "%23");
    str=substitute(str, ">", "%3E");
    str=substitute(str, "<", "%3C");
    str=substitute(str, "{", "%7B");
    str=substitute(str, "}", "%7D");
    str=substitute(str, "[", "%5B");
    str=substitute(str, "]", "%5D");
    str=substitute(str, "\"", "%22");
    str=substitute(str, "'", "%27");
    str=substitute(str, "`", "%60");
    str=substitute(str, "^", "%5E");
    return str;
  }

  /**
   * replaces html formatting in text with character entities so that the
   * raw text is viewable in browser
   */
  public static String htmlTagsToCharEntities(String source)
  {
    if(source == null || source.length() < 1)
      return "";
    String str=substitute(source, "<", "&lt;");
    str=substitute(str, ">", "&gt;");
    return str;
  }

  /**
   * format the string into an acceptable sql string by substituting special
   * characters.
   */
  public static String escapeSQLChars(String source)
  {
    return StringUtils.substitute(source, "'", "''");
  }

    /**
     * get a properly formatted date string
     * @param d the date object
     * @param includeMilliseconds true to include the millisecond portion of
     * the timestamp (NOTE: MS Access does NOT accept a date/time stamp with
     * milliseconds)

     */
  public static String getDbTimeDateString(Date d, boolean includeMilliseconds)
  {
    String tmp;
    if (d instanceof Timestamp)
      d = timestampToDate((Timestamp) d);

    SimpleDateFormat f = new SimpleDateFormat(dbTsFormat_);
    tmp = f.format(d);

    if(includeMilliseconds)
      return tmp;
    return tmp.substring(0, tmp.lastIndexOf("."));
  }

  /** Attempt to build a Date from the given string and format.  If it builds,
   *  return the canonical format of the date
   */
  private static String getDateFromStringHelper (String date, String format)
  {
    Date res=null;
    SimpleDateFormat df = new SimpleDateFormat(format);
    try
    {
      res = df.parse(date);
    }
    catch (Exception ex)
    {
      return null;
    }
    return new SimpleDateFormat(dtFormat_).format(res);
  }

  /** Attempt to build a Date from the given string using the given format.  If it builds,
   *  return the canonical format of the timestamp.
   */
  private static String getCanonicalTimestampStringHelper (String date,
                                                           String format)
  {
    Date res=null;
    SimpleDateFormat df = new SimpleDateFormat(format);
    try
    {
      res = df.parse(date);
    }
    catch (Exception ex)
    {
      return null;
    }
    return new SimpleDateFormat(tsFormat_).format(res);
  }

  /** Convert a String timestamp into an actual Date object. */
  // TO_DO_TIMEZONE
  public static Date getTimestampFromString (String tsStr) throws Exception
  {
    tsStr = getCanonicalTimestampString(tsStr);
    SimpleDateFormat f = new SimpleDateFormat(tsFormat_);
    return f.parse(tsStr);
  }

  /** Given a timestamp string, return a db-acceptable timestamp string if the string complies with one
   *  of several formats.  If the string does not comply, return null.
   */
  public static String getCanonicalTimestampString (String timestampString)
  {
    String[] formats = new String[]
      {
      "yyyy-MM-dd hh:mm:ss.SSS a",
        "yyyy-MM-dd hh:mm:ss a",
        "yyyy-MM-dd hh:mm a",
        "yyyy-MM-dd HH:mm:ss.SSS",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm",
        "MM/dd/yyyy hh:mm:ss.SSS a",
        "MM/dd/yyyy hh:mm:ss a",
        "MM/dd/yyyy hh:mm a",
        "MM/dd/yyyy HH:mm:ss.SSS",
        "MM/dd/yyyy HH:mm:ss",
        "MM/dd/yyyy HH:mm"
    }; // don't alter this ordering.
    String d;
    for (int i=0;i<formats.length;i++)
      if ((d = getCanonicalTimestampStringHelper(timestampString,
                                                 formats[i])) != null)
        return d;
    return null;

  }

  /** Given a timestamp string, return a db-acceptable date string if the string complies with one
   *  of several formats.  If the string does not comply, return null.
   */
  // TO_DO_TIMEZONE
  public static String getDateFromString (String timestamp)
  {
    String d;
    if ((d = getDateFromStringHelper(timestamp,"yyyy-MM-dd")) != null)
      return d;
    else if ((d = getDateFromStringHelper(timestamp,"MM/dd/yyyy")) != null)
      return d;
    return null;
  }



  /**
         * Returns a US Locale standard timestamp with the requested options
         * @param time is the time to convert
         * @param includeSeconds indicates whether seconds should be included
         * @param includeMilliseconds indicatese whether milliseconds should be included
         * @param includeAMPM indicates whether AM/PM should be included
         * @return
         */
  // TO_DO_TIMEZONE
  public static String getDateTimeString(Timestamp time, boolean includeSeconds,
                                         boolean includeMilliseconds,
                                         boolean includeAMPM) throws Exception
  {
    String[] formats = new String[]
      {
      "MM/dd.yyyy hh:mm:ss.SSS a",
        "MM/dd.yyyy hh:mm:ss a",
        "MM/dd/yyyy hh:mm a",
        "MM/dd/yyyy HH:mm"
    };

    if(includeSeconds && includeMilliseconds && includeAMPM)
      return getDateTimeStringHelper(time,formats[0]);
    else if(includeSeconds && includeAMPM)
      return getDateTimeStringHelper(time,formats[1]);
    else if(includeAMPM)
      return getDateTimeStringHelper(time,formats[2]);
    return getDateTimeStringHelper(time,formats[3]);
  }

  /**
     * This is a helper function for getDateTimeString()
         * @param time will be returned as a String
         * @param format is the format to display the Timestamp in
         * @return time as a String
         * @throws Exception
         */
  private static String getDateTimeStringHelper(Timestamp time,
                                                String format) throws Exception
  {
    SimpleDateFormat f = new SimpleDateFormat(format);
    Date d = new Date(time.getTime());
    return  f.format(d).toString();
  }



  /**
 * get a properly formatted date string for the current time
 * @param includeMilliseconds true to include the millisecond portion of
 * the timestamp (NOTE: MS Access does NOT accept a date/time stamp with
 * milliseconds)
 */
  // TO_DO_TIMEZONE - OK
  public static String getDbTimeDateString(boolean includeMilliseconds)
  {
    Date d = new Date();
    return getDbTimeDateString(d, includeMilliseconds);
  }

  /**
   * remove a recurrent character from a string
   */
  public static String removeRecurrentChars(char ch, String source)
  {
    if(source == null)
      return null;
    StringBuffer buffer = new StringBuffer();
    boolean found = false;
    for(int i=0; i<source.length(); ++i)
    {
      char current = source.charAt(i);
      if(found)
      {
        if(current != ch)
        {
          buffer.append(current);
          found = false;
        }
      }
      else if(current == ch)
      {
        found = true;
        buffer.append(current);
      }
      else
      {
        buffer.append(current);
      }
    }
    return buffer.toString();
  }

  /**
   *
   * @param ts
   * @return
   */
  public static Date timestampToDate (Timestamp ts)
  {
    return new Date(ts.getTime() + (ts.getNanos() / 1000000));
  }

  private static final String dbTsFormat_ = "yyyy-MM-dd HH:mm:ss.SSS";
  private static final String tsFormat_ = "MM/dd/yyyy HH:mm:ss.SSS";
  private static final String dtFormat_ = "MM/dd/yyyy";

  /** Given a java.util.Date convert it to a String in mm/dd/yyyy hh:mm:ss
   *  format.
   */
  // TO_DO_TIMEZONE - OK
  public static String getTimestampString (Timestamp ts)
  {
    Date dd = timestampToDate(ts);
    SimpleDateFormat formatter =  new SimpleDateFormat (tsFormat_) ;
    return formatter.format(dd);
  }

  /** Given a java.util.Date convert it to a String in mm/dd/yyyy hh:mm:ss
   *  format.
   */
  // TO_DO_TIMEZONE - OK
  public static String getTimestampString ()
  {
    Date d = new Date();
    SimpleDateFormat formatter
      =  new SimpleDateFormat (tsFormat_) ;

    return formatter.format(d);
  }

  /** Given a string in the format returned by getTimestampString
   *  parse it and return a java.util.Date.
   */
  public static Timestamp parseTimestampString (String d) throws java.text.ParseException
    {
    SimpleDateFormat formatter
      =  new SimpleDateFormat (tsFormat_) ;
    Date dt = formatter.parse(d);
    return new Timestamp(dt.getTime());
  }

  /**
   *
   * @param d
   * @return
   * @throws java.text.ParseException
   */
  public static Timestamp parseDateString (String d) throws java.text.ParseException
    {
    SimpleDateFormat formatter
      =  new SimpleDateFormat (dtFormat_) ;
    Date dt = formatter.parse(d);
    return new Timestamp(dt.getTime());
  }

  /**
   * remove all the characters from the source string that occur in the
   * ignorestr argument
   */
  public static String removeChars(String ignoreStr, String source)
  {
    if(ignoreStr == null || source == null)
      return source;
    StringBuffer tmp = new StringBuffer(source.length());
    for(int i=0; i<source.length(); ++i)
    {
      boolean found = false;
      for(int j=0; j<ignoreStr.length(); ++j)
      {
        if(source.charAt(i) == ignoreStr.charAt(j))
          found = true;
      }
      if(!found)
        tmp.append(source.charAt(i));
    }
    return tmp.toString();
  }


  /**
   * remove all the characters from the source string that occur in the
   * ignorestr argument
   */
  public static String escapeChars(String ignoreStr,
                                   char escapeChar, String source)
  {
    if(ignoreStr == null || source == null)
      return source;
    StringBuffer tmp = new StringBuffer(source.length() + 10);
    for(int i=0; i<source.length(); ++i)
    {
      boolean found = false;
      for(int j=0; j<ignoreStr.length(); ++j)
      {
        if(source.charAt(i) == ignoreStr.charAt(j))
          found = true;
      }
      if(found)
      {
        tmp.append(escapeChar);
      }
      tmp.append(source.charAt(i));
    }
    return tmp.toString();
  }





  /**
   * find the first unescaped character specified.  The escape character is '\'.
   * return the position of the character if found, otherwise return -1
   * @param source the string to search
   * @param cfind the character to find.
   * @param startpos the index to start searching for the character.
   */
  public static int findUnescaped(String source, char cfind, int startpos)
  {
    char escaped = '\\';
    char previous = cfind;
    if(source == null)
      return -1;
    for(int i=startpos; i<source.length(); ++i)
    {
      char current = source.charAt(i);
      if(current == cfind)
      {
        if(i == 0 || previous != escaped)
          return i;
      }
      previous = source.charAt(i);
    }
    return -1;
  }

  /**
 * find the first unescaped character specified.  The escape character is '\'.
 * return the position of the character if found, otherwise return -1
 * @param source the string to search
 * @param escapeChar
 * @param cfind the character to find.
 * @param startpos the index to start searching for the character.
 */
  public static int findUnescaped(String source,
                                  char escapeChar, char cfind, int startpos)
  {
    char escaped = escapeChar;
    char previous = cfind;
    if(source == null)
      return -1;
    for(int i=startpos; i<source.length(); ++i)
    {
      char current = source.charAt(i);
      if(current == cfind)
      {
        if(i == 0 || previous != escaped)
          return i;
      }
      previous = source.charAt(i);
    }
    return -1;
  }


  /**
   * test for a alphanumeric character string
   */
  public static boolean isAlphaNum(String str)
  {
    str = str.toLowerCase();
    for(int i=0; i<str.length(); ++i)
    {
      char alpha = str.charAt(i);
      if(!((alpha >= 'a' && alpha <= 'z') || (alpha >= '0' && alpha <= '9')))
      {
        return false;
      }
    }
    return true;
  }
  /**
   * test for a numeric (integer) character string
   */
  public static boolean isNum(String str)
  {
    str = str.toLowerCase();
    for(int i=0; i<str.length(); ++i)
    {
      char alpha = str.charAt(i);
      if(!(alpha >= '0' && alpha <= '9'))
      {
        return false;
      }
    }
    return true;
  }

  /**
   * check if the string is a numeric value (integer or float)
   * @param str
   * @return
   */
  public static boolean isNumeric(String str)
  {
    try
    {
      float value = Float.parseFloat(str);
      return true;
    }
    catch(Exception ex)
    {
      return false;
    }
  }
  /**
   * safely trim string, including null string
   * @param str any string or null
   * @return returns str w/white space trimmed if str is not null, otherwise returns empty string
   */
  public static String safeTrim(String str)
  {
    if (str!=null)
      return str.trim();
    else
      return "";
  }

  /**
   * make a zero length or whitespace string null
   */
  public static String emptyAsNull(String str)
  {
    if(str == null)
      return str;
    str.trim();
    if(str.length() < 1)
      return null;
    return str;
  }

  /**
   * if the string argument is null, then return an empty string "" instead
   * otherwise return the string
   * @param str the string to test
   * @return an empty string (if str is null) or str
   */
  public static String nullAsEmpty(String str)
  {
    if(str == null)
    {
      return "";
    }
    return str;
  }

  /**
   * strip the quotes from the source string, this will remove
   * the leading and ending single or double quote.
   * @param value the source string
   * @param checkFirst if check first, then make sure the string
   * begins and ends with double or single quotes, if not, then
   * just return the string
   * @return the string, unquoted
   */
  public static String stripQuotes(String value, boolean checkFirst)
  {
    if(StringUtils.emptyAsNull(value) == null)
    {
      return value;
    }
    value = value.trim();
    if(value.length() < 2)
    {
      return value;
    }
    if(checkFirst)
    {
      if((value.charAt(0) == '\'' && value.charAt(value.length()-1) == '\'') ||
          (value.charAt(0) == '"' && value.charAt(value.length()-1) == '"'))
      {
        return value.substring(1, value.length() - 1);
      }
      return value;
    }
    return value.substring(1, value.length() - 1);
  }

  /**
   * check if a string is a quoted string, using either double or single quotes
   * @param value the source string
   * @return true if quoted string
   */
  public static boolean isQuotedString(String value)
  {
    if(StringUtils.emptyAsNull(value) == null)
    {
      return false;
    }
    value = value.trim();
    if(value.length() < 2)
    {
      return false;
    }
    if((value.charAt(0) == '\'' && value.charAt(value.length()-1) == '\'') ||
        (value.charAt(0) == '"' && value.charAt(value.length()-1) == '"'))
    {
      return true;
    }
    return false;
  }

  /**
   * single quote the source string
   * @param source the source
   * @return
   */
  public static String getQuotedString(String source)
  {
    return "'" + source + "'";
  }

  /**
   * search and replace a substring within a string.  This allows for options
   * such as case sensitive searching and whole word matching.
   * @param source - the source to edit
   * @param search - the value to search for
   * @param replace - the replacement value
   * @param caseSensitive - if true then do a case sensitive search, otherwise do
   * a case insensitive search
   * @param wholeword - if true, then the searched for string must be a whole word which
   * is defined as a string that does not have any alpha-numeric (or '-') characters
   * adjacent to it.
   * @return the new string with any replacements in it
   */
  public static String searchAndReplace(String source, String search, String replace,
                                        boolean caseSensitive,
                                        boolean wholeword)
  {
    int curpos = 0;
    int lastpos = curpos;
    StringBuffer tmp = new StringBuffer();
    final int searchlen = search.length();
    while(curpos >= 0)
    {
      curpos = find(source, search, curpos, caseSensitive, wholeword);
      if(curpos < 0)
      {
        tmp.append(source.substring(lastpos));
      }
      else
      {
        tmp.append(source.substring(lastpos, curpos));
        tmp.append(replace);
        lastpos = curpos + searchlen;
        curpos = lastpos;
      }
    }
    return tmp.toString();
  }

  /**
   * search for a substring within a string.  this advanced version of find
   * allows for the following options:
   * case search option
   * whole word match
   * @param source - the source to in
   * @param search - the string to search for
   * @param startpos - the starting position to start the search on
   * @param caseSensitive - if true then do a case sensitive search, otherwise do
   * a case insensitive search
   * @param wholeword - if true, then the searched for string must be a whole word which
   * is defined as a string that does not have any alpha-numeric (or '-') characters
   * adjacent to it.
   * @return -1 if not found otherwise the character position of the first character
   * of the search string.
   */
  public static int find(String source, String search,
                         int startpos, boolean caseSensitive,
                         boolean wholeword)
  {
    if(emptyAsNull(source) == null || emptyAsNull(search) == null || startpos >= source.length())
      return -1;
    // check for lower case
    if(caseSensitive == false)
    {
      search = search.toLowerCase();
      source = source.toLowerCase();
    }
    int delimlen = search.length();
    for(int i=startpos; i<source.length(); ++i)
    {
      if(source.startsWith(search, i))
      {
        if((wholeword && isWholeWord(source, i, delimlen)) || !wholeword)
        {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * check if the match is a whole word.  A whole
   * word is defined as a sequence of characters that is delimited by
   * non-alphanumeric (nor '-') character (before and after).  So the following sequences are
   * defined for a search of 'foo':
   * 'foo bar' - valid whole word match
   * 'foo-bar' - not a whole word
   * '<tr>foo</tr>' - valid whole word match
   * 'foobar' - not a whole word
   * 'foo1' - not a whole word
   * @param source the source to check for leading/trailing characters
   * @param startpos the starting position of the possible match
   * @param delimlen the length of the matching delimiter
   * @return true if this is a generic type and is a whole word match
   */
  public static boolean isWholeWord(String source, int startpos, int delimlen)
  {
    char before = ' ';
    char after = ' ';
    if(startpos > 0)
    {
      before = source.charAt(startpos - 1);
    }
    if(source.length() > (startpos + delimlen))
    {
      after = source.charAt(startpos + delimlen);
    }
    if((Character.isLetterOrDigit(before) == false) && (Character.isLetterOrDigit(after) == false)
       && (before != '-') && (after != '-'))
   {
     return true;
   }
   return false;
  }

  /**
   * split the string into a set of strings using the delimiter string.
   * If the source or delimiter are empty, then return a vector object
   * with no elements in it.
   * @param source the source to parse
   * @param delimiter the delimiter to parse on
   * @return the vector of strings
   */
  public static Vector stringToVector(String source, String delimiter)
  {
    Vector tmp = new Vector();
    if(source == null || delimiter == null || delimiter.length() < 1)
      return tmp;
    int delimlen = delimiter.length();
    int startpos = 0;
    for(int i=0; i<source.length(); ++i)
    {
      if(source.startsWith(delimiter, i))
      {
        String sub = source.substring(startpos, (i - startpos));
        tmp.addElement(sub);
        i += delimlen;
        startpos = i;
      }
    }
    return tmp;
  }

  /**
           *parses source string at opendelim and place into a vector.  Each
           *element in the vector is a String
           * NOTE: the delimiters must match otherwise a CkcException is thrown.
           */
  public static Vector stringToVector(String source, char delim)
  {
    Vector v = new Vector();
    if(source == null || source.length() < 1)
    {
      return v;
    }

    String curToken="";
    for(int i = 0; i < source.length(); i ++)
    {
      if(source.charAt(i) == delim)
      {
        v.addElement(curToken);
        curToken="";
      }
      else
        curToken += source.charAt(i);
    }
    v.addElement(curToken);

    return v;
  }

  /**
  *parses source string at opendelim and place into a vector.  Each
  *element in the vector is a String.  Trims whitespace from each element.
  * NOTE: the delimiters must match otherwise a CkcException is thrown.
  */
  public static Vector stringToVectorUsingTrim(String source, char delim)
  {
    Vector v = new Vector();
    if(source.length() < 1)
    {
      return v;
    }

    String curToken="";
    for(int i = 0; i < source.length(); i ++)
    {
      if(source.charAt(i) == delim)
      {
        v.addElement(curToken.trim());
        curToken="";
      }
      else
        curToken += source.charAt(i);
    }
    v.addElement(curToken.trim());

    return v;
  }


  /**
       *creates a delimited String from the elements in vect and delim
       */
  public static String vectorToString(Vector vect, String delim)
  {
    StringBuffer make = new StringBuffer();
    for(int i = 0; i < vect.size(); i++)
    {
      if(i>0)
        make.append(delim);
      make.append(vect.elementAt(i).toString());

    }

    return make.toString();
  }



  /**
   *  parse a string into two pieces on the delimited.  In all cases the tuple's members
   *  will be valid string (though they may be empty).
   *  @param source the source string
   *  @param delim the delim to parse on
   *  @return a twotuple (each member a String) with the first string the characters before
   *  the delim char and the second string the characters after the first
   *  delim found.
   */
  public static TwoTuple splitStringInTwo(String source, char delim)
  {
    TwoTuple t = new TwoTuple("", "");
    if(source == null)
    {
      return t;
    }
    int index = source.indexOf(delim);
    if(index < 0)
    {
      t.setFirst(source);
      return t;
    }
    int len = source.length();
    t.setFirst(source.substring(0, index));
    if(index >= len)
    {
      return t;
    }
    t.setSecond(source.substring(index, len));
    return t;
  }

  /**
   *  parse a string into two pieces on the delimited.  In all cases the tuple's members
   *  will be valid string (though they may be empty).
   *  @param source the source string
   *  @param delim the delim to parse on
   *  @return a twotuple (each member a String) with the first string the characters before
   *  the delim char and the second string the characters after the first
   *  delim found.
   */
  public static TwoTuple splitStringInTwo(String source, String delim)
  {
    TwoTuple t = new TwoTuple("", "");
    if(source == null)
    {
      return t;
    }
    int index = source.indexOf(delim);
    if(index < 0)
    {
      t.setFirst(source);
      return t;
    }
    int len = source.length();
    t.setFirst(source.substring(0, index));
    if(index >= len)
    {
      return t;
    }
    t.setSecond(source.substring(index + delim.length(), len));
    return t;
  }

  /**
       *  parse a string into three pieces on the delimiter.  In all cases the tuple's members
                         *  will be valid string (though they may be empty).
       *  @param source the source string
       *  @param delim the delim to parse on
                         *  @return a threetuple (each member a String) parsed without the delimiter char
                         *  delim found.
       */
  public static ThreeTuple splitStringInThree(String source, char delim)
  {
    TwoTuple t1 = splitStringInTwo(source, delim);
    TwoTuple t2 = splitStringInTwo((String)t1.getSecond(), delim);
    return new ThreeTuple(t1.getFirst(), t2.getFirst(), t2.getSecond());
  }

  /**
   * debug print a tree, this assumes that the keys and values
   * are strings.
   * @param tree
   * @return the values and keys (one pair per line) as key=value
   */
  public static String debugPrint(TreeMap tree)
  {
    StringBuffer buf = new StringBuffer();
    Set keys = tree.keySet();
    Iterator iter = keys.iterator();
    while(iter.hasNext())
    {
      String key = (String)iter.next();
      String value = (String)tree.get(key);
      buf.append(key).append("=").append(value);
      buf.append("\n");
    }
    return buf.toString();
  }

  /**
   * a simple ascii encoder that uses the pad to encode the input
   * string.  The pad and source string may only contain ascii characters
   * in the range of '0'-'Z'
   * @param pad the pad to use for encryption
   * @param src the source string to encode
   * @return the encoded string
   */
  public static String encode(String src, String pad)
  {
    int minc = (int)'!'; // 33
    int maxc = (int)'~'; // 126
    int range = maxc - minc + 1;
    StringBuffer tmp = new StringBuffer(src.length() + 32);
    while(pad.length() < src.length())
    {
      pad += pad;
    }
    for(int i=0; i<src.length(); ++i)
    {
      int srcC = (int)src.charAt(i);
      int padC = (int)pad.charAt(i);
      int c = (srcC - minc) + (padC - minc);
      if(c > range)
        c -= range;
      c += minc;
      tmp.append((char)c);
    }
    return tmp.toString();
  }

  /**
   * a simple ascii decoder that uses the pad to decode the input
   * string.  The pad and source string may only contain ascii characters
   * in the range of '0'-'Z'
   * @param pad the pad to use for encryption
   * @param src the source string to decode
   * @return the decoded string
   */
  public static String decode(String src, String pad)
  {
    int minc = (int)'!'; // 33
    int maxc = (int)'~'; // 126
    int range = maxc - minc + 1;
    StringBuffer tmp = new StringBuffer(src.length() + 32);
    while(pad.length() < src.length())
    {
      pad += pad;
    }
    for(int i=0; i<src.length(); ++i)
    {
      int srcC = (int)src.charAt(i);
      srcC -= minc;
      int padC = (int)pad.charAt(i);
      padC -= minc;
      int c = srcC - padC;
      if(c < 0)
        c += range;
      c += minc;
      tmp.append((char)c);
    }
    return tmp.toString();
  }

  /**
  * This method removes all instances of the escape character in the
  * input string, except when the escape character is escaping itself.
  * @param source the input String
  * @param escapeChar the character used to escape
  * @return the String minus the escape characters
  */
  public static String removeEscapeChars(String source, char escapeChar)
  {
    if(source==null)
      return source;
    StringBuffer result=new StringBuffer();
    int lastEscapePos=0;
    for(int i=0; i<source.length(); ++i)
    {
      char c = source.charAt(i);
      if(c == escapeChar)
      {
        if(lastEscapePos==i-1)
        {
          result.append(c);
        }
        else
          lastEscapePos=i;
      }
      else
        result.append(c);
    }
    return result.toString();
  }

  /**
  * tokenize the input around all whitespace
  * @param source
  * @return Vector the tokens, could be an empty vector.
  */
  public static Vector tokenizeString(String source)
  {
    Vector tokens=new Vector();
    if(source==null)
      return tokens;
    char c, tokenChar=' ';
    int curpos=0, endpos=source.length();
    while(curpos<endpos)
    {
      StringBuffer curToken=new StringBuffer();
      c = source.charAt(curpos);
      while(c != tokenChar && curpos<endpos)
      {
        curToken.append(c);
        curpos++;
        if(curpos<endpos)
          c = source.charAt(curpos);
      }
      curpos++;
      if(curToken.length()>0)
        tokens.addElement(curToken.toString());
    }//end while
    return tokens;
  }

  /**
   * implementation of find_last_not_of
   * finds last position in string str with character different from arg, e.g.,
   * returns str.length()-1 if str does not end with arg, returns -1 if str consists
   * entirely of arg
   * @param str a string
   * @param arg a character
   * @return index of last character in str different from arg
   */
  public static int findLastNotOf(String str, char arg)
  {
    int n=str.length()-1;
    while (n >= 0 && str.charAt(n) == arg)
    {
      n--;
    }
    return n;
  }

  public static String booleanToString(boolean value)
  {
    if(value) return "true";
    return "false";
  }

  /**
   * count the number of times the character appears in teh string
   * @param source the source
   * @param c the character to look for
   * @return the number of times found
   */
  public static int countChars(String source, char c)
  {
    if(emptyAsNull(source) == null)
    {
      return 0;
    }
    int count = 0;
    for(int i=0; i<source.length(); ++i)
    {
      if(source.charAt(i) == c)
      {
        ++count;
      }
    }
    return count;
  }
  /**
  * calculates the number of "lines" in a string given the max width
  * @param  source - string to parse
  * @param  maxWidth - max width of a line in characters
  * @param  defaultRows - this is used if calculated lines < defaultRows
  * @param  extraRows - rows to append
  * @param  maxRows - maximum size to return. 0 = unlimited.
  * @return  returns number of lines.
  */
  public static int numberOfLines(String source,
                                  int maxWidth,
                                  int defaultRows, int extraRows, int maxRows)
  {
    if(source==null || source.equals(""))
      return defaultRows;
    char newLine = '\n';
    int count = 0;
    int lastpos = 0;
    int size = 0;
    int pos = source.indexOf(newLine, 0);

    // in testing, the column width appeared 2 characters less than specified
    //maxWidth -= 2;

    // find each newline
    while(pos != -1)
    {
      size = pos - lastpos;
      count += (size / maxWidth);
      if ((size % maxWidth) > 0)
      {
        ++count;
      }
      lastpos = pos + 1;
      pos = source.indexOf(newLine, lastpos);
    }
    size = (source.length() - lastpos);
    if (size > 0)
    {
      count += (size / maxWidth);
      if ((size % maxWidth) > 0)
      {
        ++count;
      }
    }

    // add the extraOutputParams rows to the count for a buffer
    count += extraRows;

    if(count < defaultRows)
    {
      count = defaultRows;
    }

    if ((count > maxRows) && (maxRows > 0))
    {
      count = maxRows;
    }
    return count;
  }

  //    public static void main (String[] args) {
  //        Timestamp now = new Timestamp(System.currentTimeMillis());
  //        String s = getTimestampString(now);
  //        Timestamp now2=null;
  //        try {
  //        now2 = parseTimestampString(s);
  //        } catch (Exception e) {}
  //        System.out.println("now =" +s);
  //        System.out.println("now2 =" +getTimestampString(now2));
  //    }

  /**
       *
       * @param args
       */
  //  public static void main (String[] args)
  //  {
  //    String d = getCanonicalTimestampString("10/12/1999 10:30:45 pm");
  //    System.out.println(d);
  //    d = getTimestampString(new Timestamp(System.currentTimeMillis()));
  //    System.out.println("Timestamp with milliseconds: " + d);
  //    d = getDbTimeDateString(true);
  //    System.out.println("Timestamp with milliseconds: " + d);
  //    d = getDbTimeDateString(false);
  //    System.out.println("Timestamp without milliseconds: " + d);
  //    d = getDbTimeDateString(new Date(),true);
  //    System.out.println("Date with milliseconds: " + d);
  //    d = getDbTimeDateString(new Date(),false);
  //    System.out.println("Date without milliseconds: " + d);
  //
  //    d = getDbTimeDateString(new Timestamp(System.currentTimeMillis()),true);
  //    System.out.println("Timestamp with milliseconds: " + d);
  //    d = getDbTimeDateString(new Timestamp(System.currentTimeMillis()),false);
  //    System.out.println("Timestamp without milliseconds: " + d);
  //    try
  //    {
  //      Date dt = getTimestampFromString("2001-12-10 12:13:14.135");
  //      String dts = getDbTimeDateString(dt,true);
  //      System.out.println(dts);
  //      System.out.println(dt.toString());
  //    }
  //    catch (Exception e)
  //    {
  //      System.out.println(e);
  //    }
  //  }


  //  public static void main(String args[]) throws Exception
  //  {
  //    Long t = new Long("1062247440000");
  //    long time = t.longValue();
  //    String timestampString = "2003-08-30 08:44:00.0";
  //    String dateString = "Sat Aug 30 08:44:00 EDT 2003";
  //    Timestamp timestamp = new Timestamp(time);
  //    Date date = new Date(time);
  //    String format = "MM/dd/yyyy hh:mm:ss.SSS a";
  //    System.out.println(getCanonicalTimestampString(timestampString));
  //    System.out.println(getDateFromString(timestampString));
  //    System.out.println(getDateTimeString(timestamp,true,true,true));
  //    System.out.println(getDateTimeStringHelper(timestamp,format));
  //    System.out.println(getDbTimeDateString(date,true));
  //    System.out.println(getDbTimeDateString(true));
  //    System.out.println(getTimestampFromString(timestampString));
  //    System.out.println(getTimestampString(timestamp));
  //    System.out.println(getTimestampFromString(timestampString));
  //  }
}
