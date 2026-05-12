package pj_asterix;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class M_users {

    private Db_mariadb db;

    private int id;
    private String name;
    private String email;
    private String password;
    private String commentaire;
    private int roleId;

    /* =====================
       CONSTRUCTEURS
       ===================== */

    // Constructeur simple (utilisé dans getRecords)
    public M_users(Db_mariadb db, int id, String name, String email, String password, String commentaire, int roleId) {
        this.db = db;
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.commentaire = commentaire;
        this.roleId = roleId;
    }

    // Constructeur INSERT
    public M_users(Db_mariadb db, String name, String email, String password, String commentaire, int roleId) throws SQLException {
        this.db = db;
        this.name = name;
        this.email = email;
        this.password = hashPassword(password);  // ← HASHÉ LE PASSWORD
        this.commentaire = commentaire;
        this.roleId = roleId;

        String comSql = (commentaire == null)
                ? "NULL"
                : "'" + commentaire + "'";

        String sql;
        sql = "INSERT INTO mcd_users(name, email, password, commentaire, role_id) VALUES ("
                + "'" + name + "', "
                + "'" + email + "', "
                + "'" + this.password + "', "  // ← UTILISER LE PASSWORD HASHÉ
                + comSql + ", "
                + roleId + ");";

        db.sqlExec(sql);

        ResultSet res = db.sqlLastId();
        res.first();
        this.id = res.getInt("id");
        res.close();
    }

    // Constructeur SELECT par id
    public M_users(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        String sql;
        sql = "SELECT * FROM mcd_users WHERE id = " + id + ";";

        ResultSet res = db.sqlSelect(sql);
        res.first();

        this.name = res.getString("name");
        this.email = res.getString("email");
        this.password = res.getString("password");
        this.commentaire = res.getString("commentaire");
        this.roleId = res.getInt("role_id");

        res.close();
    }

    /* =====================
       GETTERS / SETTERS
       ===================== */

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    /* =====================
       CRUD
       ===================== */

    public void update() throws SQLException {

        String comSql = (commentaire == null)
                ? "NULL"
                : "'" + commentaire + "'";

        String sql = "UPDATE mcd_users SET "
                + "name = '" + name + "', "
                + "email = '" + email + "', "
                + "commentaire = " + comSql + ", "
                + "role_id = " + roleId;

        // ✅ Ajouter password uniquement si présent et différent
        if (password != null && !password.isEmpty()) {
            // Vérifier si c'est déjà un hash BCrypt
            if (!password.startsWith("$2a$") && !password.startsWith("$2b$") && !password.startsWith("$2x$") && !password.startsWith("$2y$")) {
                // C'est un mot de passe en clair, il faut le hasher
                password = hashPassword(password);
            }
            sql += ", password = '" + password + "'";
        }

        sql += " WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    public void delete(int id) throws SQLException {
        String sql;
        sql = "DELETE FROM mcd_users WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    /* =====================
       GET RECORDS
       ===================== */

    public static LinkedHashMap<Integer, M_users> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<Integer, M_users> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {

        LinkedHashMap<Integer, M_users> lesUtilisateurs = new LinkedHashMap<>();

        String sql;
        sql = "SELECT * FROM mcd_users WHERE " + clauseWhere + " ORDER BY name;";

        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            int id = res.getInt("id");
            String name = res.getString("name");
            String email = res.getString("email");
            String password = res.getString("password");
            String commentaire = res.getString("commentaire");
            int roleId = res.getInt("role_id");

            M_users unUtilisateur =
                    new M_users(db, id, name, email, password, commentaire, roleId);

            lesUtilisateurs.put(id, unUtilisateur);
        }

        res.close();
        return lesUtilisateurs;
    }

    /* =====================
       CONNEXION
       ===================== */

    public static M_users connexion_log(Db_mariadb db, String name, String motPasse) throws SQLException {

        String sql;
        M_users unUtil = null;
        sql = "SELECT * FROM mcd_users WHERE name = '" + name + "' ";

        ResultSet res = db.sqlSelect(sql);

        if (res.first()) {
            String mpHash = res.getString("password");

            if (BCrypt.verifyer().verify(motPasse.toCharArray(), mpHash).verified) {
                int id = res.getInt("id");
                unUtil = new M_users(db, id);
            }
        }
        res.close();
        return unUtil;
    }

    /* =====================
       MÉTHODES UTILITAIRES
       ===================== */

    // ← AJOUT : Méthode pour hasher un mot de passe avec BCrypt
    private static String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    /* =====================
       AFFICHAGE
       ===================== */

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }

    /* =====================
       MAIN DE TEST
       ===================== */

    public static void main(String[] args) throws Exception {

        Db_mariadb mabase =
                new Db_mariadb(CL_Connection.url, CL_Connection.login, CL_Connection.password);

        LinkedHashMap<Integer, M_users> lesUtilisateurs =
                M_users.getRecords(mabase);

        for (Integer uneCle : lesUtilisateurs.keySet()) {
            M_users u = lesUtilisateurs.get(uneCle);
            System.out.println(u);
        }
        
        // Test de création et connexion
        System.out.println("\n=== TEST CRÉATION ET CONNEXION ===");
        try {
            M_users newUser = new M_users(mabase, "TestUser2024", "test@example.com", "motdepasse123", "User test", 2);
            System.out.println("✓ Utilisateur créé : " + newUser.getName() + " (ID: " + newUser.getId() + ")");
            
            M_users userConnecte = M_users.connexion_log(mabase, "TestUser2024", "motdepasse123");
            if (userConnecte != null) {
                System.out.println("✓✓✓ CONNEXION RÉUSSIE : " + userConnecte.getName());
            } else {
                System.out.println("❌ CONNEXION ÉCHOUÉE");
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
        
        mabase.closeBase();
    }
}