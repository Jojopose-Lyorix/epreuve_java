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
public class M_Autorisation {
        private Db_mariadb db;
    private int idAutorisation;
    private String code;
    private String description;
    
    public M_Autorisation(Db_mariadb db, int idAutorisation, String code, String description){
        this.db = db;
        this.idAutorisation = idAutorisation;
        this.code = code;
        this.description = description;
                
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
     public static LinkedHashMap <Integer, M_Autorisation> getRecords (Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    } 
    public static LinkedHashMap <Integer, M_Autorisation> getRecords (Db_mariadb db, String clauseWhere) throws SQLException {
        LinkedHashMap <Integer, M_Autorisation> lesAutorisations;
        lesAutorisations = new LinkedHashMap();
        M_Autorisation unAutorisation;
        
        int cle;
        String code, description;
          
        String sql;
        sql = "SELECT * FROM mcd_autorisation WHERE "+clauseWhere +" ORDER BY code";
        ResultSet res;
        res = db.sqlSelect(sql);
        
        while (res.next()){
            cle = res.getInt("idAutorisation");
            code = res.getString("code");
            description = res.getString("description");
            
        
            unAutorisation = new M_Autorisation(db, cle, code, description);
            lesAutorisations.put(cle, unAutorisation);
        }
        res.close();
        return lesAutorisations;
    } 

    @Override
    public String toString() {
        return "M_Autorisation{" + "db=" + db + ", idAutorisation=" + idAutorisation + ", code=" + code + ", description=" + description +'}';
    }
    
    public static void main(String[] args) throws Exception{
        Db_mariadb mabase;
        mabase = new Db_mariadb(CL_Connection.url, CL_Connection.login, CL_Connection.password);
        
        M_Autorisation unAutorisation;
        
       // unAutorisation = new M_Autorisation(mabase, 1, "libelle", "truc");
       // System.out.println(unAutorisation.toString());
       
      // unRole = new M_Role(mabase, 1, "libelle");
      // System.out.println(unRole.toString());
        
     //  unRole = new M_Role(mabase, 5);
      // System.out.println(unRole.toString());
       
   
    //unRole.setLibelle("bob");

  
   LinkedHashMap <Integer, M_Autorisation> lesAutorisations;
   lesAutorisations = M_Autorisation.getRecords(mabase);
   
   for (Integer uneCle : lesAutorisations.keySet()){
       unAutorisation = lesAutorisations.get(uneCle);
       System.out.println(unAutorisation.toString());
   }
   
            
   
}
}
