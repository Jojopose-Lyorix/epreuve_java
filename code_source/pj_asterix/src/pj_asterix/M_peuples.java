package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_peuples {

    private Db_mariadb db;

    private int id;
    private String nom;
    private String gentile_m_s;
    private String gentile_m_p;
    private String gentile_f_s;
    private String gentile_f_p;
    private String image;
    private String commentaire;

    /* ===== CONSTRUCTEUR LECTURE ===== */
    public M_peuples(Db_mariadb db, int id, String nom,
                     String gentile_m_s, String gentile_m_p,
                     String gentile_f_s, String gentile_f_p,
                     String image, String commentaire) {
        this.db = db;
        this.id = id;
        this.nom = nom;
        this.gentile_m_s = gentile_m_s;
        this.gentile_m_p = gentile_m_p;
        this.gentile_f_s = gentile_f_s;
        this.gentile_f_p = gentile_f_p;
        this.image = image;
        this.commentaire = commentaire;
    }

    /* ===== CONSTRUCTEUR INSERT ===== */
    public M_peuples(Db_mariadb db, String nom,
                     String gentile_m_s, String gentile_m_p,
                     String gentile_f_s, String gentile_f_p,
                     String image, String commentaire) throws SQLException {
        this.db = db;
        this.nom = nom;
        this.gentile_m_s = gentile_m_s;
        this.gentile_m_p = gentile_m_p;
        this.gentile_f_s = gentile_f_s;
        this.gentile_f_p = gentile_f_p;
        this.image = image;
        this.commentaire = commentaire;

        String sql = "INSERT INTO mcd_peuples(nom, gentile_m_s, gentile_m_p, gentile_f_s, gentile_f_p, image, commentaire) VALUES ("
                + "'" + nom + "', "
                + (gentile_m_s == null ? "NULL" : "'" + gentile_m_s + "'") + ", "
                + (gentile_m_p == null ? "NULL" : "'" + gentile_m_p + "'") + ", "
                + (gentile_f_s == null ? "NULL" : "'" + gentile_f_s + "'") + ", "
                + (gentile_f_p == null ? "NULL" : "'" + gentile_f_p + "'") + ", "
                + (image == null ? "NULL" : "'" + image + "'") + ", "
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

    public String getGentileMS() {
        return gentile_m_s;
    }

    public String getGentileMP() {
        return gentile_m_p;
    }

    public String getGentileFS() {
        return gentile_f_s;
    }

    public String getGentileFP() {
        return gentile_f_p;
    }

    public String getImage() {
        return image;
    }

    public String getCommentaire() {
        return commentaire;
    }

    /* ===== UPDATE ===== */
    public void update() throws SQLException {
        String sql = "UPDATE mcd_peuples SET "
                + "nom = '" + nom + "', "
                + "gentile_m_s = " + (gentile_m_s == null ? "NULL" : "'" + gentile_m_s + "'") + ", "
                + "gentile_m_p = " + (gentile_m_p == null ? "NULL" : "'" + gentile_m_p + "'") + ", "
                + "gentile_f_s = " + (gentile_f_s == null ? "NULL" : "'" + gentile_f_s + "'") + ", "
                + "gentile_f_p = " + (gentile_f_p == null ? "NULL" : "'" + gentile_f_p + "'") + ", "
                + "image = " + (image == null ? "NULL" : "'" + image + "'") + ", "
                + "commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire + "'")
                + " WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    /* ===== DELETE ===== */
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_peuples WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* ===== GET RECORDS ===== */
    public static LinkedHashMap<Integer, M_peuples> getRecords(Db_mariadb db) throws SQLException {

        LinkedHashMap<Integer, M_peuples> peuples = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_peuples;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_peuples p = new M_peuples(
                    db,
                    res.getInt("id"),
                    res.getString("nom"),
                    res.getString("gentile_m_s"),
                    res.getString("gentile_m_p"),
                    res.getString("gentile_f_s"),
                    res.getString("gentile_f_p"),
                    res.getString("image"),
                    res.getString("commentaire")
            );
            peuples.put(p.id, p);
        }

        res.close();
        return peuples;
    }

    /* ===== AFFICHAGE ===== */
    @Override
    public String toString() {
        return nom;
    }
}
