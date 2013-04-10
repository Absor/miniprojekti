package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;


/**
 * Model for reference types. A reference type has its name and lists of required and optional
 * fields. New reference types shouldn't need to be created outside initial-data.yml.
 *
 */
@Entity
public class ReferenceType extends Model {

	@Id
	public Long id;
	
	public String name;
	
	// JoinTable names has to be stated explicitly for both lists for them to be separate tables
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "RequiredFields")
	public List<FieldType> requiredFields = new ArrayList<FieldType>();
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "OptionalFields")
	public List<FieldType> optionalFields = new ArrayList<FieldType>();
	
	public ReferenceType(String name, String[] required, String[] optional) {
		this.name = name;
		
		for (String field : required) {
			requiredFields.add(FieldType.find.ref(field));
		}
		for (String field : optional) {
			optionalFields.add(FieldType.find.ref(field));
		}
	}
	
	/**
	 * Save method overridden to always save many to many associations
	 */
	public void save() {
		super.save();
		this.saveManyToManyAssociations("requiredFields");
		this.saveManyToManyAssociations("optionalFields");
	}
	
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
