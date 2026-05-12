package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_Apparaitre {

    private Db_mariadb db;
    
    // Clé primaire composée (3 champs)
    private int albumId;
    private int personnageId;
    private short numPage;
    private String commentaire;

    // Constructeur privé (pour getRecords)
    private M_Apparaitre(Db_mariadb db, int albumId, int personnageId, 
                         short numPage, String commentaire) {
        this.db = db;
        this.albumId = albumId;
        this.personnageId = personnageId;
        this.numPage = numPage;
        this.commentaire = commentaire;
    }

    // INSERT
    public static M_Apparaitre createAndInsert(Db_mariadb db, int albumId, 
                                                int personnageId, short numPage, 
                                                String commentaire) throws SQLException {

        String sql = "INSERT INTO mcd_apparaitres(album_id, personnage_id, num_page, "
                + "commentaire, created_at) VALUES ("
                + albumId + ", "
                + personnageId + ", "
                + numPage + ", "
                + toSql(commentaire) + ", "
                + "NOW());";

        db.sqlExec(sql);

        return new M_Apparaitre(db, albumId, personnageId, numPage, commentaire);
    }

    // SELECT par clé primaire composée
    public M_Apparaitre(Db_mariadb db, int albumId, int personnageId, short numPage) throws SQLException {
        this.db = db;
        this.albumId = albumId;
        this.personnageId = personnageId;
        this.numPage = numPage;

        String sql = "SELECT * FROM mcd_apparaitres WHERE album_id = " + albumId 
                + " AND personnage_id = " + personnageId 
                + " AND num_page = " + numPage + ";";

        ResultSet res = db.sqlSelect(sql);
        res.first();

        this.commentaire = res.getString("commentaire");

        res.close();
    }

    // Getters
    public int getAlbumId() { return albumId; }
    public int getPersonnageId() { return personnageId; }
    public short getNumPage() { return numPage; }
    public String getCommentaire() { return commentaire; }

    // Setter
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public void update() throws SQLException {
        String sql = "UPDATE mcd_apparaitres SET "
                + "commentaire = " + toSql(commentaire) + ", "
                + "updated_at = NOW() "
                + "WHERE album_id = " + albumId 
                + " AND personnage_id = " + personnageId 
                + " AND num_page = " + numPage + ";";

        db.sqlExec(sql);
    }

    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_apparaitres WHERE album_id = " + albumId 
                + " AND personnage_id = " + personnageId 
                + " AND num_page = " + numPage + ";";
        db.sqlExec(sql);
    }

    private static String toSql(Object val) {
        return (val == null) ? "NULL" : "'" + val + "'";
    }

    public static LinkedHashMap<String, M_Apparaitre> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<String, M_Apparaitre> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {

        LinkedHashMap<String, M_Apparaitre> lesApparitions = new LinkedHashMap<>();
        String sql = "SELECT * FROM mcd_apparaitres WHERE " + clauseWhere 
                + " ORDER BY album_id, personnage_id, num_page;";

        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_Apparaitre a = new M_Apparaitre(db,
                res.getInt("album_id"),
                res.getInt("personnage_id"),
                res.getShort("num_page"),
                res.getString("commentaire")
            );
            // Clé composite : "albumId-personnageId-numPage"
            String cle = a.albumId + "-" + a.personnageId + "-" + a.numPage;
            lesApparitions.put(cle, a);
        }
        res.close();
        return lesApparitions;
    }

    @Override
    public String toString() {
        return "Apparition{album=" + albumId + ", perso=" + personnageId 
             + ", page=" + numPage + ", com=" + commentaire + "}";
    }
}