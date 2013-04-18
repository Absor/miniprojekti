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
          
          browser.pageSource must contain("@inproceedings{aar2000") and
				contain("title = {titleX}") and
				contain("booktitle = {booktitleX}") and
				contain("year = {year2000}") and
				contain("author = {authorX}")
        }
      } ^
      "add a valid inproceedings reference by requiring all required fields filled" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_inproceedings").click()

          // fill one by one and submit after every
          browser.$("#author").text("authorX")
          browser.$("input.btn-primary").click()
          browser.$("#title").text("titleX")
          browser.$("input.btn-primary").click()
          browser.$("#booktitle").text("booktitleX")
          browser.$("input.btn-primary").click()
          
          // should still be on the page
          browser.$(".help-inline").get(3).getText must equalTo("required") 
        }
      } ^
      "choose to create most used reference types (inproceedings, article, book, misc)" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          browser.pageSource must contain("inproceedings") and contain("article") and contain("book") and contain("misc")
        }
      } ^
      "add an article reference" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_article").click()

          // fill required
          browser.$("#author").text("authorX")
          browser.$("#journal").text("journalX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("year2000")
          browser.$("input.btn-primary").click()

          browser.$(".alert").first.getText must equalTo("Done! Reference has been created!")
        }
      } ^
      "add a book reference" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_book").click()

          // fill required
          browser.$("#author").text("authorX")
          browser.$("#publisher").text("publisherX")
          browser.$("#title").text("titleX")
          browser.$("#year").text("year2000")
          browser.$("input.btn-primary").click()

          browser.$(".alert").first.getText must equalTo("Done! Reference has been created!")
        }
      } ^
      "add a misc reference" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // go to chooser
          browser.$("#add").click()

          // go to add
          browser.$("#add_misc").click()

          // fill none
          browser.$("input.btn-primary").click()

          browser.$(".alert").first.getText must equalTo("Done! Reference has been created!")
        }
      } ^
      "see a list of references" ! {
        running(TestServer(3333), HTMLUNIT) { browser =>
          browser.goTo("http://localhost:3333/")

          // 1-4
          for(i <- 1 until 5) {
	          browser.$("#add").click()
	          browser.$("#add_misc").click()
	          browser.$("#author").text("author" + i)
	          browser.$("input.btn-primary").click()
          }          

          browser.pageSource must contain("author1") and contain("author2") and contain("author3") and contain("author4")
        }
      }
}
