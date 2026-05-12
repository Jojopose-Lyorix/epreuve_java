package pj_asterix;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class C_asterix {
    private Db_mariadb baseRR;
    private M_Adorer adorer;
    private M_Album album;
    private M_Apparaitre apparaitre;
    private M_personnages unPersonnage;
    private M_categorie uneCategorie;
    private M_citations uneCitations;
    private V_Utilisateur_Admin fm_util;
    private M_Autorisation unAutorisation;
    private M_Autoriser unAutoriser;
    private M_citer unCiter;
    private M_divinites uneDivinite;
    private M_exercer unExerce;
    private M_genres unGenre;
    private M_role unRole;
    private M_users unUtilisateur, utilConnecte;
    
    private V_main fm_main;
    private V_Personnage fm_personnage;
    private V_Divinite fm_divinite;
    
    Integer uneCle;
    
    private LinkedHashMap <Integer, M_role> lesRoles;
    private LinkedHashMap <Integer, M_users> lesUtilisateurs;
    private LinkedHashMap <Integer, M_personnages> lesPersonnages;
    private LinkedHashMap <Integer, M_peuples> lesPeuples;
    private LinkedHashMap <Integer, M_divinites> lesDivinites;
    private LinkedHashMap <Integer, M_Autorisation> lesAutorisations;
    
    
    public C_asterix() throws Exception{
        connection();
        fm_main = new V_main(this);
        fm_personnage = new V_Personnage(fm_main, true);
        fm_divinite = new V_Divinite(fm_main, true);
        fm_util = new V_Utilisateur_Admin(fm_main, true);
        fm_main.afficher();
    }
    
    private void connection () throws Exception{
        baseRR = new Db_mariadb(CL_Connection.url, CL_Connection.login, CL_Connection.password);
    }
    
    
    public void deconnexion() throws SQLException {
        baseRR.closeBase();
    }
    
    public LinkedHashMap <Integer, M_Autorisation> autorisationRole(int idRole) throws SQLException, Exception{
        return M_Autoriser.getLesAutorisation(baseRR, idRole);
    }
    
    // ← AJOUT : Méthode pour obtenir l'ID du rôle de l'utilisateur connecté
    public int getIdRoleUtilConnecte() {
        if (utilConnecte != null) {
            return utilConnecte.getRoleId();
        }
        return 1; // Valeur par défaut (admin) si pas connecté
    }
    
    // ← MODIFICATION : Passer l'ID du rôle à la vue
    public void aff_V_Personnage() throws SQLException{
        lesPersonnages = M_personnages.getRecords(baseRR);
        lesPeuples = M_peuples.getRecords(baseRR);
        
        // ← AJOUT : Définir le rôle avant d'afficher
        fm_personnage.setIdRole(getIdRoleUtilConnecte());
        fm_personnage.afficher(this, lesPersonnages, lesPeuples);
    }
    
    public void enregistrer(int idPerso, String nom, String com, char genre, int peuple) throws IOException, SQLException{
        unPersonnage = new M_personnages(baseRR, idPerso);
        
        unPersonnage.setNom(nom);
        unPersonnage.setCommentaire(com);
        unPersonnage.setGenreCode(String.valueOf(genre));
        unPersonnage.setPeupleId(peuple);
        
        unPersonnage.update();
        aff_V_Personnage();
    }
    
    public void ajouter(String nom, String com, char genre, int peuple) throws IOException, SQLException{
        unPersonnage = new M_personnages(baseRR, nom, com, String.valueOf(genre), peuple);
        lesPersonnages.put(unPersonnage.getId(), unPersonnage);
        aff_V_Personnage();
    }
    
    public void supp_Personnage(int iPerso) throws SQLException{
        unPersonnage = new M_personnages(baseRR, iPerso);
        unPersonnage.delete();
        aff_V_Personnage();
    }
    
    // ← MODIFICATION : Passer l'ID du rôle à la vue des divinités
    public void aff_V_Divinite() throws SQLException{
        lesDivinites = M_divinites.getRecords(baseRR);
        
        // ← AJOUT : Définir le rôle avant d'afficher
        fm_divinite.setIdRole(getIdRoleUtilConnecte());
        fm_divinite.afficher(this, lesDivinites);
    }
    
    public void enregistrer_divi(int idDivi, String nom, String con, String com) throws IOException, SQLException{
        uneDivinite = new M_divinites(baseRR, idDivi);
        uneDivinite.setNom(nom);
        uneDivinite.setCommentaire(com);
        uneDivinite.setContexte(con);
        
        uneDivinite.update();
        aff_V_Divinite();
    }
    
    public void ajouter_divi(String nom, String com, String con) throws IOException, SQLException{
        uneDivinite = new M_divinites(baseRR, nom, com, con);
        lesDivinites.put(uneDivinite.getId(), uneDivinite);
        aff_V_Divinite(); // ← CORRIGÉ : était aff_V_Personnage()
    }
    
    public void supp_divi(int iDivi) throws SQLException{
        uneDivinite = new M_divinites(baseRR, iDivi);
        uneDivinite.delete();
        aff_V_Divinite(); // ← CORRIGÉ : était aff_V_Personnage()
    }
    
    public void aff_V_util() throws SQLException, Exception{
        lesUtilisateurs = M_users.getRecords(baseRR);
        lesRoles = M_role.getRecords(baseRR);
        
        fm_util.afficher(this, lesUtilisateurs, lesRoles);
    }
    
    public void aff_V_MonCompte(M_users utilisateur) throws SQLException, Exception {
        // Récupérer les rôles pour la liste déroulante
        LinkedHashMap<Integer, M_role> lesRoles = M_role.getRecords(baseRR);
        
        V_Utilisateur_Admin laVue = new V_Utilisateur_Admin(fm_main, true);
        laVue.afficherMonCompte(this, utilisateur, lesRoles);
    }
    
    public void modifierUtil(int idUtilisateur, String vName, String vMail, String vCom, String vMdp, int vRole) {
        try {
            if (idUtilisateur == -1) {
                // Création d'un nouvel utilisateur
                unUtilisateur = new M_users(baseRR, vName, vMail, vMdp, vCom, vRole);
                if (lesUtilisateurs != null) {
                    lesUtilisateurs.put(unUtilisateur.getId(), unUtilisateur);
                }
            } else {
                // Modification d'un utilisateur existant
                if (lesUtilisateurs != null) {
                    unUtilisateur = lesUtilisateurs.get(idUtilisateur);
                } else {
                    // Cas "Mon Compte" : charger directement depuis la BDD
                    unUtilisateur = new M_users(baseRR, idUtilisateur);
                }
                
                unUtilisateur.setName(vName);
                unUtilisateur.setEmail(vMail);
                unUtilisateur.setCommentaire(vCom);   
                if (vMdp != null) {
                    unUtilisateur.setPassword(vMdp);
                }     
                unUtilisateur.setRoleId(vRole);      
                
                unUtilisateur.update();
                
                // Mettre à jour utilConnecte si c'est le même utilisateur
                if (utilConnecte != null && utilConnecte.getId() == idUtilisateur) {
                    utilConnecte = unUtilisateur;
                }
            }
            
            // Rafraîchir seulement si lesUtilisateurs existe
            if (lesUtilisateurs != null && lesRoles != null) {
                fm_util.afficher(this, lesUtilisateurs, lesRoles);
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur modification utilisateur: " + e.getMessage());
        }
    }
    
    public void supprimerUtil(int idUtilisateur) {
        try {
            unUtilisateur = lesUtilisateurs.get(idUtilisateur);
            
            if (unUtilisateur != null) {
                // Supprimer de la base de données
                unUtilisateur.delete(idUtilisateur);
                
                // Supprimer de la collection locale
                lesUtilisateurs.remove(idUtilisateur);
                
                // Rafraîchir l'affichage
                fm_util.afficher(this, lesUtilisateurs, lesRoles);
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur suppression utilisateur: " + e.getMessage());
        }
    }
    
    public M_users connection_Util(String Name, String motPasse) throws SQLException{
        utilConnecte = M_users.connexion_log(baseRR, Name, motPasse);
        return utilConnecte;
    }
    
    public void deconnection(){
        utilConnecte = null;
    }
    
    // ← AJOUT : Getter pour l'utilisateur connecté
    public M_users getUtilConnecte() {
        return utilConnecte;
    }
    
    public static void main(String[] args) throws Exception {
        C_asterix leControler = new C_asterix();
    }
   
    public Db_mariadb getBaseRR() {
        return baseRR;
    }
}