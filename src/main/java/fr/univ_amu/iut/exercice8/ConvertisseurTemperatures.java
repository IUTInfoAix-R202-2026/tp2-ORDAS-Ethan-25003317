package fr.univ_amu.iut.exercice8;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

/**
 * Exercice 8 (capstone) - Convertisseur de températures.
 *
 * <p>Cet exercice synthétise tous les types de bindings vus dans le TP :
 *
 * <ul>
 *   <li>Binding unidirectionnel (Labels de lecture)
 *   <li>Binding bidirectionnel (TextField ↔ Slider via {@link NumberStringConverter})
 *   <li>{@code ChangeListener} pour la conversion avec formule (C = (F-32)*5/9)
 *   <li>Sliders verticaux ({@code Orientation.VERTICAL})
 * </ul>
 *
 * <p>L'application affiche deux panneaux côte à côte : un pour Celsius, un pour Fahrenheit.
 * Modifier l'un met à jour l'autre automatiquement.
 */
public class ConvertisseurTemperatures extends Application {

  private boolean updating = false;

  @Override
  public void start(Stage primaryStage) {
    // TODO exercice 8 : construire le convertisseur de températures.
    //
    // 1. Créer le panneau Celsius (VBox) :
    //    - Label "°C" (style bold, 16px)
    //    - Slider vertical [0, 100], valeur initiale 0, id "slider-celsius"
    //    - TextField, id "tf-celsius", maxWidth 50
    //
    // 2. Créer le panneau Fahrenheit (VBox) :
    //    - Label "°F" (style bold, 16px)
    //    - Slider vertical [0, 212], valeur initiale 32, id "slider-fahrenheit"
    //    - TextField, id "tf-fahrenheit", maxWidth 50
    //
    // 3. Ajouter un ChangeListener sur le slider Celsius :
    //    fahrenheit = celsius * 9/5 + 32
    //    (utiliser un flag "updating" pour éviter les boucles infinies)
    //
    // 4. Ajouter un ChangeListener sur le slider Fahrenheit :
    //    celsius = (fahrenheit - 32) * 5/9
    //
    // 5. Lier chaque TextField à son slider via
    //    Bindings.bindBidirectional(tf.textProperty(), slider.valueProperty(),
    //        new NumberStringConverter())
    //
    // 6. Composer les panneaux dans un HBox, créer la Scene, afficher.
    Slider sliderC = new Slider(0, 100, 0);
    sliderC.setId("slider-celsius");

    Slider sliderF = new Slider(32, 212, 32);
    sliderF.setId("slider-fahrenheit");

    TextField tfC = new TextField();
    tfC.setId("tf-celsius");

    TextField tfF = new TextField();
    tfF.setId("tf-fahrenheit");

    Bindings.bindBidirectional(
        tfC.textProperty(), sliderC.valueProperty(), new NumberStringConverter());
    Bindings.bindBidirectional(
        tfF.textProperty(), sliderF.valueProperty(), new NumberStringConverter());

    sliderC
        .valueProperty()
        .addListener(
            (obs, old, newVal) -> {
              if (!updating) {
                updating = true;
                sliderF.setValue(newVal.doubleValue() * 9.0 / 5.0 + 32);
                updating = false;
              }
            });
    sliderF
        .valueProperty()
        .addListener(
            (obs, old, newVal) -> {
              if (!updating) {
                updating = true;
                sliderC.setValue((newVal.doubleValue() - 32) * 5.0 / 9.0);
                updating = false;
              }
            });

    HBox root = new HBox(sliderC, sliderF, tfC, tfF);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
