package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_personnages {

    private Db_mariadb db;

    private int id;
    private String nom;
    private String commentaire;
    private String genre_code;
    private Integer peuple_id;

    /* ===== CONSTRUCTEUR LECTURE (avec tous les champs) ===== */
    public M_personnages(Db_mariadb db, int id, String nom,
                         String commentaire, String genre_code, Integer peuple_id) {
        this.db = db;
        this.id = id;
        this.nom = nom;
        this.commentaire = commentaire;
        this.genre_code = genre_code;
        this.peuple_id = peuple_id;
    }

    /* ===== CONSTRUCTEUR PAR ID ===== */
    public M_personnages(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        String sql = "SELECT * FROM mcd_personnages WHERE id = " + id + ";";
        ResultSet res = db.sqlSelect(sql);

        if (res.next()) {
            this.nom = res.getString("nom");
            this.commentaire = res.getString("commentaire");
            this.genre_code = res.getString("genre_code");
            
            Object peupleObj = res.getObject("peuple_id");
            if (peupleObj != null) {
                this.peuple_id = ((Number) peupleObj).intValue();
            } else {
                this.peuple_id = null;
            }
        }
        res.close();
    }

    /* ===== CONSTRUCTEUR INSERT ===== */
    public M_personnages(Db_mariadb db, String nom,
                         String commentaire, String genre_code, Integer peuple_id) throws SQLException {
        this.db = db;
        this.nom = nom;
        this.commentaire = commentaire;
        this.genre_code = genre_code;
        this.peuple_id = peuple_id;

        String sql = "INSERT INTO mcd_personnages (nom, commentaire, genre_code, peuple_id, created_at, updated_at) VALUES ("
            + "'" + nom.replace("'", "''") + "', "
            + (commentaire == null ? "NULL" : "'" + commentaire.replace("'", "''") + "'") + ", "
            + "'" + genre_code.replace("'", "''") + "', "
            + (peuple_id == null ? "NULL" : peuple_id) + ", "
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

    public String getCommentaire() {
        return commentaire;
    }

    public String getGenreCode() {
        return genre_code;
    }

    public Integer getPeupleId() {
        return peuple_id;
    }

    /* ===== SETTERS ===== */
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setGenreCode(String genre_code) {
        this.genre_code = genre_code;
    }

    public void setPeupleId(Integer peuple_id) {
        this.peuple_id = peuple_id;
    }

    /* ===== UPDATE ===== */
    public void update() throws SQLException {
        String sql = "UPDATE mcd_personnages SET "
            + "nom = '" + nom.replace("'", "''") + "', "
            + "commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire.replace("'", "''") + "'") + ", "
            + "genre_code = '" + genre_code.replace("'", "''") + "', "
            + "peuple_id = " + (peuple_id == null ? "NULL" : peuple_id) + ", "
            + "updated_at = NOW() "
            + "WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    /* ===== DELETE ===== */
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_personnages WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* ===== GET RECORDS ===== */
    public static LinkedHashMap<Integer, M_personnages> getRecords(Db_mariadb db) throws SQLException {
        LinkedHashMap<Integer, M_personnages> personnages = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_personnages;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            Integer peupleId = null;
            Object peupleObj = res.getObject("peuple_id");
            if (peupleObj != null) {
                peupleId = ((Number) peupleObj).intValue();
            }

            M_personnages p = new M_personnages(
                db,
                res.getInt("id"),
                res.getString("nom"),
                res.getString("commentaire"),
                res.getString("genre_code"),
                peupleId
            );
            personnages.put(p.getId(), p);
        }

        res.close();
        return personnages;
    }

    /* ===== AFFICHAGE ===== */
    @Override
    public String toString() {
        return nom;
    }
}