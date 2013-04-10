import java.util.List;
import java.util.Map;

import models.ReferenceType;

import com.avaje.ebean.Ebean;

import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;


public class Global extends GlobalSettings {
	
	public void onStart(Application app) {
		InitialData.insert(app);
	}
	
	static class InitialData {
        
        public static void insert(Application app) {
        	
        	// Are we executing the program for the first time?
            if(Ebean.find(ReferenceType.class).findRowCount() == 0) {
                
                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");

                // Insert all field and reference types first
                Ebean.save(all.get("fieldTypes"));
                Ebean.save(all.get("referenceTypes"));
                
                for(Object project: all.get("referenceTypes")) {
                    // Insert the referenceType/fieldType relations
                    Ebean.saveManyToManyAssociations(project, "requiredFields");
                    Ebean.saveManyToManyAssociations(project, "optionalFields");
                }
            }
        }
        
    }
}
