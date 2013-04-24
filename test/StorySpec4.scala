import org.specs2._

import play.api.test._
import play.api.test.Helpers._

class StorySpec4 extends Specification {def is =

    "User can" ^
      "edit references" ! {
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

          browser.$("#edit_1").click()
          browser.$("#author").text("authorY")
          browser.$("#booktitle").text("booktitleY")
          browser.$("#editor").text("editorX")
          browser.$("input.btn-primary").click()

          browser.pageSource must contain("authorY") and
          contain("booktitleY") and
          contain("editorX")
        }
      } ^
      "not leave required fields empty when editing" ! {
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

          browser.$("#edit_1").click()
          browser.$("#author").text("")
          browser.$("#booktitle").text("")
          browser.$("#editor").text("editorX")
          browser.$("input.btn-primary").click()

          browser.pageSource must not contain("Reference has been updated!")
        }
      } ^
      "not leave reference id empty when editing" ! {
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

          browser.$("#edit_1").click()
          browser.$("#referenceId").text("")
          browser.$("input.btn-primary").click()

          browser.pageSource must not contain("Reference has been updated!")
        }
      } ^
      "not use already existing reference id when editing" ! {
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

          browser.goTo("http://localhost:3333/")
          browser.$("#add").click()
          browser.$("#add_inproceedings").click()

          browser.$("#referenceId").text("ABC456")
          browser.$("#author").text("authorX")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("year2000")
          browser.$("input.btn-primary").click()

          browser.$("#edit_1").click()
          browser.$("#referenceId").text("ABC456")
          browser.$("input.btn-primary").click()

          browser.pageSource must not contain("Reference has been updated!")
        }
      } 
      
}
