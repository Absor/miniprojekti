import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.xml.FieldType;
import org.junit.*;

import play.libs.Yaml;

import com.avaje.ebean.Ebean;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import models.*;


public class ModelTest {
	
	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
        
        // Loading test references
        Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("test-data.yml");
        Ebean.save(all.get("references"));
    }
	
    @Test
    public void findById() {
       Reference refFromDb = Reference.find.byId(1L);
       assertThat(refFromDb.title).isEqualTo("testtit1");
       assertThat(refFromDb.referenceType.id).isEqualTo(1L);
    }
    
    @Test
    public void deleteById() {
       Reference.find.byId(1L).delete();
       Reference refFromDb = Reference.find.byId(1L);
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
    	
        ReferenceType ref = ReferenceType.find.where().eq("name", "test").findUnique();
        assertThat(ref.name).isEqualTo("test");
        assertThat(ref.requiredFields.get(1).fieldName).isEqualTo("Required 2");
        assertThat(ref.optionalFields.get(0).fieldName).isEqualTo("Optional 1");
    }
    
    // Testing referece ID generation
    
    @Test
    public void ReferenceIdGeneratedFromAuthorAndYear() {
    	Reference ref = new Reference();
    	ref.referenceType = ReferenceType.find.where().eq("name", "misc").findUnique();
    	ref.author = "Luukkainen, Matti";
    	ref.year = "2009";
    	ref.save();
    	assertThat(ref.referenceId).isEqualTo("L2009");
    }
    
    @Test
    public void ReferenceIdGenerationAddsSuffix() {
    	Reference ref = new Reference();
    	ref.referenceType = ReferenceType.find.where().eq("name", "misc").findUnique();
    	ref.author = "Luukkainen, Matti";
    	ref.year = "2009";
    	ref.save();
    	
    	ref = new Reference();
    	ref.referenceType = ReferenceType.find.where().eq("name", "misc").findUnique();
    	ref.author = "Luukkainen, Matti";
    	ref.year = "2009";
    	ref.save();
    	
    	assertThat(ref.referenceId).isEqualTo("L2009-1");
    	
    	ref = new Reference();
    	ref.referenceType = ReferenceType.find.where().eq("name", "misc").findUnique();
    	ref.author = "Luukkainen, Matti";
    	ref.year = "2009";
    	ref.save();
    	
    	assertThat(ref.referenceId).isEqualTo("L2009-2");
    }
    
    @Test
    public void ReferenceIdIsDatabaseIdWithoutAuthorAndYear() {
    	Reference ref = new Reference();
    	ref.referenceType = ReferenceType.find.where().eq("name", "misc").findUnique();
    	ref.author = "Luukkainen, Matti";
    	ref.save();
    	String id = Long.toString(ref.id);
    	assertThat(ref.referenceId).isEqualTo(id);
    }
}
