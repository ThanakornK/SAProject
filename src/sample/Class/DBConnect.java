package sample.Class;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DBConnect {
    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:testDB.db"); //connecting to database
            System.out.println("Connected");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e+"");
        }
        return con;
    }

    public int insertRecord(String sql, Map<String,String> ParaCommands){             //ParaCommands = {{"str","food_name"},{"double,food_price"},{"int,food_amount"}}
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        int index = 1;  // for tell parameter position

        try {
            ps = con.prepareStatement(sql);

            for (String i : ParaCommands.keySet()) {
                switch (i) {
                    case "str":
                        ps.setString(index, ParaCommands.get(i));
                        index++;
                        continue;
                    case "double":
                        ps.setDouble(index, Double.parseDouble(ParaCommands.get(i)));
                        index++;
                        continue;
                    case "int":
                        ps.setInt(index, Integer.parseInt(ParaCommands.get(i)));
                        index++;
                        continue;
                }
            }

            ps.execute();
            System.out.println("Data has been insert");
            return 0;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return 1;

        }catch (NumberFormatException e){
            AlertBox alertBox = new AlertBox();
            alertBox.alertERR("err","ข้อมูลที่กรอกไม่ถูกต้อง");
            return 1;
        }
    }

    public int updateRecord(String sql, Map<String,String> ParaCommands) { // last parameter must be pk
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;
        int index = 1;

        try {
            ps = con.prepareStatement(sql);

            for (String i : ParaCommands.keySet()) {
                switch (i) {
                    case "str":
                        ps.setString(index, ParaCommands.get(i));
                        index++;
                        continue;
                    case "double":
                        ps.setDouble(index, Double.parseDouble(ParaCommands.get(i)));
                        index++;
                        continue;
                    case "int":
                        ps.setInt(index, Integer.parseInt(ParaCommands.get(i)));
                        index++;
                        continue;
                }
            }

            ps.execute();
            System.out.println("Data has been updated");
            return 0;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return 1;

        }catch (NumberFormatException e) {
            return 1;

        }
    }

    public int deleteRecord(String sql, String pkType, String pkName) {
        Connection con = DBConnect.connect();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sql);
            switch (pkType) {
                case "str":
                    ps.setString(1, pkName);
                    break;
                case "double":
                    ps.setDouble(1, Double.parseDouble(pkName));
                    break;
                case "int":
                    ps.setInt(1, Integer.parseInt(pkName));
                    break;
            }
            ps.execute();
            System.out.println("Data has been deleted");

        } catch (SQLException e) {
            System.out.println(e.toString());
            return 1;

        } catch (NumberFormatException e) {
            return 1;
        } finally {
            try {
                ps.close();
                con.close();

                return 0;

            } catch (SQLException e) {
                e.printStackTrace();
                return 1;
            }
        }

    }
}
