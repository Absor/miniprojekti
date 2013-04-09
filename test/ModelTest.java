import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.xml.FieldType;
import org.junit.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import models.*;

public class ModelTest {
	
	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }
	
    @Test
    public void findById() {
       Reference refFromDb = Reference.find.byId(1001l);
       assertThat(refFromDb.author).isEqualTo("testaut1");
       assertThat(refFromDb.referenceType.id).isEqualTo(1);
    }
    
    @Test
    public void deleteById() {
       Reference.find.byId(1001l).delete();
       Reference refFromDb = Reference.find.byId(1l);
       // can't find anymore
       assertThat(refFromDb).isNull();
    }
    
    @Test
    public void AddingFieldTypesToReferenceType() {
    	new models.FieldType("Required 1").save();
    	new models.FieldType("Required 2").save();
    	new models.FieldType("Optional 1").save();
    	new models.FieldType("Optional 2").save();
    	
    	String[] req = {"Required 1", "Required 2" };
    	String[] opt = {"Optional 1", "Optional 2"};

    	ReferenceType asd = new ReferenceType("test", req, opt);
    	asd.save();
    	
        ReferenceType ref = ReferenceType.find.where().eq("id", "1").findUnique();
        assertThat(ref.name).isEqualTo("inproceedings");
        assertThat(ref.requiredFields.get(0).fieldName).isEqualTo("Required 2");
        //assertThat(ref.optionalFields.get(0).fieldName).isEqualTo("Optional 1");
    }
    
}
