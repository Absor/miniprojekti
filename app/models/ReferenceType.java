package models;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model.Finder;

@Entity
public class ReferenceType {

	@Id
	public Long id;
	
	public String name;
	
	public static Finder<Long, ReferenceType> find = new Finder<Long, ReferenceType>(
			Long.class, ReferenceType.class);
	
	/*
	 *  Options for form "id - name" map of all types.
	 */
	public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(ReferenceType r: ReferenceType.find.orderBy("name").findList()) {
            options.put(r.id.toString(), r.name);
        }
        return options;
    }
}
