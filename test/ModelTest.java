import org.junit.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import models.*;

public class ModelTest {
	
    @Test
    public void findById() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
           public void run() {
               Reference refFromDb = Reference.find.byId(1001l);
               assertThat(refFromDb.author).isEqualTo("testaut1");
               assertThat(refFromDb.referenceType.id).isEqualTo(1);
           }
        });
    }
    
    @Test
    public void deleteById() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
           public void run() {
               Reference.find.byId(1001l).delete();
               Reference refFromDb = Reference.find.byId(1l);
               // can't find anymore
               assertThat(refFromDb).isNull();
           }
        });
    }
    
}
