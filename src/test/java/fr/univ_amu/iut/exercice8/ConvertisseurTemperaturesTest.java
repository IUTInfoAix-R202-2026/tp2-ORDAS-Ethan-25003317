package fr.univ_amu.iut.exercice8;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/** Tests de l'exercice 8 (capstone) - Convertisseur de températures. */
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConvertisseurTemperaturesTest {

  private Stage stage;

  @Start
  void start(Stage stage) throws Exception {
    stage.setScene(null); // évite la fuite de Scene entre tests (TestFX réutilise le Stage)
    this.stage = stage;
    new ConvertisseurTemperatures().start(stage);
  }

  @Test
  @Order(1)
  void laFenetreEstVisible(FxRobot robot) {
    assertThat(stage.isShowing()).as("le Stage doit être affiché").isTrue();
  }

  @Test
  @Order(2)
  void leSliderCelsiusExiste(FxRobot robot) {
    Slider s = robot.lookup("#slider-celsius").queryAs(Slider.class);
    assertThat(s).as("un Slider avec id 'slider-celsius' doit exister").isNotNull();
    assertThat(s.getMax()).as("le max du slider Celsius doit être 100").isEqualTo(100);
  }

  @Test
  @Order(3)
  void leSliderFahrenheitExiste(FxRobot robot) {
    Slider s = robot.lookup("#slider-fahrenheit").queryAs(Slider.class);
    assertThat(s).as("un Slider avec id 'slider-fahrenheit' doit exister").isNotNull();
    assertThat(s.getMax()).as("le max du slider Fahrenheit doit être 212").isEqualTo(212);
  }

  @Test
  @Order(4)
  void lesTextFieldsExistent(FxRobot robot) {
    TextField tfC = robot.lookup("#tf-celsius").queryAs(TextField.class);
    TextField tfF = robot.lookup("#tf-fahrenheit").queryAs(TextField.class);
    assertThat(tfC).as("un TextField avec id 'tf-celsius' doit exister").isNotNull();
    assertThat(tfF).as("un TextField avec id 'tf-fahrenheit' doit exister").isNotNull();
  }

  @Test
  @Order(5)
  void valeurInitialeCorrecte(FxRobot robot) {
    Slider celsius = robot.lookup("#slider-celsius").queryAs(Slider.class);
    Slider fahrenheit = robot.lookup("#slider-fahrenheit").queryAs(Slider.class);
    assertThat(celsius.getValue())
        .as("la valeur initiale du slider Celsius doit être 0")
        .isCloseTo(0, within(1.0));
    assertThat(fahrenheit.getValue())
        .as("la valeur initiale du slider Fahrenheit doit être 32 (0°C = 32°F)")
        .isCloseTo(32, within(1.0));
  }

  @Test
  @Order(6)
  void deplacerCelsiusMetAJourFahrenheit(FxRobot robot) {
    Slider celsius = robot.lookup("#slider-celsius").queryAs(Slider.class);
    Slider fahrenheit = robot.lookup("#slider-fahrenheit").queryAs(Slider.class);

    robot.interact(() -> celsius.setValue(100));

    assertThat(fahrenheit.getValue())
        .as("100°C doit correspondre à 212°F")
        .isCloseTo(212, within(1.0));
  }

  @Test
  @Order(7)
  void deplacerFahrenheitMetAJourCelsius(FxRobot robot) {
    Slider celsius = robot.lookup("#slider-celsius").queryAs(Slider.class);
    Slider fahrenheit = robot.lookup("#slider-fahrenheit").queryAs(Slider.class);

    robot.interact(() -> fahrenheit.setValue(212));

    assertThat(celsius.getValue())
        .as("212°F doit correspondre à 100°C")
        .isCloseTo(100, within(1.0));
  }

  @Test
  @Order(8)
  void textFieldCelsiusSynchroAvecSlider(FxRobot robot) {
    Slider celsius = robot.lookup("#slider-celsius").queryAs(Slider.class);
    TextField tfC = robot.lookup("#tf-celsius").queryAs(TextField.class);

    robot.interact(() -> celsius.setValue(50));

    assertThat(tfC.getText())
        .as("quand le slider Celsius passe à 50, le TextField doit contenir '50'")
        .contains("50");
  }

  @Test
  @Order(9)
  void conversionCorrecteValeurIntermediaire(FxRobot robot) {
    Slider celsius = robot.lookup("#slider-celsius").queryAs(Slider.class);
    Slider fahrenheit = robot.lookup("#slider-fahrenheit").queryAs(Slider.class);

    // Température du corps humain : 37°C = 98.6°F
    robot.interact(() -> celsius.setValue(37));

    assertThat(fahrenheit.getValue())
        .as("37°C doit correspondre à environ 98.6°F")
        .isCloseTo(98.6, within(1.0));
  }

  @Test
  @Order(10)
  void textFieldFahrenheitSynchroAvecSlider(FxRobot robot) {
    Slider fahrenheit = robot.lookup("#slider-fahrenheit").queryAs(Slider.class);
    TextField tfF = robot.lookup("#tf-fahrenheit").queryAs(TextField.class);

    robot.interact(() -> fahrenheit.setValue(100));

    assertThat(tfF.getText())
        .as("quand le slider Fahrenheit passe à 100, le TextField doit contenir '100'")
        .contains("100");
  }

  @Test
  @Order(11)
  void conversionAllerRetour(FxRobot robot) {
    Slider celsius = robot.lookup("#slider-celsius").queryAs(Slider.class);
    Slider fahrenheit = robot.lookup("#slider-fahrenheit").queryAs(Slider.class);

    // Aller : 50°C -> 122°F
    robot.interact(() -> celsius.setValue(50));
    assertThat(fahrenheit.getValue()).as("50°C -> 122°F").isCloseTo(122, within(1.0));

    // Retour : remettre Fahrenheit à 32 -> Celsius revient à 0
    robot.interact(() -> fahrenheit.setValue(32));
    assertThat(celsius.getValue()).as("32°F -> 0°C").isCloseTo(0, within(1.0));
  }
}
