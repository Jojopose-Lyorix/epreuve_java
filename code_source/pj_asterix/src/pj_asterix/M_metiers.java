package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_metiers {

    private Db_mariadb db;

    private int id;
    private String nom;
    private String commentaire;

    /* ===== CONSTRUCTEUR LECTURE ===== */
    public M_metiers(Db_mariadb db, int id, String nom, String commentaire) {
        this.db = db;
        this.id = id;
        this.nom = nom;
        this.commentaire = commentaire;
    }

    /* ===== CONSTRUCTEUR INSERT ===== */
    public M_metiers(Db_mariadb db, String nom, String commentaire) throws SQLException {
        this.db = db;
        this.nom = nom;
        this.commentaire = commentaire;

        String sql = "INSERT INTO mcd_metiers(nom, commentaire) VALUES ("
                + "'" + nom + "', "
                + (commentaire == null ? "NULL" : "'" + commentaire + "'")
                + ");";

        db.sqlExec(sql);

        ResultSet res = db.sqlLastId();
        res.first();
        this.id = res.getInt("id");
        res.close();
    }

    /* ===== GETTERS ===== */
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getCommentaire() {
        return commentaire;
    }

    /* ===== UPDATE ===== */
    public void update() throws SQLException {
        String sql = "UPDATE mcd_metiers SET "
                + "nom = '" + nom + "', "
                + "commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire + "'")
                + " WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    /* ===== DELETE ===== */
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_metiers WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* ===== GET RECORDS ===== */
    public static LinkedHashMap<Integer, M_metiers> getRecords(Db_mariadb db) throws SQLException {

        LinkedHashMap<Integer, M_metiers> metiers = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_metiers;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_metiers m = new M_metiers(
                    db,
                    res.getInt("id"),
                    res.getString("nom"),
                    res.getString("commentaire")
            );
            metiers.put(m.id, m);
        }

        res.close();
        return metiers;
    }

    /* ===== AFFICHAGE ===== */
    @Override
    public String toString() {
        return nom;
    }
}
