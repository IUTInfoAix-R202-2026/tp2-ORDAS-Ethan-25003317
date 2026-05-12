package fr.univ_amu.iut.exercice3;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Exercice 3 - Palette réactive (pont avec le TP1).
 *
 * <p>Cet exercice reprend la Palette du TP1 (exercice 6) et la refactorise avec des propriétés
 * JavaFX. Le comportement est identique, mais l'implémentation est supérieure :
 *
 * <ul>
 *   <li>TP1 : {@code int[] compteurs} + {@code setText()} dans chaque handler (3x le meme code)
 *   <li>TP2 : {@code IntegerProperty nbClics} dans chaque {@link BoutonCouleur} + 1 binding
 * </ul>
 *
 * <p>Comportement attendu :
 *
 * <pre>
 * +------------------------------+
 * | [Rouge] [Vert] [Bleu]        |  HBox de 3 BoutonCouleur
 * +------------------------------+
 * |                              |
 * |     (zone de couleur)        |  Pane #zone dont le fond change
 * |                              |
 * +------------------------------+
 * | Rouge: 0  Vert: 0  Bleu: 0  |  Label #compteurs (bind)
 * +------------------------------+
 * </pre>
 *
 * @see BoutonCouleur
 */
public class PaletteReactive extends Application {

  @Override
  public void start(Stage primaryStage) {
    // TODO exercice 3 : réimplémenter la Palette du TP1 avec des propriétés.
    //
    // 1. Créer un BorderPane comme racine.
    //
    // 2. Top : un HBox avec trois BoutonCouleur :
    // - new BoutonCouleur("Rouge", "red") id: "btn-rouge"
    // - new BoutonCouleur("Vert", "green") id: "btn-vert"
    // - new BoutonCouleur("Bleu", "blue") id: "btn-bleu"
    //
    // 3. Center : un Pane avec l'id "zone", taille minimale 300x200.
    //
    // 4. Bottom : un Label avec l'id "compteurs".
    //
    // 5. Appeler createBindings() pour lier le label et la zone aux boutons.
    //
    // 6. Créer la Scene, l'attacher au Stage, afficher.
    BorderPane borderPane = new BorderPane();
    Scene scene = new Scene(borderPane);
    BoutonCouleur boutonRouge = new BoutonCouleur("Rouge", "red");
    boutonRouge.setId("btn-rouge");
    BoutonCouleur boutonVert = new BoutonCouleur("Vert", "green");
    boutonVert.setId("btn-vert");
    BoutonCouleur boutonBleu = new BoutonCouleur("Bleu", "blue");
    boutonBleu.setId("btn-bleu");
    HBox hbox = new HBox(10);
    hbox.getChildren().addAll(boutonRouge, boutonVert, boutonBleu);
    borderPane.setTop(hbox);
    Pane zone = new Pane();
    zone.setId("zone");
    zone.setMinSize(300, 200);
    borderPane.setCenter(zone);
    Label compteur = new Label("Bienvenue !");
    compteur.setId("compteurs");
    borderPane.setBottom(compteur);
    createBindings(boutonRouge, boutonVert, boutonBleu, zone, compteur);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Crée les bindings entre les boutons, la zone de couleur et le label compteurs.
   *
   * <p>Cette méthode remplace les 3 handlers {@code setOnAction} du TP1 par des bindings
   * déclaratifs. Après cette méthode, plus aucun {@code setText()} n'est nécessaire : le label se
   * met à jour automatiquement quand un compteur change.
   */
  void createBindings(
      BoutonCouleur btnRouge,
      BoutonCouleur btnVert,
      BoutonCouleur btnBleu,
      Pane zone,
      Label labelCompteurs) {

    btnRouge.setId("btn-rouge");

    btnRouge
        .nbClicsProperty()
        .addListener(
            (_, _, _) -> {
              zone.setStyle("-fx-background-color: " + btnRouge.getCouleur() + ";");
            });

    btnVert.setId("btn-vert");
    btnVert
        .nbClicsProperty()
        .addListener(
            (_, _, _) -> {
              zone.setStyle("-fx-background-color: " + btnVert.getCouleur() + ";");
            });
    btnBleu.setId("btn-bleu");
    btnBleu
        .nbClicsProperty()
        .addListener(
            (_, _, _) -> {
              zone.setStyle("-fx-background-color: " + btnBleu.getCouleur() + ";");
            });

    StringExpression compteur =
        Bindings.concat(
            "Rouge: ",
            btnRouge.nbClicsProperty().asString(),
            "  Vert: ",
            btnVert.nbClicsProperty().asString(),
            "  Bleu: ",
            btnBleu.nbClicsProperty().asString());

    BooleanBinding aucunClic =
        btnRouge
            .nbClicsProperty()
            .isEqualTo(0)
            .and(btnVert.nbClicsProperty().isEqualTo(0))
            .and(btnBleu.nbClicsProperty().isEqualTo(0));
    StringExpression texteFinal = Bindings.when(aucunClic).then("Bienvenue !").otherwise(compteur);

    labelCompteurs.textProperty().bind(texteFinal);
    // TODO exercice 3 : créer les bindings.
    //
    // 1. Pour chaque bouton, ajouter un handler setOnAction (en plus de celui
    // du BoutonCouleur) qui change le style de la zone :
    // zone.setStyle("-fx-background-color: " + btn.getCouleur() + ";")
    // Note : le BoutonCouleur incrémente déjà nbClics dans son propre handler.
    // L'ajout d'un 2e handler via addEventHandler(ActionEvent.ACTION, ...) ou
    // en encapsulant l'ancien fonctionne aussi, mais le plus simple est
    // d'utiliser un ChangeListener sur nbClicsProperty() pour changer la couleur.
    //
    // 2. Créer une StringExpression avec Bindings.concat() :
    // "Rouge: " + btnRouge.nbClicsProperty().asString()
    // + " Vert: " + btnVert.nbClicsProperty().asString()
    // + " Bleu: " + btnBleu.nbClicsProperty().asString()
    //
    // 3. Lier labelCompteurs.textProperty() à cette expression via bind().
    //
    // 4. (Optionnel) Utiliser Bindings.when() pour afficher "Bienvenue !"
    // quand aucun bouton n'a été cliqué, et le texte des compteurs sinon.

  }

  public static void main(String[] args) {
    launch(args);
  }
}
