import org.specs2._

import play.api.test._
import play.api.test.Helpers._

class StorySpec3 extends Specification {def is =

    "User can" ^
      "sort the references by title descending" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("#title").text("aaa")
          browser.$("input.btn-primary").click()
          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("#title").text("bbb")
          browser.$("input.btn-primary").click()
          
          browser.$("#sort_by_referenceId").click()
          browser.$("#sort_by_referenceId").click()

          browser.pageSource must be matching("(?s).*bbb.*aaa.*")
        }
      } ^
      "sort the references by title ascending" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("#title").text("aaa")
          browser.$("input.btn-primary").click()
          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("#title").text("bbb")
          browser.$("input.btn-primary").click()
          
          browser.$("#sort_by_referenceId").click()

          browser.pageSource must be matching("(?s).*aaa.*bbb.*")
        }
      } ^
      "sort the references by author descending" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("#author").text("aaa")
          browser.$("input.btn-primary").click()
          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("#author").text("bbb")
          browser.$("input.btn-primary").click()
          
          browser.$("#sort_by_author").click()
          browser.$("#sort_by_author").click()

          browser.pageSource must be matching("(?s).*bbb.*aaa.*")
        }
      } ^
      "sort the references by author ascending" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("#author").text("aaa")
          browser.$("input.btn-primary").click()
          browser.$("#add").click()
          browser.$("#add_misc").click()
          browser.$("#author").text("bbb")
          browser.$("input.btn-primary").click()
          
          browser.$("#sort_by_author").click()

          browser.pageSource must be matching("(?s).*aaa.*bbb.*")
        }
      }
}
