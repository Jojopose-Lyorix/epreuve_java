package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_Adorer {

    private Db_mariadb db;

    private int peupleId;
    private int diviniteId;
    private String commentaire;

    // Constructeur simple (utilisé dans getRecords) - PRIVÉ
    private M_Adorer(Db_mariadb db, int peupleId, int diviniteId, String commentaire) {
        this.db = db;
        this.peupleId = peupleId;
        this.diviniteId = diviniteId;
        this.commentaire = commentaire;
    }

    // Méthode statique pour INSERT (remplace le 2ème constructeur)
    public static M_Adorer createAndInsert(Db_mariadb db, int peupleId, 
                                            int diviniteId, String commentaire) throws SQLException {
        
        String comSql = (commentaire == null) ? "NULL" : "'" + commentaire + "'";

        String sql = "INSERT INTO mcd_adorer(peuple_id, divinite_id, commentaire) VALUES ("
                + peupleId + ", "
                + diviniteId + ", "
                + comSql + ");";

        db.sqlExec(sql);
        
        return new M_Adorer(db, peupleId, diviniteId, commentaire);
    }

    // Constructeur SELECT (par PK composée)
    public M_Adorer(Db_mariadb db, int peupleId, int diviniteId) throws SQLException {
        this.db = db;
        this.peupleId = peupleId;
        this.diviniteId = diviniteId;

        String sql = "SELECT * FROM mcd_adorer WHERE peuple_id = " + peupleId
                + " AND divinite_id = " + diviniteId + ";";

        ResultSet res = db.sqlSelect(sql);
        res.first();

        this.commentaire = res.getString("commentaire");
        res.close();
    }

    // Getters
    public int getPeupleId() { return peupleId; }
    public int getDiviniteId() { return diviniteId; }
    public String getCommentaire() { return commentaire; }

    // Setter
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public void update() throws SQLException {
        String comSql = (commentaire == null) ? "NULL" : "'" + commentaire + "'";

        String sql = "UPDATE mcd_adorer SET "
                + "commentaire = " + comSql + " "
                + "WHERE peuple_id = " + peupleId + " "
                + "AND divinite_id = " + diviniteId + ";";

        db.sqlExec(sql);
    }

    // Corrigé : utilise les attributs de l'instance
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_adorer WHERE peuple_id = " + this.peupleId
                + " AND divinite_id = " + this.diviniteId + ";";
        db.sqlExec(sql);
    }

    public static LinkedHashMap<String, M_Adorer> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<String, M_Adorer> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {

        LinkedHashMap<String, M_Adorer> lesAdorations = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_adorer WHERE " + clauseWhere 
                   + " ORDER BY peuple_id, divinite_id;";

        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            int peupleId = res.getInt("peuple_id");
            int diviniteId = res.getInt("divinite_id");
            String commentaire = res.getString("commentaire");

            M_Adorer uneAdoration = new M_Adorer(db, peupleId, diviniteId, commentaire);

            String cle = peupleId + "-" + diviniteId;
            lesAdorations.put(cle, uneAdoration);
        }

        res.close();
        return lesAdorations;
    }

    @Override
    public String toString() {
        return "M_Adorer{peupleId=" + peupleId + ", diviniteId=" + diviniteId 
             + ", commentaire=" + commentaire + '}';
    }
}