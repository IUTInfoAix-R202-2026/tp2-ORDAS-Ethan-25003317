package fr.univ_amu.iut.exercice1;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;

/**
 * Exercice 1 - Découverte des propriétés JavaFX.
 *
 * <p>Les propriétés JavaFX sont un mécanisme de <b>liaison de données</b> : elles encapsulent une
 * valeur et permettent d'être notifié automatiquement quand cette valeur change. Ce mécanisme est
 * général et utile bien au-delà des interfaces graphiques.
 *
 * <p>Cet exercice explore trois concepts fondamentaux :
 *
 * <ul>
 *   <li>{@link IntegerProperty} : une propriété qui encapsule un entier
 *   <li>{@link InvalidationListener} : notifie quand la propriété est invalidée (lazy)
 *   <li>{@link ChangeListener} : notifie avec l'ancienne et la nouvelle valeur
 * </ul>
 *
 * @see <a href=
 *     "https://openjfx.io/javadoc/25/javafx.base/javafx/beans/property/IntegerProperty.html">IntegerProperty</a>
 */
public class ProprieteSimple {

  private IntegerProperty anIntProperty;
  private InvalidationListener invalidationListener;
  private ChangeListener<Number> changeListener;

  /**
   * Crée la propriété et affiche ses informations sur la sortie standard.
   *
   * <p>Sortie attendue (si la propriété vaut 1024) :
   *
   * <pre>
   * anIntProperty = IntegerProperty [value: 1024]
   * anIntProperty.get() = 1024
   * anIntProperty.getValue() = 1024
   * </pre>
   */
  void creerPropriete() {
    // TODO exercice 1 : créer la propriété et afficher ses informations.
    //
    // 1. Instancier anIntProperty avec new SimpleIntegerProperty() si elle
    // est null. Sinon, ne rien faire (elle a déjà été créée par setAnInt).
    //
    // 2. Afficher sur System.out :
    // - une ligne vide
    // - "anIntProperty = " + anIntProperty (appelle toString())
    // - "anIntProperty.get() = " + anIntProperty.get()
    // - "anIntProperty.getValue() = " + anIntProperty.getValue()
    System.out.println("anIntProperty = " + anIntProperty());
    System.out.println("anIntProperty.get() = " + anIntProperty.get());
    System.out.println("anIntProperty.getValue() = " + anIntProperty.getValue());
  }

  /**
   * Ajoute un {@link InvalidationListener}, modifie la propriete, puis le retire.
   *
   * <p>L'InvalidationListener est "paresseux" : il n'est déclenché qu'une fois après un changement,
   * et ne se redéclenchera pas tant que la valeur n'a pas été lue (via get()).
   *
   * <p>Sortie attendue :
   *
   * <pre>
   * Add invalidation listener.
   * setValue() with 1024.
   * set() with 2105.
   * The observable has been invalidated.
   * setValue() with 5012.
   * Remove invalidation listener.
   * set() with 1024.
   * </pre>
   *
   * <p>Remarque : setValue(1024) ne déclenche rien car la valeur ne change pas. set(2105) déclenche
   * le listener. setValue(5012) ne le redéclenche PAS car la valeur n'a pas été lue entre-temps
   * (comportement paresseux).
   */
  void ajouterEtRetirerInvalidationListener() {
    // TODO exercice 1 : ajouter un InvalidationListener et observer son
    // comportement.
    //
    // 1. Afficher une ligne vide puis "Add invalidation listener."
    //
    // 2. Créer un InvalidationListener qui affiche
    // "The observable has been invalidated." et le stocker dans
    // le champ this.invalidationListener.
    //
    // 3. Ajouter le listener à anIntProperty via addListener().
    //
    // 4. Afficher "setValue() with 1024." puis appeler setValue(1024).
    // -> Le listener ne se déclenche PAS (même valeur).
    //
    // 5. Afficher "set() with 2105." puis appeler set(2105).
    // -> Le listener se déclenche.
    //
    // 6. Afficher "setValue() with 5012." puis appeler setValue(5012).
    // -> Le listener ne se redéclenche PAS (paresseux : pas de get() entre-temps).
    //
    // 7. Afficher "Remove invalidation listener." et retirer le listener.
    //
    // 8. Afficher "set() with 1024." puis appeler set(1024).
    // -> Rien ne se passe (listener retiré).
    this.invalidationListener = obs -> System.out.println("The observable has been invalidated.");
    anIntProperty.addListener(invalidationListener);
    System.out.println("Add invalidation listener.");
    System.out.println("setValue() with 1024.");
    anIntProperty.setValue(1024);
    System.out.println("set() with 2105.");
    anIntProperty.set(2105);
    System.out.println("setValue() with 5012.");
    anIntProperty.setValue(5012);
    System.out.println("Remove invalidation listener.");
    anIntProperty.removeListener(invalidationListener);
    System.out.println("set() with 1024.");
    anIntProperty.set(1024);
  }

