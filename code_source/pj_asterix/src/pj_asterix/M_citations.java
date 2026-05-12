package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_citations {

    private Db_mariadb db;

    private int id;
    private String texte;
    private String texte_fr;
    private String commentaire;

    /* ===== CONSTRUCTEURS ===== */

    // Constructeur lecture (SELECT)
    public M_citations(Db_mariadb db, int id, String texte, String texte_fr, String commentaire) {
        this.db = db;
        this.id = id;
        this.texte = texte;
        this.texte_fr = texte_fr;
        this.commentaire = commentaire;
    }

    // Constructeur INSERT
    public M_citations(Db_mariadb db, String texte, String texte_fr, String commentaire) throws SQLException {
        this.db = db;
        this.texte = texte;
        this.texte_fr = texte_fr;
        this.commentaire = commentaire;

        String sql = "INSERT INTO mcd_citations(texte, texte_fr, commentaire) VALUES ("
                + "'" + texte + "', "
                + (texte_fr == null ? "NULL" : "'" + texte_fr + "'") + ", "
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

    public String getTexte() {
        return texte;
    }

    public String getTexteFr() {
        return texte_fr;
    }

    public String getCommentaire() {
        return commentaire;
    }

    /* ===== UPDATE ===== */

    public void update() throws SQLException {
        String sql = "UPDATE mcd_citations SET "
                + "texte = '" + texte + "', "
                + "texte_fr = " + (texte_fr == null ? "NULL" : "'" + texte_fr + "'") + ", "
                + "commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire + "'")
                + " WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    /* ===== DELETE ===== */

    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_citations WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* ===== GET RECORDS ===== */

    public static LinkedHashMap<Integer, M_citations> getRecords(Db_mariadb db) throws SQLException {

        LinkedHashMap<Integer, M_citations> citations = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_citations;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_citations c = new M_citations(
                    db,
                    res.getInt("id"),
                    res.getString("texte"),
                    res.getString("texte_fr"),
                    res.getString("commentaire")
            );
            citations.put(c.id, c);
        }

        res.close();
        return citations;
    }

    /* ===== AFFICHAGE ===== */

    @Override
    public String toString() {
        return texte;
    }
}
