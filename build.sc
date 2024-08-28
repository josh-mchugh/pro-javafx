import mill._, scalalib._

trait AppModule extends ScalaModule {
  def scalaVersion = "3.3.3"

  def resolutionCustomizer = T.task {
     Some( (r: coursier.core.Resolution) =>
       r.withOsInfo(coursier.core.Activation.Os.fromProperties(sys.props.toMap))
     )
   }

  def ivyDeps = {
    Agg(
      ivy"org.scalafx::scalafx:22.0.0-R33",
      ivy"org.controlsfx:controlsfx:11.2.1",
      ivy"org.openjfx:javafx-base:22.0.2",
      ivy"org.openjfx:javafx-controls:22.0.2",
      ivy"org.openjfx:javafx-fxml:22.0.2",
      ivy"org.openjfx:javafx-graphics:22.0.2",
      ivy"org.openjfx:javafx-media:22.0.2",
      ivy"org.openjfx:javafx-swing:22.0.2",
      ivy"org.openjfx:javafx-web:22.0.2",
    )
  }
}

object app extends AppModule { }

object chapter1 extends AppModule {
  object helloearthrise extends AppModule { }
  object helloscrollpane extends AppModule { }
  object audioconfig extends AppModule { }
}

object chapter2 extends AppModule {
  object stagecoach extends AppModule { }
  object onthescene extends AppModule { }
  object metronome1 extends AppModule { }
  object metronomepathtransition extends AppModule { }
}