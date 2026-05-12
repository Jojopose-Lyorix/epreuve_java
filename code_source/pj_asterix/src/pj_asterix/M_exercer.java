package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_exercer {

    private Db_mariadb db;

    private int personnage_id;
    private int metier_id;
    private int ordre;
    private String commentaire;

    /* ===== CONSTRUCTEUR ===== */
    public M_exercer(Db_mariadb db, int personnage_id, int metier_id, int ordre, String commentaire) {
        this.db = db;
        this.personnage_id = personnage_id;
        this.metier_id = metier_id;
        this.ordre = ordre;
        this.commentaire = commentaire;
    }

    /* ===== INSERT ===== */
    public void insert() throws SQLException {
        String sql = "INSERT INTO mcd_exercer(personnage_id, metier_id, ordre, commentaire) VALUES ("
                + personnage_id + ", "
                + metier_id + ", "
                + ordre + ", "
                + (commentaire == null ? "NULL" : "'" + commentaire + "'")
                + ");";

        db.sqlExec(sql);
    }

    /* ===== GETTERS ===== */
    public int getPersonnageId() {
        return personnage_id;
    }

    public int getMetierId() {
        return metier_id;
    }

    public int getOrdre() {
        return ordre;
    }

    public String getCommentaire() {
        return commentaire;
    }

    /* ===== UPDATE ===== */
    public void update() throws SQLException {
        String sql = "UPDATE mcd_exercer SET "
                + "ordre = " + ordre + ", "
                + "commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire + "'")
                + " WHERE personnage_id = " + personnage_id
                + " AND metier_id = " + metier_id + ";";

        db.sqlExec(sql);
    }

    /* ===== DELETE ===== */
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_exercer WHERE "
                + "personnage_id = " + personnage_id
                + " AND metier_id = " + metier_id + ";";

        db.sqlExec(sql);
    }

    /* ===== GET RECORDS ===== */
    public static LinkedHashMap<String, M_exercer> getRecords(Db_mariadb db) throws SQLException {

        LinkedHashMap<String, M_exercer> exercers = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_exercer;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_exercer e = new M_exercer(
                    db,
                    res.getInt("personnage_id"),
                    res.getInt("metier_id"),
                    res.getInt("ordre"),
                    res.getString("commentaire")
            );

            // clé simple personnage-metier
            String cle = e.personnage_id + "-" + e.metier_id;
            exercers.put(cle, e);
        }

        res.close();
        return exercers;
    }

    /* ===== AFFICHAGE ===== */
    @Override
    public String toString() {
        return "Personnage " + personnage_id + " | Métier " + metier_id + " | Ordre " + ordre;
    }
}