  /**
   * Ajoute un {@link ChangeListener}, modifie la propriété, puis le retire.
   *
   * <p>Contrairement à l'InvalidationListener, le ChangeListener est déclenché à chaque changement
   * de valeur et reçoit l'ancienne ET la nouvelle valeur.
   *
   * <p>Sortie attendue :
   *
   * <pre>
   * Add change listener.
   * setValue() with 1024.
   * set() with 2105.
   * The observableValue has changed: oldValue = 1024, newValue = 2105
   * setValue() with 5012.
   * The observableValue has changed: oldValue = 2105, newValue = 5012
   * Remove change listener.
   * set() with 1024.
   * </pre>
   */
  void ajouterEtRetirerChangeListener() {
    // TODO exercice 1 : ajouter un ChangeListener et observer son comportement.
    //
    // 1. Afficher une ligne vide puis "Add change listener."
    //
    // 2. Créer un ChangeListener<Number> qui affiche
    // "The observableValue has changed: oldValue = X, newValue = Y"
    // et le stocker dans this.changeListener.
    //
    // 3. Ajouter le listener à anIntProperty.
    //
    // 4. Afficher "setValue() with 1024." puis appeler setValue(1024).
    // -> Pas de déclenchement (même valeur).
    //
    // 5. Afficher "set() with 2105." puis appeler set(2105).
    // -> Le listener se déclenche avec oldValue=1024, newValue=2105.
    //
    // 6. Afficher "setValue() with 5012." puis appeler setValue(5012).
    // -> Le listener se déclenche avec oldValue=2105, newValue=5012.
    // (Contrairement à l'InvalidationListener, il se déclenche à chaque
    // changement.)
    //
    // 7. Afficher "Remove change listener." et retirer le listener.
    //
    // 8. Afficher "set() with 1024." puis appeler set(1024).
    // -> Rien (listener retiré).
    this.changeListener =
        (observable, oldValue, newValue) ->
            System.out.println(
                "The observableValue has changed: oldValue = "
                    + oldValue
                    + ", newValue = "
                    + newValue);
    anIntProperty.addListener(changeListener);
    System.out.println("Add change listener.");
    System.out.println("setValue() with 1024.");
    anIntProperty.setValue(1024);
    System.out.println("set() with 2105.");
    anIntProperty.set(2105);
    System.out.println("setValue() with 5012.");
    anIntProperty.setValue(5012);
    System.out.println("Remove change listener.");
    anIntProperty.removeListener(changeListener);
    System.out.println("set() with 1024.");
    anIntProperty.set(1024);
  }

  public int getAnInt() {
    return anIntProperty.get();
  }

  public void setAnInt(int value) {
    if (anIntProperty == null) {
      anIntProperty = new SimpleIntegerProperty();
    }
    anIntProperty.set(value);
  }

  public IntegerProperty anIntProperty() {
    return anIntProperty;
  }

  public static void main(String[] args) {
    ProprieteSimple exemple = new ProprieteSimple();
    exemple.setAnInt(1024);
    exemple.creerPropriete();
    exemple.ajouterEtRetirerInvalidationListener();
    exemple.ajouterEtRetirerChangeListener();
  }
}
