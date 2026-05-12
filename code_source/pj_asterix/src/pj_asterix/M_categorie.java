package pj_asterix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class M_categorie {

    private Db_mariadb db;

    private int id;
    private String nom;
    private String commentaire;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    /* =====================
       CONSTRUCTEURS
       ===================== */

    // Constructeur simple (utilisé dans getRecords)
    public M_categorie(Db_mariadb db, int id, String nom, String commentaire,
                       LocalDateTime created_at, LocalDateTime updated_at) {
        this.db = db;
        this.id = id;
        this.nom = nom;
        this.commentaire = commentaire;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Constructeur INSERT
    public M_categorie(Db_mariadb db, String nom, String commentaire) throws SQLException {
        this.db = db;
        this.nom = nom;
        this.commentaire = commentaire;

        String nomSql = esc(nom);
        String comSql = (commentaire == null) ? "NULL" : "'" + esc(commentaire) + "'";

        String sql = "INSERT INTO mcd_categories(nom, commentaire, created_at, updated_at) VALUES ("
                + "'" + nomSql + "', "
                + comSql + ", "
                + "NOW(), NOW()"
                + ");";

        db.sqlExec(sql);

        ResultSet res = db.sqlLastId();
        res.first();
        this.id = res.getInt("id");
        res.close();

        // Optionnel : recharger pour récupérer created_at/updated_at
        M_categorie reloaded = M_categorie.getById(db, this.id);
        if (reloaded != null) {
            this.created_at = reloaded.created_at;
            this.updated_at = reloaded.updated_at;
        }
    }

    // Constructeur SELECT par id
    public M_categorie(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        loadById(id);
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

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public LocalDateTime getUpdatedAt() {
        return updated_at;
    }

    /* =====================
       CRUD
       ===================== */

    public void update() throws SQLException {
        String nomSql = esc(nom);
        String comSql = (commentaire == null) ? "NULL" : "'" + esc(commentaire) + "'";

        String sql = "UPDATE mcd_categories SET "
                + "nom = '" + nomSql + "', "
                + "commentaire = " + comSql + ", "
                + "updated_at = NOW() "
                + "WHERE id = " + id + ";";

        db.sqlExec(sql);

        // Optionnel : recharger updated_at depuis la BDD
        M_categorie reloaded = M_categorie.getById(db, this.id);
        if (reloaded != null) {
            this.updated_at = reloaded.updated_at;
        }
    }

    public void delete() throws SQLException {
        delete(this.db, this.id);
    }

    public static void delete(Db_mariadb db, int id) throws SQLException {
        String sql = "DELETE FROM mcd_categories WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* =====================
       SELECTS / GET RECORDS
       ===================== */

    public static M_categorie getById(Db_mariadb db, int id) throws SQLException {
        String sql = "SELECT * FROM mcd_categories WHERE id = " + id + ";";
        ResultSet res = db.sqlSelect(sql);

        if (!res.next()) {
            res.close();
            return null;
        }

        M_categorie c = mapRow(db, res);
        res.close();
        return c;
    }

    public static LinkedHashMap<Integer, M_categorie> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<Integer, M_categorie> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {
        LinkedHashMap<Integer, M_categorie> categories = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_categories WHERE " + clauseWhere + " ORDER BY nom;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            M_categorie c = mapRow(db, res);
            categories.put(c.id, c);
        }

        res.close();
        return categories;
    }

    /* =====================
       OUTILS INTERNES
       ===================== */

    private void loadById(int id) throws SQLException {
        String sql = "SELECT * FROM mcd_categories WHERE id = " + id + ";";
        ResultSet res = db.sqlSelect(sql);

        if (!res.next()) {
            res.close();
            throw new SQLException("mcd_categories introuvable pour id=" + id);
        }

        M_categorie c = mapRow(this.db, res);

        this.id = c.id;
        this.nom = c.nom;
        this.commentaire = c.commentaire;
        this.created_at = c.created_at;
        this.updated_at = c.updated_at;

        res.close();
    }

    private static M_categorie mapRow(Db_mariadb db, ResultSet res) throws SQLException {
        int id = res.getInt("id");
        String nom = res.getString("nom");
        String commentaire = res.getString("commentaire");

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        if (res.getTimestamp("created_at") != null) {
            createdAt = res.getTimestamp("created_at").toLocalDateTime();
        }
        if (res.getTimestamp("updated_at") != null) {
            updatedAt = res.getTimestamp("updated_at").toLocalDateTime();
        }

        return new M_categorie(db, id, nom, commentaire, createdAt, updatedAt);
    }

    private static String esc(String s) {
        return (s == null) ? null : s.replace("'", "''");
    }

    /* =====================
       AFFICHAGE
       ===================== */

    @Override
    public String toString() {
        // pratique pour JComboBox
        return nom;
    }

    /* =====================
       MAIN DE TEST
       ===================== */

    public static void main(String[] args) throws Exception {

        Db_mariadb mabase =
                new Db_mariadb(CL_Connection.url, CL_Connection.login, CL_Connection.password);

        // 1) Lire tout
        LinkedHashMap<Integer, M_categorie> cats = M_categorie.getRecords(mabase);
        for (int id : cats.keySet()) {
            System.out.println(id + " -> " + cats.get(id).getNom());
        }

        // 2) Insert
        M_categorie c = new M_categorie(mabase, "Boissons", "Pour les banquets");
        System.out.println("INSERT id=" + c.getId() + " nom=" + c.getNom());

        // 3) Update
        c.setCommentaire("Pour les banquets (modifié)");
        c.update();
        System.out.println("UPDATED commentaire=" + M_categorie.getById(mabase, c.getId()).getCommentaire());

        // 4) Delete
        c.delete();
        System.out.println("DELETED id=" + c.getId());
    }
}
