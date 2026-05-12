package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_role {

    private Db_mariadb db;
    private int id;
    private String code;

    /* =====================
       CONSTRUCTEURS
       ===================== */

    // Constructeur simple (utilisé dans getRecords)
    public M_role(Db_mariadb db, int id, String code) {
        this.db = db;
        this.id = id;
        this.code = code;
        
    }

    // Constructeur INSERT
    public M_role(Db_mariadb db, String code) throws SQLException {
        this.db = db;
        this.code = code;

      

        String sql;
        sql = "INSERT INTO mcd_roles(code) VALUES ("
                + "'" + code + "', " ;

        db.sqlExec(sql);

        ResultSet res = db.sqlLastId();
        res.first();
        this.id = res.getInt("id");
        res.close();
    }

    // Constructeur SELECT par id
    public M_role(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        String sql;
        sql = "SELECT * FROM mcd_roles WHERE id = " + id + ";";

        ResultSet res = db.sqlSelect(sql);
        res.first();

        this.code = res.getString("code");


        res.close();
    }

    /* =====================
       GETTERS / SETTERS
       ===================== */

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

   

    /* =====================
       CRUD
       ===================== */

    public void update() throws SQLException {
        

        String sql;
        sql = "UPDATE mcd_roles SET "
                + "code = '" + code + "', "
                ;

        db.sqlExec(sql);
    }

    public void delete(int id) throws SQLException {
        String sql;
        sql = "DELETE FROM mcd_roles WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* =====================
       GET RECORDS
       ===================== */

    public static LinkedHashMap<Integer, M_role> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<Integer, M_role> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {

        LinkedHashMap<Integer, M_role> lesRoles = new LinkedHashMap<>();

        String sql;
        sql = "SELECT * FROM mcd_roles WHERE " + clauseWhere + " ORDER BY code;";

        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            int id = res.getInt("id");
            String code = res.getString("code");
            

            M_role unRole = new M_role(db, id, code);
            lesRoles.put(id, unRole);
        }

        res.close();
        return lesRoles;
    }

    /* =====================
       AFFICHAGE
       ===================== */

   
   

    /* =====================
       MAIN DE TEST
       ===================== */

    public static void main(String[] args) throws Exception {

        Db_mariadb mabase =
                new Db_mariadb(CL_Connection.url, CL_Connection.login, CL_Connection.password);

        LinkedHashMap<Integer, M_role> lesRoles =
                M_role.getRecords(mabase);

        for (Integer uneCle : lesRoles.keySet()) {
            M_role unRole = lesRoles.get(uneCle);
            System.out.println(unRole);
        }
    }
}
