package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_divinites {

    private Db_mariadb db;

    private int id;
    private String nom;
    private String contexte;
    private String commentaire;

    /* ===== CONSTRUCTEUR LECTURE ===== */
    public M_divinites(Db_mariadb db, int id, String nom, String contexte, String commentaire) {
        this.db = db;
        this.id = id;
        this.nom = nom;
        this.contexte = contexte;
        this.commentaire = commentaire;
    }

    /* ===== CONSTRUCTEUR PAR ID ===== */
    public M_divinites(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        String sql = "SELECT * FROM mcd_divinites WHERE id = " + id + ";";
        ResultSet res = db.sqlSelect(sql);

        if (res.next()) {
            this.nom = res.getString("nom");
            this.contexte = res.getString("contexte");
            this.commentaire = res.getString("commentaire");
        }
        res.close();
    }

    /* ===== CONSTRUCTEUR INSERT ===== */
    public M_divinites(Db_mariadb db, String nom, String contexte, String commentaire) throws SQLException {
        this.db = db;
        this.nom = nom;
        this.contexte = contexte;
        this.commentaire = commentaire;

        String sql = "INSERT INTO mcd_divinites(nom, contexte, commentaire, created_at, updated_at) VALUES ("
                + "'" + nom.replace("'", "''") + "', "
                + (contexte == null ? "NULL" : "'" + contexte.replace("'", "''") + "'") + ", "
                + (commentaire == null ? "NULL" : "'" + commentaire.replace("'", "''") + "'") + ", "
                + "NOW(), "
                + "NULL"
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

    public String getContexte() {
        return contexte;
    }

    public String getCommentaire() {
        return commentaire;
    }

    /* ===== SETTERS ===== */
    public void setDb(Db_mariadb db) {
        this.db = db;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setContexte(String contexte) {
        this.contexte = contexte;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    /* ===== UPDATE ===== */
    public void update() throws SQLException {
        String sql = "UPDATE mcd_divinites SET "
                + "nom = '" + nom.replace("'", "''") + "', "
                + "contexte = " + (contexte == null ? "NULL" : "'" + contexte.replace("'", "''") + "'") + ", "
                + "commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire.replace("'", "''") + "'") + ", "
                + "updated_at = NOW() "
                + "WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    /* ===== DELETE ===== */
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_divinites WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* ===== GET RECORDS ===== */
    public static LinkedHashMap<Integer, M_divinites> getRecords(Db_mariadb db) throws SQLException {

        LinkedHashMap<Integer, M_divinites> divinites = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_divinites;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_divinites d = new M_divinites(
                    db,
                    res.getInt("id"),
                    res.getString("nom"),
                    res.getString("contexte"),
                    res.getString("commentaire")
            );
            divinites.put(d.id, d);
        }

        res.close();
        return divinites;
    }

    /* ===== AFFICHAGE ===== */
    @Override
    public String toString() {
        return nom;
    }
}