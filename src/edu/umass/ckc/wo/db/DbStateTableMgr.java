package edu.umass.ckc.wo.db;


import org.apache.log4j.Logger;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 3/15/13
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbStateTableMgr {
    private static Logger logger = Logger.getLogger(DbStateTableMgr.class);


    private Connection conn;
//    private List<Column> cols;

    public DbStateTableMgr() {}

    public DbStateTableMgr(Connection c) throws SQLException {
        this.conn = c;
//        loadCols(tableName);

     }

    protected void create(int studId, String tableName) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "insert into " +tableName+ " (studId) values (?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.execute();
        }
        finally {
            if (stmt != null)
                stmt.close();
        }
    }

    /**  Get a Map that maps column names to the their datatypes as specified for MySQL columns (e.g. int, tinyint, varchar, double)
     *
     * @param table
     * @return
     * @throws SQLException
     */
    public Map<String,String> getColumnDataTypes (String table) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        Map<String,String> m = new Hashtable<String,String>(71);
        try {
            String q = "select COLUMN_NAME, DATA_TYPE from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ? and table_schema='wayangoutpostdb'";
            stmt = conn.prepareStatement(q);
            stmt.setString(1, table);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String cn= rs.getString(1);
                String dt= rs.getString(2);
                m.put(cn,dt);
            }
            return m;
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    public void  printCols (String table) throws SQLException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        try {
            String q = "select COLUMN_NAME, DATA_TYPE from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ? and table_schema='wayangoutpostdb'";
            stmt = conn.prepareStatement(q);
            stmt.setString(1,table);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String n=rs.getString(1);
                System.out.print("\"" + n + "\",");
            }

        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    /**
     * Return a string like ('a', 'b', 'c') where a, b, c are strings in the colNames
     * @param colNames
     * @return
     */
    public static String getColumnsString (List<String> colNames) {
        StringBuilder sb = new StringBuilder("(");
        for (String c : colNames) {
            sb.append("'" + c + "',");
        }
        // delete the last trailing comma
        sb.deleteCharAt(sb.length()-1);
        sb.append(")");
        return sb.toString();
    }



    protected String getColumnUpdateString (String[] cols) {
        StringBuilder sb = new StringBuilder();
        for (String colname : cols) {
            sb.append(" " + colname+"=?,");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    protected void setCols(Class c, Object model, PreparedStatement ps, String[] cols, Class smClass, Map<String, String> types)  {
        try {
            for (int i = 0; i < cols.length; i++) {
                String colName = cols[i];
                String colType = types.get(colName);
                setTableCol(c, model, ps, colName, i+1, smClass, colType);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // set the value of the column in the db row using studentModel class.
    private void setTableCol(Class c, Object model, PreparedStatement ps, String colName, int colIx, Class smClass, String colType) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(colName,smClass);
            Method m = pd.getReadMethod();
            Type t= m.getGenericReturnType();
            Object val = m.invoke(model);
//            System.out.println("Saving value of " + val.toString());
            if (val != null)
                ps.setObject(colIx, val);
            else {
                if (colType.equals("int"))
                    ps.setNull(colIx, Types.INTEGER);
                else if (colType.equals("varchar"))
                    ps.setNull(colIx, Types.VARCHAR);
                else if (colType.equals("double"))
                    ps.setNull(colIx, Types.DOUBLE);
            }

        } catch (Exception e) {
            System.out.println("Cannot write value for column " + colName );
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public int updateIntColumn(int studId, String col, int value, String tableName) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "update "+tableName+ " set " +col+ "=? where studid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, value);
            stmt.setInt(2,studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }



    public int save(Object model, int studId, String tableName, String[] tableCols, Class smClass) throws SQLException {
        PreparedStatement ps=null;
        try {
            Map<String,String> types = getColumnDataTypes(tableName);
            String q = "update "  +tableName+ " set " + getColumnUpdateString(tableCols) + " where studId=?";
            ps = conn.prepareStatement(q);
            setCols(smClass,model,ps, tableCols, smClass, types);
            int ix= tableCols.length;
            ps.setInt(ix+1,studId);
            return ps.executeUpdate();
        }
        catch (SQLException exc) {
            System.out.println("Failed on save of table: " + tableName + " for columns:");
            for (String c : tableCols)
                System.out.print(c + ",");
            System.out.println();
            throw exc;
        }
        finally {
            if (ps != null)
                ps.close();
        }
    }

    void setObjectPropVal(PropertyDescriptor d, String name, Object model, ResultSet rs, String colType)  {
        try {
            Method setter = d.getWriteMethod();
            Object o = rs.getObject(name);  // if declared as unsigned int, get Long, signed int => Integer
            // problem:  if a tinyint is used to represent a boolean we get back an Integer object
            if (o == null)
                return;
            //  problem:  if a tinyint is used to represent a boolean we get back an Integer object
            // so if the column type is tinyint we cast the object to a boolean so we can call the method correctly
            if (colType.equals("tinyint") )
                if (o != null)
                    o = new Boolean(((Integer) o) == 1);
                else o = new Boolean(false);
            setter.invoke(model,o);
        } catch (Exception e) {
            System.out.println("Failed to load property " + name);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


    public void load(int studId, Object model, String tableName, String[] cols, Class smClass) throws SQLException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        Map<String,String> colTypes = getColumnDataTypes(tableName);
        try {
            String q = "select * from " + tableName + " where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            rs = stmt.executeQuery();
            // If no student model row exists for this user,  they must be new.   Create an empty row
            // and then load which will get all the default values declared in the db table.
            if (!rs.next()) {
                create(studId, tableName);
                load(studId,model, tableName, cols, smClass);  // potential for infinite loop if create fails.
            }
            else {
                try {
                    for (String col: cols) {
                        PropertyDescriptor d = new PropertyDescriptor(col, smClass);
                        String dt =  colTypes.get(col);
                        setObjectPropVal(d, col, model, rs,dt);
                    }
                } catch (IntrospectionException e) {
                    System.out.println("Failed to find accessor " + cols + " in the class " + smClass.getName() + " Make sure there is a get method with exactly this name.");
                    throw e;
                }

            }
        }
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }


    public static int clear(Connection conn, String table, int studId) throws SQLException {
        PreparedStatement stmt=null;
        try {
            String q = "delete from "+table+" where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1,studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }
}
