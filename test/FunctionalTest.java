import org.junit.*;

import java.util.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class FunctionalTest {

    @Test
    public void redirectsToReferences() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
           public void run() {
               Result result = callAction(controllers.routes.ref.Application.index());

               assertThat(status(result)).isEqualTo(SEE_OTHER);
               assertThat(redirectLocation(result)).isEqualTo("/references");
           }
        });
    }
    
    @Test
    public void listsReferencesOnFrontPage() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
           public void run() {
               Result result = callAction(controllers.routes.ref.Application.list());

               assertThat(status(result)).isEqualTo(OK);
               assertThat(contentAsString(result)).contains("References list");
           }
        });
    }
    
    @Test
    public void createANewReference() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.save());

                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                
                Map<String,String> data = new HashMap<String,String>();
                data.put("author", "badbadbad");
                
                result = callAction(
                    controllers.routes.ref.Application.save(), 
                    fakeRequest().withFormUrlEncodedBody(data)
                );
                
                // should fail with no type
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("<input type=\"text\" id=\"author\" name=\"author\" value=\"badbadbad\" >");
                
                // add the required fields
                data.put("referenceType.id", "1");
                data.put("title", "test");
                data.put("year", "2013");
                data.put("publisher", "otava");
                
                result = callAction(
                    controllers.routes.ref.Application.save(), 
                    fakeRequest().withFormUrlEncodedBody(data)
                );
                
                // should succeed
                assertThat(status(result)).isEqualTo(SEE_OTHER);
                assertThat(redirectLocation(result)).isEqualTo("/references");
                assertThat(flash(result).get("success")).isEqualTo("Reference has been created!");
                
                // should be listed
                result = callAction(controllers.routes.ref.Application.list());
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentAsString(result)).contains("badbadbad");
                
            }
        });
    }
    
    @Test
    public void downloadBibtex() {
    	running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.generateBib());

                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentAsString(result)).contains("author = {\\aa\\\"{o}\\\"{a}\\{\\\"\\$},");
            }
         });
    }
    
}
