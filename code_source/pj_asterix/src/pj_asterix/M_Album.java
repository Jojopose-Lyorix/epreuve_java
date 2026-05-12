package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_Album {

    private Db_mariadb db;
    
    private int id;
    private int ordre;
    private String titre;
    private String dateSortie;    // Simplifié : String au lieu de LocalDate
    private Integer nbPages;
    private String couverture;
    private String resume;
    private String commentaire;

    // Constructeur privé (pour getRecords)
    private M_Album(Db_mariadb db, int id, int ordre, String titre, String dateSortie,
                    Integer nbPages, String couverture, String resume, String commentaire) {
        this.db = db;
        this.id = id;
        this.ordre = ordre;
        this.titre = titre;
        this.dateSortie = dateSortie;
        this.nbPages = nbPages;
        this.couverture = couverture;
        this.resume = resume;
        this.commentaire = commentaire;
    }

    // INSERT
    public static M_Album createAndInsert(Db_mariadb db, int ordre, String titre, 
                                          String dateSortie, Integer nbPages, 
                                          String couverture, String resume, 
                                          String commentaire) throws SQLException {

        String sql = "INSERT INTO mcd_albums(ordre, titre, date_sortie, nb_pages, "
                + "couverture, resume, commentaire, created_at) VALUES ("
                + ordre + ", "
                + "'" + titre + "', "
                + toSql(dateSortie) + ", "
                + toSql(nbPages) + ", "
                + toSql(couverture) + ", "
                + toSql(resume) + ", "
                + toSql(commentaire) + ", "
                + "NOW());";

        db.sqlExec(sql);

        ResultSet res = db.sqlSelect("SELECT LAST_INSERT_ID() AS lid;");
        res.first();
        int newId = res.getInt("lid");
        res.close();

        return new M_Album(db, newId, ordre, titre, dateSortie, nbPages, 
                          couverture, resume, commentaire);
    }

    // SELECT par ID
    public M_Album(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_albums WHERE id = " + id + ";");
        res.first();

        this.ordre = res.getInt("ordre");
        this.titre = res.getString("titre");
        this.dateSortie = res.getString("date_sortie");
        this.nbPages = (Integer) res.getObject("nb_pages");
        this.couverture = res.getString("couverture");
        this.resume = res.getString("resume");
        this.commentaire = res.getString("commentaire");

        res.close();
    }

    // Getters
    public int getId() { return id; }
    public int getOrdre() { return ordre; }
    public String getTitre() { return titre; }
    public String getDateSortie() { return dateSortie; }
    public Integer getNbPages() { return nbPages; }
    public String getCouverture() { return couverture; }
    public String getResume() { return resume; }
    public String getCommentaire() { return commentaire; }

    // Setters
    public void setOrdre(int ordre) { this.ordre = ordre; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDateSortie(String dateSortie) { this.dateSortie = dateSortie; }
    public void setNbPages(Integer nbPages) { this.nbPages = nbPages; }
    public void setCouverture(String couverture) { this.couverture = couverture; }
    public void setResume(String resume) { this.resume = resume; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public void update() throws SQLException {
        String sql = "UPDATE mcd_albums SET "
                + "ordre = " + ordre + ", "
                + "titre = '" + titre + "', "
                + "date_sortie = " + toSql(dateSortie) + ", "
                + "nb_pages = " + toSql(nbPages) + ", "
                + "couverture = " + toSql(couverture) + ", "
                + "resume = " + toSql(resume) + ", "
                + "commentaire = " + toSql(commentaire) + ", "
                + "updated_at = NOW() "
                + "WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    public void delete() throws SQLException {
        db.sqlExec("DELETE FROM mcd_albums WHERE id = " + this.id + ";");
    }

    // Utilitaire pour gérer les NULL en SQL
    private static String toSql(Object val) {
        return (val == null) ? "NULL" : "'" + val + "'";
    }

    public static LinkedHashMap<Integer, M_Album> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<Integer, M_Album> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {

        LinkedHashMap<Integer, M_Album> lesAlbums = new LinkedHashMap<>();
        ResultSet res = db.sqlSelect("SELECT * FROM mcd_albums WHERE " + clauseWhere + " ORDER BY ordre;");

        while (res.next()) {
            M_Album a = new M_Album(db,
                res.getInt("id"),
                res.getInt("ordre"),
                res.getString("titre"),
                res.getString("date_sortie"),
                (Integer) res.getObject("nb_pages"),
                res.getString("couverture"),
                res.getString("resume"),
                res.getString("commentaire")
            );
            lesAlbums.put(a.getId(), a);
        }
        res.close();
        return lesAlbums;
    }

    @Override
    public String toString() {
        return ordre + " - " + titre + " (" + dateSortie + ")";
    }
}