package edu.umass.ckc.wo.db;



    import java.io.FileReader;
    import java.io.IOException;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;

    import au.com.bytecode.opencsv.CSVReader;

    public class ProblemStatusMarker {

        private static int updateStatus(Connection conn, String id, String status) throws SQLException {
            if (id.length()>0 && status.length()>0) {
                int idx = Integer.parseInt(id);
                PreparedStatement stmt = null;
                try {
                    String q = "update problem set status=? where id=?";
                    stmt = conn.prepareStatement(q);
                    stmt.setString(1, status);
                    stmt.setInt(2, idx);
                    return stmt.executeUpdate();
                } finally {
                    if (stmt != null)
                        stmt.close();
                }
            }
            return 0;
        }

        public static void main(String[] args)
        {
            CSVReader reader = null;
            try {
                Connection conn  = DbUtil.getAConnection("rose");

                //Get the CSVReader instance with specifying the delimiter to be used
                reader = new CSVReader(new FileReader("U:\\FlashProbsWithNewButtons\\AllProbsTesting.csv"),',');
                String [] nextLine;
                //Read one line at a time
                while ((nextLine = reader.readNext()) != null)
                {
                    String id=null;
                    String status=null;
                    int i = 0;
                    for(String token : nextLine)
                    {
                        i++;
                        if (i == 1)
                            id = token;
                        else if (i == 5)
                            status = token;
                    }
                    if (status != null && !status.equalsIgnoreCase("C")) {

                        id = id.trim();
                        if (id != null && !id.equals("") && !id.equals("id" )) {
                            System.out.println("ID: " + id + " : " + status);
                            updateStatus(conn,id,status);
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


