package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_lieux {

    private Db_mariadb db;

    private int id;
    private String nom_fr;
    private String nom;
    private String commentaire;
    private Long categorie_id;
    private Long peuple_id;

    /* ===== CONSTRUCTEUR LECTURE ===== */
    public M_lieux(Db_mariadb db, int id, String nom_fr, String nom,
                   String commentaire, Long categorie_id, Long peuple_id) {
        this.db = db;
        this.id = id;
        this.nom_fr = nom_fr;
        this.nom = nom;
        this.commentaire = commentaire;
        this.categorie_id = categorie_id;
        this.peuple_id = peuple_id;
    }

    /* ===== CONSTRUCTEUR INSERT ===== */
    public M_lieux(Db_mariadb db, String nom_fr, String nom,
                   String commentaire, Long categorie_id, Long peuple_id) throws SQLException {
        this.db = db;
        this.nom_fr = nom_fr;
        this.nom = nom;
        this.commentaire = commentaire;
        this.categorie_id = categorie_id;
        this.peuple_id = peuple_id;

        String sql = "INSERT INTO mcd_lieux(nom_fr, nom, commentaire, categorie_id, peuple_id) VALUES ("
                + "'" + nom_fr + "', "
                + (nom == null ? "NULL" : "'" + nom + "'") + ", "
                + (commentaire == null ? "NULL" : "'" + commentaire + "'") + ", "
                + (categorie_id == null ? "NULL" : categorie_id) + ", "
                + (peuple_id == null ? "NULL" : peuple_id)
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

    public String getNomFr() {
        return nom_fr;
    }

    public String getNom() {
        return nom;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Long getCategorieId() {
        return categorie_id;
    }

    public Long getPeupleId() {
        return peuple_id;
    }

    /* ===== UPDATE ===== */
    public void update() throws SQLException {
        String sql = "UPDATE mcd_lieux SET "
                + "nom_fr = '" + nom_fr + "', "
                + "nom = " + (nom == null ? "NULL" : "'" + nom + "'") + ", "
                + "commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire + "'") + ", "
                + "categorie_id = " + (categorie_id == null ? "NULL" : categorie_id) + ", "
                + "peuple_id = " + (peuple_id == null ? "NULL" : peuple_id)
                + " WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    /* ===== DELETE ===== */
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_lieux WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* ===== GET RECORDS ===== */
    public static LinkedHashMap<Integer, M_lieux> getRecords(Db_mariadb db) throws SQLException {

        LinkedHashMap<Integer, M_lieux> lieux = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_lieux;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_lieux l = new M_lieux(
                    db,
                    res.getInt("id"),
                    res.getString("nom_fr"),
                    res.getString("nom"),
                    res.getString("commentaire"),
                    (Long) res.getObject("categorie_id"),
                    (Long) res.getObject("peuple_id")
            );
            lieux.put(l.id, l);
        }

        res.close();
        return lieux;
    }

    /* ===== AFFICHAGE ===== */
    @Override
    public String toString() {
        return nom_fr;
    }
}
