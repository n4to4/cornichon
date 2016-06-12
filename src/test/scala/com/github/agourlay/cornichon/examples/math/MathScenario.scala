package com.github.agourlay.cornichon.examples.math

import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.core.SimpleStepAssertion
import com.github.agourlay.cornichon.steps.regular.AssertStep

import scala.concurrent.duration._

class MathScenario extends CornichonFeature with MathSteps {

  lazy val feature = Feature("Cornichon feature math examples") {

    Scenario("Simple addition") {

      When I save("arg1" → "2")

      And I save("arg2" → "3")

      Then assert adding_values("arg1", "arg2").equals(5)

    }

    Scenario("Random draw should eventually be identical") {

      When I generate_random_int("random-1")

      Eventually(maxDuration = 3 seconds, interval = 1 millis) {

        When I generate_random_int("random-2")

        Then assert session_values("random-1", "random-2").areEquals

      }
    }

    Scenario("MonteCarlo approximation of PI") {

      Repeat(2000) {

        Given I generate_random_double("x")

        And I generate_random_double("y")

        Then I calculate_point_in_circle("inside")

      }

      And I estimate_pi_from_ratio("inside", "pi")

      Then assert double_value("pi").isBetween(3.0, 3.3)

      And I show_session("pi")

    }

    Scenario("Addition table") {

      WithDataInputs(
        """
          | a | b  | c  |
          | 1 | 3  | 4  |
          | 7 | 4  | 11 |
          | 1 | -1 | 0  |
        """
      ) {
          Then assert AssertStep("sum of 'a' + 'b' = 'c'", s ⇒ SimpleStepAssertion(s.get("a").toInt + s.get("b").toInt, s.get("c").toInt))
        }
    }
  }
}
