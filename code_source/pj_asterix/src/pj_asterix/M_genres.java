package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_genres {

    private Db_mariadb db;
    private char code;
    private String nom;
    private String commentaire;

    // Constructeur simple (utilisé dans getRecords) - PRIVÉ
    private M_genres(Db_mariadb db, char code, String nom, String commentaire) {
        this.db = db;
        this.code = code;
        this.nom = nom;
        this.commentaire = commentaire;
    }

    // Méthode statique pour INSERT (remplace le 2ème constructeur)
    public static M_genres createAndInsert(Db_mariadb db, char code, 
                                          String nom, String commentaire) throws SQLException {
        
        String comSql = (commentaire == null) ? "NULL" : "'" + commentaire + "'";

        String sql = "INSERT INTO mcd_genres(code, nom, commentaire) VALUES ("
                + "'" + code + "', "
                + "'" + nom + "', "
                + comSql + ");";

        db.sqlExec(sql);
        
        return new M_genres(db, code, nom, commentaire);
    }

    // Constructeur SELECT par code
    public M_genres(Db_mariadb db, char code) throws SQLException {
        this.db = db;
        this.code = code;

        String sql = "SELECT * FROM mcd_genres WHERE code = '" + code + "';";

        ResultSet res = db.sqlSelect(sql);
        res.first();

        this.nom = res.getString("nom");
        this.commentaire = res.getString("commentaire");

        res.close();
    }

    // Getters
    public char getCode() { 
        return code; 
    }

    public String getNom() { 
        return nom; 
    }

    public String getCommentaire() { 
        return commentaire; 
    }

    // Setters
    public void setNom(String nom) { 
        this.nom = nom; 
    }

    public void setCommentaire(String commentaire) { 
        this.commentaire = commentaire; 
    }

    public void update() throws SQLException {
        String comSql = (commentaire == null) ? "NULL" : "'" + commentaire + "'";

        String sql = "UPDATE mcd_genres SET "
                + "nom = '" + nom + "', "
                + "commentaire = " + comSql + " "
                + "WHERE code = '" + code + "';";

        db.sqlExec(sql);
    }

    // Corrigé : utilise l'attribut de l'instance
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_genres WHERE code = '" + this.code + "';";
        db.sqlExec(sql);
    }

    public static LinkedHashMap<Character, M_genres> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<Character, M_genres> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {

        LinkedHashMap<Character, M_genres> lesGenres = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_genres WHERE " + clauseWhere + " ORDER BY nom;";

        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            char code = res.getString("code").charAt(0);
            String nom = res.getString("nom");
            String commentaire = res.getString("commentaire");

            M_genres unGenres = new M_genres(db, code, nom, commentaire);
            lesGenres.put(code, unGenres);
        }

        res.close();
        return lesGenres;
    }

    @Override
    public String toString() {
        return "M_Genre{code=" + code + ", nom='" + nom + "', commentaire='" + commentaire + "'}";
    }

    public static void main(String[] args) throws Exception {

        Db_mariadb mabase = new Db_mariadb(
            CL_Connection.url, 
            CL_Connection.login, 
            CL_Connection.password
        );

        // Test : création et insertion
        M_genres nouveauGenre = M_genres.createAndInsert(mabase, 'F', "Féminin", "Genre féminin");
        System.out.println("Genre créé : " + nouveauGenre);

        // Récupération de tous les genres
        LinkedHashMap<Character, M_genres> lesGenres = M_genres.getRecords(mabase);

        for (Character uneCle : lesGenres.keySet()) {
            M_genres unGenre = lesGenres.get(uneCle);
            System.out.println(uneCle + " -> " + unGenre);
        }

        // Test : modification
        nouveauGenre.setCommentaire("Genre féminin (modifié)");
        nouveauGenre.update();
        System.out.println("Après modification : " + nouveauGenre);

        // Test : suppression
        nouveauGenre.delete();
        System.out.println("Genre supprimé");
    }
}