import org.specs2._

import play.api.test._
import play.api.test.Helpers._

class StorySpec2 extends Specification {def is =

    "User can" ^
      "use own value for reference ID" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_inproceedings").click()

          // fill details
          browser.$("#referenceId").text("ABC123")
          browser.$("#author").text("authorX")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("year2000")
          browser.$("input.btn-primary").click()

          browser.pageSource must contain("ABC123")
        }
      } ^
      "get an error if trying to use same reference ID again" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
                    
          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_inproceedings").click()

          browser.$("#referenceId").text("ABC123")
          browser.$("#author").text("authorX")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("year2000")
          browser.$("input.btn-primary").click()
          
          browser.$("#add").click()
          browser.$("#add_inproceedings").click()

          browser.$("#referenceId").text("ABC123")
          browser.$("#author").text("authorX")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("year2000")
          browser.$("input.btn-primary").click()

          browser.pageSource must contain("ID already in use. Leave the field empty to generate a random ID.")
        }
      } ^
      "let the reference ID be generated from the author and year" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>

          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_inproceedings").click()

          browser.$("#author").text("Luukkainen, Matti")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("2009")
          browser.$("input.btn-primary").click()

          browser.$(".alert").first.getText must equalTo("Done! Reference has been created!")
          browser.pageSource must contain("L2009")
        }
      } ^
      "let the reference ID be generated from the author and year using automatic suffixes" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>

          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_inproceedings").click()

          browser.$("#author").text("Luukkainen, Matti")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("2009")
          browser.$("input.btn-primary").click()

          browser.$("#add").click()
          browser.$("#add_inproceedings").click()

          browser.$("#author").text("Luukkainen, Matti")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("2009")
          browser.$("input.btn-primary").click()

          browser.$("#add").click()
          browser.$("#add_inproceedings").click()

          browser.$("#author").text("Luukkainen, Matti")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("2009")
          browser.$("input.btn-primary").click()

          browser.pageSource must contain("L2009") and
          contain("L2009-1") and
          contain("L2009-2")
        }
      } ^
      "let the reference ID be generated from the database ID" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>

          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_misc").click()

          browser.$("#author").text("Luukkainen, Matti")
          browser.$("#note").text("noteX")
          browser.$("#title").text("titleX")
          browser.$("input.btn-primary").click()

          browser.$("tr td").get(1).getText must equalTo("1")
        }
      } ^
      "delete a reference" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>

          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_misc").click()

          browser.$("#author").text("Luukkainen, Matti")
          browser.$("#note").text("noteX")
          browser.$("#title").text("titleX")
          browser.$("input.btn-primary").click()

          browser.$("#delete_1").click()
          browser.pageSource must not contain("Luukkainen")
        }
      } ^
      "edit a reference" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>

          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_misc").click()

          browser.$("#author").text("Luukkainen, Matti")
          browser.$("#note").text("noteX")
          browser.$("#title").text("titleX")
          browser.$("input.btn-primary").click()

          browser.$("#edit_1").click()
          browser.$("#author").text("Laukkainen, Matti")
          browser.$("input.btn-primary").click()
          
          browser.pageSource must not contain("Luukkainen") and (browser.pageSource must contain("Laukkainen"))
        }
      }
}
