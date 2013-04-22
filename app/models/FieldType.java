package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Constraint;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

/**
 * Model for field types. A field type has only its name and new field types
 * shouldn't need to be created outside initial-data.yml.
 * 
 */
@Entity
public class FieldType extends Model {

	@Id
	public String fieldName;

	public static Finder<String, FieldType> find = new Finder<String, FieldType>(
			String.class, FieldType.class);

	public FieldType(String fieldName) {
		this.fieldName = fieldName;
	}

	public static boolean fieldNameIsValid(String fieldName) {
		List<FieldType> fields = FieldType.find.all();
		List<String> fieldNames = new ArrayList<String>();
		for (FieldType field : fields) {
			fieldNames.add(field.fieldName);
		}
		fieldNames.add("id");

		if (fieldNames.contains(fieldName)) {
			return true;
		}
		return false;
	}

}
