package contactmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // liste en mémoire des contacts
    private static List<Contact> contacts = new ArrayList<>();
    // modèle pour la JList qui affiche les contacts
    private static DefaultListModel<String> modeleListe = new DefaultListModel<>();

    public static void main(String[] args) {
        // création de la fenêtre principale
        JFrame fenetre = new JFrame("Gestion des contacts");
        fenetre.setSize(600, 500);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setLayout(new BorderLayout());
        fenetre.getContentPane().setBackground(new Color(240, 248, 255));
        Font police = new Font("SansSerif", Font.PLAIN, 14);

        // panneau du formulaire pour nom, téléphone, email
        JPanel panneauFormulaire = new JPanel(new GridLayout(3, 2, 10, 10));
        panneauFormulaire.setBorder(BorderFactory.createTitledBorder("Informations du contact"));
        panneauFormulaire.setBackground(Color.WHITE);

        JLabel labelNom = new JLabel("Nom :");
        JTextField champNom = new JTextField();
        JLabel labelTel = new JLabel("Téléphone :");
        JTextField champTel = new JTextField();
        JLabel labelEmail = new JLabel("Email :");
        JTextField champEmail = new JTextField();

        // appliquer la police aux labels et champs
        labelNom.setFont(police);
        labelTel.setFont(police);
        labelEmail.setFont(police);
        champNom.setFont(police);
        champTel.setFont(police);
        champEmail.setFont(police);

        panneauFormulaire.add(labelNom);
        panneauFormulaire.add(champNom);
        panneauFormulaire.add(labelTel);
        panneauFormulaire.add(champTel);
        panneauFormulaire.add(labelEmail);
        panneauFormulaire.add(champEmail);

        // boutons Ajouter, Modifier, Supprimer
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");

        // style des boutons
        boutonAjouter.setBackground(new Color(102, 205, 170));
        boutonModifier.setBackground(new Color(255, 204, 102));
        boutonSupprimer.setBackground(new Color(255, 99, 71));
        boutonAjouter.setForeground(Color.WHITE);
        boutonModifier.setForeground(Color.WHITE);
        boutonSupprimer.setForeground(Color.WHITE);

        boutonAjouter.setFont(police);
        boutonModifier.setFont(police);
        boutonSupprimer.setFont(police);

        JPanel panneauBoutons = new JPanel(new GridLayout(1, 3, 10, 0));
        panneauBoutons.setBackground(new Color(240, 248, 255));
        panneauBoutons.add(boutonAjouter);
        panneauBoutons.add(boutonModifier);
        panneauBoutons.add(boutonSupprimer);

        // message d'information à l'utilisateur
        JLabel message = new JLabel("");
        message.setFont(police);
        message.setForeground(Color.DARK_GRAY);

        // assembler le haut de la fenêtre
        JPanel panneauHaut = new JPanel(new BorderLayout(10, 10));
        panneauHaut.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panneauHaut.setBackground(new Color(240, 248, 255));
        panneauHaut.add(panneauFormulaire, BorderLayout.NORTH);
        panneauHaut.add(panneauBoutons, BorderLayout.CENTER);
        panneauHaut.add(message, BorderLayout.SOUTH);

        // zone de recherche en haut de la liste
        JTextField champRecherche = new JTextField();
        champRecherche.setFont(police);
        JPanel panneauRecherche = new JPanel(new BorderLayout());
        panneauRecherche.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panneauRecherche.setBackground(new Color(230, 230, 250));
        panneauRecherche.add(new JLabel("🔍 Recherche : "), BorderLayout.WEST);
        panneauRecherche.add(champRecherche, BorderLayout.CENTER);

        // liste des contacts avec scroll
        JList<String> listeContacts = new JList<>(modeleListe);
        listeContacts.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(listeContacts);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des contacts"));

        JPanel panneauCentre = new JPanel(new BorderLayout());
        panneauCentre.add(panneauRecherche, BorderLayout.NORTH);
        panneauCentre.add(scrollPane, BorderLayout.CENTER);

        // ajouter les panneaux à la fenêtre
        fenetre.add(panneauHaut, BorderLayout.NORTH);
        fenetre.add(panneauCentre, BorderLayout.CENTER);

        // charger les contacts depuis la base au démarrage
        chargerContacts();

        // gestion du clic sur Ajouter
        boutonAjouter.addActionListener(e -> {
            String nom = champNom.getText().trim();
            String tel = champTel.getText().trim();
            String email = champEmail.getText().trim();

            if (!nom.isEmpty()) {
                // validation des champs
                if (!isValidNom(nom)) {
                    message.setText("❌ Le nom ne doit pas contenir de chiffres.");
                    return;
                }
                if (!isValidTelephone(tel)) {
                    message.setText("❌ Le téléphone ne doit pas contenir de lettres.");
                    return;
                }
                if (!isValidEmail(email)) {
                    message.setText("❌ Email non valide.");
                    return;
                }

                // insertion en base de données
                try (Connection conn = Database.getConnection()) {
                    String sql = "INSERT INTO contacts (nom, telephone, email) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, nom);
                    stmt.setString(2, tel);
                    stmt.setString(3, email);
                    stmt.executeUpdate();
                    message.setText("✔ Contact ajouté à la base de données !");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    message.setText("Erreur lors de l'enregistrement.");
                }

                // réinitialiser le formulaire et rafraîchir la liste
                champNom.setText("");
                champTel.setText("");
                champEmail.setText("");
                chargerContacts();
            } else {
                message.setText("Le nom est requis.");
            }
        });

        // gestion du clic sur Modifier
        boutonModifier.addActionListener(e -> {
            int index = listeContacts.getSelectedIndex();
            if (index >= 0 && index < contacts.size()) {
                String nom = champNom.getText().trim();
                String tel = champTel.getText().trim();
                String email = champEmail.getText().trim();

                if (!nom.isEmpty()) {
                    // mêmes validations que pour l'ajout
                    if (!isValidNom(nom)) {
                        message.setText("❌ Le nom ne doit pas contenir de chiffres.");
                        return;
                    }
                    if (!isValidTelephone(tel)) {
                        message.setText("❌ Le téléphone ne doit pas contenir de lettres.");
                        return;
                    }
                    if (!isValidEmail(email)) {
                        message.setText("❌ Email non valide.");
                        return;
                    }

                    // mise à jour en base
                    try (Connection conn = Database.getConnection()) {
                        int id = contacts.get(index).getId();
                        String sql = "UPDATE contacts SET nom=?, telephone=?, email=? WHERE id=?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, nom);
                        stmt.setString(2, tel);
                        stmt.setString(3, email);
                        stmt.setInt(4, id);
                        stmt.executeUpdate();
                        message.setText("✔ Contact modifié !");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        message.setText("Erreur lors de la mise à jour.");
                    }

                    // réinit formulaire et liste
                    champNom.setText("");
                    champTel.setText("");
                    champEmail.setText("");
                    listeContacts.clearSelection();
                    chargerContacts();
                } else {
                    message.setText("Le nom est requis pour modifier.");
                }
            } else {
                message.setText("Sélectionnez un contact à modifier.");
            }
        });

        // gestion du clic sur Supprimer
        boutonSupprimer.addActionListener(e -> {
            int index = listeContacts.getSelectedIndex();
            if (index >= 0 && index < contacts.size()) {
                int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer ce contact ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = Database.getConnection()) {
                        int id = contacts.get(index).getId();
                        String sql = "DELETE FROM contacts WHERE id=?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, id);
                        stmt.executeUpdate();
                        message.setText("✔ Contact supprimé !");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        message.setText("Erreur lors de la suppression.");
                    }

                    // réinit formulaire et rafraîchissement
                    champNom.setText("");
                    champTel.setText("");
                    champEmail.setText("");
                    chargerContacts();
                }
            } else {
                message.setText("Sélectionnez un contact à supprimer.");
            }
        });

        // affichage des détails lors de la sélection
        listeContacts.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = listeContacts.getSelectedIndex();
                if (index >= 0 && index < contacts.size()) {
                    Contact c = contacts.get(index);
                    champNom.setText(c.getNom());  // afficher nom
                    champTel.setText(c.getTelephone());  // afficher téléphone
                    champEmail.setText(c.getEmail());  // afficher email
                }
            }
        });

        // filtrer la liste selon la saisie de recherche
        champRecherche.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = champRecherche.getText().toLowerCase();
                modeleListe.clear();
                for (Contact c : contacts) {
                    if (c.getNom().toLowerCase().contains(query)) {
                        modeleListe.addElement(c.toString());
                    }
                }
            }
        });

        fenetre.setVisible(true);  // afficher la fenêtre
    }

    // charge tous les contacts depuis la base de données
    private static void chargerContacts() {
        try (Connection conn = Database.getConnection()) {
            contacts.clear();
            modeleListe.clear();

            String sql = "SELECT id, nom, telephone, email FROM contacts";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String tel = rs.getString("telephone");
                String email = rs.getString("email");

                Contact c = new Contact(id, nom, tel, email);
                contacts.add(c);
                modeleListe.addElement(c.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // validation du nom (pas de chiffres)
    private static boolean isValidNom(String nom) {
        return !nom.matches(".*\\d.*");
    }

    // validation du téléphone (pas de lettres)
    private static boolean isValidTelephone(String tel) {
        return !tel.matches(".*[a-zA-Z].*");
    }

    // validation de l'email (facultatif mais doit être valide)
    private static boolean isValidEmail(String email) {
        if (email.isEmpty()) return true;
        return email.matches("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Za-z]{2,}$");
    }
}
