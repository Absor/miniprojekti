import org.specs2._

import play.api.test._
import play.api.test.Helpers._

class StoryTest extends Specification {
  def is =

    "User can" ^
      "add an inproceedings reference" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // main page
          browser.$("header h1").first.getText must equalTo("References list")

          // go to chooser
          browser.$("#add").click()

          browser.$("header h1").first.getText must equalTo("Choose a reference type")

          // go to add
          browser.$("#add_inproceedings").click()

          // fill required
          browser.$("#author").text("authorX")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("year2000")
          browser.$("input.primary").click()

          browser.$("header h1").first.getText must equalTo("References list")

          browser.$(".alert-message").first.getText must equalTo("Done! Reference has been created!")
        }
      } ^
      "generate a BibTeX file from the reference(s)" ! {
        "test" must startWith("test")
      } ^
      "add a valid inproceedings reference" ! {
        "test" must endWith("test")
      }
}