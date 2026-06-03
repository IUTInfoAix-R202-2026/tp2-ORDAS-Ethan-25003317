package fr.univ_amu.iut.exercice7;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/** Tests de l'exercice 7 - Cercle interactif avec binding bidirectionnel. */
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CercleInteractifTest {

  private Stage stage;

  @Start
  void start(Stage stage) throws Exception {
    stage.setScene(null); // évite la fuite de Scene entre tests (TestFX réutilise le Stage)
    this.stage = stage;
    new CercleInteractif().start(stage);
  }

  @Test
  @Order(1)
  void laFenetreEstVisible(FxRobot robot) {
    assertThat(stage.isShowing()).as("le Stage doit être affiché").isTrue();
  }

  @Test
  @Order(2)
  void leCercleExiste(FxRobot robot) {
    Circle c = robot.lookup("#cercle").queryAs(Circle.class);
    assertThat(c).as("un Circle avec id 'cercle' doit exister").isNotNull();
  }

  @Test
  @Order(3)
  void leSliderExiste(FxRobot robot) {
    Slider s = robot.lookup("#slider").queryAs(Slider.class);
    assertThat(s).as("un Slider avec id 'slider' doit exister").isNotNull();
    assertThat(s.getMax()).as("le max du slider doit être 250").isEqualTo(250);
  }

  @Test
  @Order(4)
  void leTextFieldExiste(FxRobot robot) {
    TextField tf = robot.lookup("#rayon").queryAs(TextField.class);
    assertThat(tf).as("un TextField avec id 'rayon' doit exister").isNotNull();
  }

  @Test
  @Order(5)
  void rayonInitialEst150(FxRobot robot) {
    Circle c = robot.lookup("#cercle").queryAs(Circle.class);
    Slider s = robot.lookup("#slider").queryAs(Slider.class);
    assertThat(c.getRadius())
        .as("le rayon initial du cercle doit être 150")
        .isCloseTo(150, within(1.0));
    assertThat(s.getValue())
        .as("la valeur initiale du slider doit être 150")
        .isCloseTo(150, within(1.0));
  }

  @Test
  @Order(6)
  void deplacerSliderModifieLeCercle(FxRobot robot) {
    Slider s = robot.lookup("#slider").queryAs(Slider.class);
    Circle c = robot.lookup("#cercle").queryAs(Circle.class);

    robot.interact(() -> s.setValue(100));

    assertThat(c.getRadius())
        .as("quand le slider passe à 100, le rayon du cercle doit suivre")
        .isCloseTo(100, within(1.0));
  }

  @Test
  @Order(7)
  void deplacerSliderModifieLeTextField(FxRobot robot) {
    Slider s = robot.lookup("#slider").queryAs(Slider.class);
    TextField tf = robot.lookup("#rayon").queryAs(TextField.class);

    robot.interact(() -> s.setValue(200));

    assertThat(tf.getText())
        .as("quand le slider passe à 200, le TextField doit contenir '200'")
        .contains("200");
  }

  @Test
  @Order(8)
  void leTextFieldModifieLeSliderEtLeCercle(FxRobot robot) {
    TextField tf = robot.lookup("#rayon").queryAs(TextField.class);
    Slider s = robot.lookup("#slider").queryAs(Slider.class);
    Circle c = robot.lookup("#cercle").queryAs(Circle.class);

    robot.interact(() -> tf.setText("75"));

    assertThat(s.getValue())
        .as("quand on saisit '75' dans le TextField, le slider doit passer à 75")
        .isCloseTo(75, within(1.0));
    assertThat(c.getRadius())
        .as("quand on saisit '75' dans le TextField, le cercle doit avoir un rayon de 75")
        .isCloseTo(75, within(1.0));
  }

  @Test
  @Order(9)
  void leCercleEstCentreDansLePanneau(FxRobot robot) {
    Circle c = robot.lookup("#cercle").queryAs(Circle.class);
    assertThat(c.centerXProperty().isBound())
        .as("centerX du cercle doit être lié par bind() (centrage automatique)")
        .isTrue();
    assertThat(c.centerYProperty().isBound())
        .as("centerY du cercle doit être lié par bind() (centrage automatique)")
        .isTrue();
  }
}
