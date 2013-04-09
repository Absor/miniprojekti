package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ReferenceType extends Model{

	@Id
	public Long id;
	
	public String name;
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "RequiredFields")
	public List<FieldType> requiredFields = new ArrayList<FieldType>();
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "OptionalFields")
	public List<FieldType> optionalFields = new ArrayList<FieldType>();
	
	public ReferenceType(Long id, String name, String[] required, String[] optional) {
		this.id = id;
		this.name = name;
		
		for (String field : required) {
			requiredFields.add(FieldType.find.ref(field));
		}
		for (String field : optional) {
			optionalFields.add(FieldType.find.ref(field));
		}
	}
	
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
