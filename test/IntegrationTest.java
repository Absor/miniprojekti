import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {
    
    @Test
    public void addingInproceedingsWorks() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                
                // main page
                assertThat(browser.$("header h1").first().getText()).isEqualTo("References list");

                // go to chooser
                browser.$("#add").click();
                
                assertThat(browser.$("header h1").first().getText()).isEqualTo("Choose a reference type");
                
                // go to add
                browser.$("#add_inproceedings").click();
                
                // fill required
                browser.$("#author").text("authorX");
                browser.$("#booktitle").text("booktitleX");
                browser.$("#title").text("titleX");
                browser.$("#year").text("year2000");
                browser.$("input.primary").click();
                
                assertThat(browser.$("header h1").first().getText()).isEqualTo("References list");
                
                assertThat(browser.$(".alert-message").first().getText()).isEqualTo("Done! Reference has been created!");
            }
        });
    }
}
