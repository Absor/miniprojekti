import org.specs2._

import play.api.test._
import play.api.test.Helpers._

class StorySpec3 extends Specification {def is =

    "User can" ^
      "sort the references by id" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("input.btn-primary").click()
          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("input.btn-primary").click()
          
          browser.$("#sort_by_referenceId").click()
          browser.$("#sort_by_referenceId").click()

          browser.$("tr td").get(1).getText must equalTo("2")
        }
      }
}
