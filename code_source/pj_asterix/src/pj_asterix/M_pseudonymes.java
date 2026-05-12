package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_pseudonymes {

    private Db_mariadb db;

    private int id;
    private String nom;
    private String commentaire;
    private int personnage_id;

    /* ===== CONSTRUCTEUR LECTURE ===== */
    public M_pseudonymes(Db_mariadb db, int id, String nom, String commentaire, int personnage_id) {
        this.db = db;
        this.id = id;
        this.nom = nom;
        this.commentaire = commentaire;
        this.personnage_id = personnage_id;
    }

    /* ===== CONSTRUCTEUR INSERT ===== */
    public M_pseudonymes(Db_mariadb db, String nom, String commentaire, int personnage_id) throws SQLException {
        this.db = db;
        this.nom = nom;
        this.commentaire = commentaire;
        this.personnage_id = personnage_id;

        String sql = "INSERT INTO mcd_pseudonymes(nom, commentaire, personnage_id) VALUES ("
                + "'" + nom + "', "
                + (commentaire == null ? "NULL" : "'" + commentaire + "'") + ", "
                + personnage_id
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

    public int getPersonnageId() {
        return personnage_id;
    }

    /* ===== UPDATE ===== */
    public void update() throws SQLException {
        String sql = "UPDATE mcd_pseudonymes SET "
                + "nom = '" + nom + "', "
                + "commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire + "'")
                + " WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    /* ===== DELETE ===== */
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_pseudonymes WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* ===== GET RECORDS ===== */
    public static LinkedHashMap<Integer, M_pseudonymes> getRecords(Db_mariadb db) throws SQLException {

        LinkedHashMap<Integer, M_pseudonymes> pseudos = new LinkedHashMap<>();

        String sql = "SELECT * FROM pseudonymes;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_pseudonymes p = new M_pseudonymes(
                    db,
                    res.getInt("id"),
                    res.getString("nom"),
                    res.getString("commentaire"),
                    res.getInt("personnage_id")
            );
            pseudos.put(p.id, p);
        }

        res.close();
        return pseudos;
    }

    /* ===== AFFICHAGE ===== */
    @Override
    public String toString() {
        return nom;
    }
}
