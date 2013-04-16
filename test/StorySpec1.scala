import org.specs2._

import play.api.test._
import play.api.test.Helpers._

class StorySpec1 extends Specification {def is =

    "User can" ^
      "add an inproceedings reference" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_inproceedings").click()

          // fill required
          browser.$("#author").text("authorX")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("year2000")
          browser.$("input.btn-primary").click()

          browser.$(".alert").first.getText must equalTo("Done! Reference has been created!")
        }
      } ^
      "generate a BibTeX file from the reference(s)" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
                    
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_inproceedings").click()

          // fill required
          browser.$("#author").text("authorX")
          browser.$("#booktitle").text("booktitleX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("year2000")
          browser.$("input.btn-primary").click()

          // go to file
          browser.$("#download").click()
          
          browser.pageSource must contain("@inproceedings{1") and
				contain("title = {titleX}") and
				contain("booktitle = {booktitleX}") and
				contain("year = {year2000}") and
				contain("author = {authorX}")
        }
      } ^
      "add a valid inproceedings reference by requiring all required fields filled (test1)" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_inproceedings").click()

          // fill only one
          browser.$("#author").text("authorX")
          browser.$("input.btn-primary").click()
          
          browser.$(".help-inline").get(1).getText must equalTo("required") 
        }
      } ^
      "add a valid inproceedings reference by requiring all required fields filled (test2)" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_inproceedings").click()

          // fill only two
          browser.$("#author").text("authorX")
	  browser.$("#booktitle").text("booktitleX")
          browser.$("input.btn-primary").click()
          
          browser.$(".help-inline").get(2).getText must equalTo("required") 
        }
      } ^
      "add a valid inproceedings reference by requiring all required fields filled (test3)" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_inproceedings").click()

          // fill only one
          browser.$("#author").text("authorX")
	  browser.$("#title").text("titleX")
	  browser.$("#booktitle").text("booktitleX")
          browser.$("input.btn-primary").click()
          
          browser.$(".help-inline").get(3).getText must equalTo("required") 
        }
      }
}
