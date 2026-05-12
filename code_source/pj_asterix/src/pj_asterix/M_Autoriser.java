/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pj_asterix;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.sql.SQLException;
/**
 *
 * @author pergaixj
 */
public class M_Autoriser {
    private Db_mariadb db;
    private int idAutorisation;
    private int idRole;
    
    public M_Autoriser(Db_mariadb db, int idAutorisation, int idRole){
        this.db = db;
        this.idAutorisation = idAutorisation;
        this.idRole = idRole;
       
    }

    public Db_mariadb getDb() {
        return db;
    }

    public void setDb(Db_mariadb db) {
        this.db = db;
    }

    public int getIdAutorisation() {
        return idAutorisation;
    }

    public void setIdAutorisation(int idAutorisation) {
        this.idAutorisation = idAutorisation;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }
   
    public static LinkedHashMap<Integer, M_Autorisation>
        getLesAutorisation(Db_mariadb db, int idRole) throws Exception{
        LinkedHashMap<Integer, M_Autorisation> ListeAutorisation;
        ListeAutorisation = new LinkedHashMap();
        M_Autorisation uneAutorisation;

        int cle;
        String code, description, sql;
        sql = "SELECT * FROM mcd_autorisation AN"
                +" INNER JOIN mcd_autoriser AR ON AN.idAutorisation=AR.idAutorisation"+" WHERE idROle = "+idRole +" ORDER By code";
        ResultSet res;
        res = db.sqlSelect(sql);

        while(res.next()){
            cle = res.getInt("IdAutorisation");
            code = res.getString("code");
            description = res.getString("description");
            uneAutorisation = new M_Autorisation(db, idRole, code, description);
            ListeAutorisation.put(cle, uneAutorisation);
        }
        res.close();
        return ListeAutorisation;
    }

}
