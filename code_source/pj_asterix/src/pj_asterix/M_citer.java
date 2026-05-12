package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_citer {

    private Db_mariadb db;

    private int album_id;
    private int citation_id;
    private int num_page;
    private String commentaire;

    /* ===== CONSTRUCTEUR ===== */
    public M_citer(Db_mariadb db, int album_id, int citation_id, int num_page, String commentaire) {
        this.db = db;
        this.album_id = album_id;
        this.citation_id = citation_id;
        this.num_page = num_page;
        this.commentaire = commentaire;
    }

    /* ===== INSERT ===== */
    public void insert() throws SQLException {
        String sql = "INSERT INTO mcd_citer(album_id, citation_id, num_page, commentaire) VALUES ("
                + album_id + ", "
                + citation_id + ", "
                + num_page + ", "
                + (commentaire == null ? "NULL" : "'" + commentaire + "'")
                + ");";

        db.sqlExec(sql);
    }

    /* ===== UPDATE ===== */
    public void update() throws SQLException {
        String sql = "UPDATE mcd_citer SET "
                + "commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire + "'")
                + " WHERE album_id = " + album_id
                + " AND citation_id = " + citation_id
                + " AND num_page = " + num_page + ";";

        db.sqlExec(sql);
    }

    /* ===== DELETE ===== */
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_citer WHERE "
                + "album_id = " + album_id
                + " AND citation_id = " + citation_id
                + " AND num_page = " + num_page + ";";

        db.sqlExec(sql);
    }

    /* ===== GET RECORDS ===== */
    public static LinkedHashMap<String, M_citer> getRecords(Db_mariadb db) throws SQLException {

        LinkedHashMap<String, M_citer> citations = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_citer;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_citer c = new M_citer(
                    db,
                    res.getInt("album_id"),
                    res.getInt("citation_id"),
                    res.getInt("num_page"),
                    res.getString("commentaire")
            );

            String cle = c.album_id + "-" + c.citation_id + "-" + c.num_page;
            citations.put(cle, c);
        }

        res.close();
        return citations;
    }

    @Override
    public String toString() {
        return "Album " + album_id + " | Citation " + citation_id + " | Page " + num_page;
    }
}
