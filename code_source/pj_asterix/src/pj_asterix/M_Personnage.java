package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_Personnage {

    private Db_mariadb db;
    private int id;
    private String nom;
    private String commentaire;
    private char genreCode;
    private Integer peupleId; // nullable

    /* =====================
       CONSTRUCTEURS
       ===================== */

    // Constructeur simple (utilisé dans getRecords)
    public M_Personnage(Db_mariadb db, int id, String nom,
                        String commentaire, char genreCode, Integer peupleId) {
        this.db = db;
        this.id = id;
        this.nom = nom;
        this.commentaire = commentaire;
        this.genreCode = genreCode;
        this.peupleId = peupleId;
    }

    // Constructeur INSERT
    public M_Personnage(Db_mariadb db, String nom,
                        String commentaire, char genreCode, Integer peupleId) throws SQLException {
        this.db = db;
        this.nom = nom;
        this.commentaire = commentaire;
        this.genreCode = genreCode;
        this.peupleId = peupleId;

        String comSql   = (commentaire == null) ? "NULL" : "'" + commentaire + "'";
        String peupleSql = (peupleId == null) ? "NULL" : peupleId.toString();

        String sql;
        sql = "INSERT INTO mcd_personnages(nom, image, commentaire, genre_code, peuple_id) VALUES ("
                + "'" + nom + "', "
                + comSql + ", "
                + "'" + genreCode + "', "
                + peupleSql + ");";

        db.sqlExec(sql);

        ResultSet res = db.sqlLastId();
        res.first();
        this.id = res.getInt("id");
        res.close();
    }

    // Constructeur SELECT par id
    public M_Personnage(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        String sql;
        sql = "SELECT * FROM mcd_personnages WHERE id = " + id + ";";

        ResultSet res = db.sqlSelect(sql);
        res.first();

        this.nom = res.getString("nom");
        this.commentaire = res.getString("commentaire");
        this.genreCode = res.getString("genre_code").charAt(0);
        this.peupleId = (res.getObject("peuple_id") == null)
                ? null
                : res.getInt("peuple_id");

        res.close();
    }

    /* =====================
       GETTERS / SETTERS
       ===================== */

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public char getGenreCode() {
        return genreCode;
    }

    public void setGenreCode(char genreCode) {
        this.genreCode = genreCode;
    }

    public Integer getPeupleId() {
        return peupleId;
    }

    public void setPeupleId(Integer peupleId) {
        this.peupleId = peupleId;
    }

    /* =====================
       CRUD
       ===================== */

    public void update() throws SQLException {

        String comSql   = (commentaire == null) ? "NULL" : "'" + commentaire + "'";
        String peupleSql = (peupleId == null) ? "NULL" : peupleId.toString();

        String sql;
        sql = "UPDATE mcd_personnages SET "
                + "nom = '" + nom + "', "
                + "commentaire = " + comSql + ", "
                + "genre_code = '" + genreCode + "', "
                + "peuple_id = " + peupleSql + " "
                + "WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    public void delete(int id) throws SQLException {
        String sql;
        sql = "DELETE FROM mcd_personnages WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* =====================
       GET RECORDS
       ===================== */

    public static LinkedHashMap<Integer, M_Personnage> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<Integer, M_Personnage> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {

        LinkedHashMap<Integer, M_Personnage> lesPersonnages = new LinkedHashMap<>();

        String sql;
        sql = "SELECT * FROM mcd_personnages WHERE " + clauseWhere + " ORDER BY nom;";

        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            int id = res.getInt("id");
            String nom = res.getString("nom");
            String commentaire = res.getString("commentaire");
            char genreCode = res.getString("genre_code").charAt(0);
            Integer peupleId = (res.getObject("peuple_id") == null)
                    ? null
                    : res.getInt("peuple_id");

            M_Personnage unPerso =
                    new M_Personnage(db, id, nom, commentaire, genreCode, peupleId);

            lesPersonnages.put(id, unPerso);
        }

        res.close();
        return lesPersonnages;
    }

    /* =====================
       AFFICHAGE
       ===================== */

    @Override
    public String toString() {
        return nom;
    }

    /* =====================
       MAIN DE TEST
       ===================== */

    public static void main(String[] args) throws Exception {

        Db_mariadb mabase =
                new Db_mariadb(CL_Connection.url, CL_Connection.login, CL_Connection.password);

        LinkedHashMap<Integer, M_Personnage> lesPersos =
                M_Personnage.getRecords(mabase);

        for (Integer uneCle : lesPersos.keySet()) {
            M_Personnage p = lesPersos.get(uneCle);
            System.out.println(p);
        }
    }
}
