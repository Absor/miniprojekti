package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class FieldType extends Model {
	
	@Id
	public String fieldName;
	
	public static Finder<String, FieldType> find = new Finder<String, FieldType>(String.class, FieldType.class);
	
	public FieldType(String fieldName)
	{
		this.fieldName = fieldName;
	}

}
