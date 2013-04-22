package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.validation.NotNull;

import play.data.validation.ValidationError;
import play.db.ebean.Model;


/**
 * Model for references. A reference has various datafields and a reference type that knows which fields are
 * required and which optional.
 *
 */
@Entity
public class Reference extends Model {
	
	@Id
	public Long id;

	@ManyToOne
	@NotNull
	public ReferenceType referenceType;
	
	@Column(unique = true)
	public String referenceId;

	public String title;
	
	public String author;
	
	public String booktitle;

	public String year;

	public String editor;
	
	public String volume;
	
	public String series;
	
	public String pages;
	
	public String address;
	
	public String month;
	
	public String organization;
	
	public String publisher;
	
	public String note;
	
	public String key;
	
	public String journal;
	
	public String howpublished;
	
	public String edition;
	
	public Reference() {
	}

	public static Finder<Long, Reference> find = new Finder<Long, Reference>(Long.class, Reference.class);
	
	public static List<Reference> findSortedAndOrdered(String sortByField, String order, String searchField, String searchString) {
		return Reference.find.fetch("referenceType").where().ilike(searchField, "%" + searchString + "%").orderBy(sortByField + " " + order).findList();
	}

	public Map<String, String> getVariables() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("referenceId", referenceId);
		map.put("title", title);
		map.put("author", author);
		map.put("booktitle", booktitle);
		map.put("year", year);
		map.put("editor", editor);
		map.put("volume", volume);
		map.put("series", series);
		map.put("pages", pages);
		map.put("address", address);
		map.put("month", month);
		map.put("organization", organization);
		map.put("publisher", publisher);
		map.put("note", note);
		map.put("key", key);
		map.put("journal", journal);
		map.put("howpublished", howpublished);
		map.put("edition", edition);
		return map;
	}

	public List<String> checkRequired() {
		ArrayList<String> errors = new ArrayList<String>();
		Map <String, String> fields = this.getVariables();
		for (FieldType fieldType : referenceType.requiredFields) {
			String field = fields.get(fieldType.fieldName);
			if (field == null || field.isEmpty()) {
				errors.add(fieldType.fieldName);
			}
		}
		return errors;
	}

	public void generateReferenceId() {
		if (referenceId != null && !referenceId.isEmpty())
			return;

		// If author and year are defined, generate ID from them using the 
		// first letter of surname and last two digits of the year
		if (author != null && year != null && !author.isEmpty() && !year.isEmpty()) {
			if (year.length() >= 4)
				referenceId = author.charAt(0) + year.substring(2);
			else
				referenceId = author.charAt(0) + year;
		}
		// Otherwise use the database generated id
		else {
			referenceId = Long.toString(this.id);
		}
		
		// If the generated id isn't unique, add a numeric suffix to make it unique
		if (!isReferenceIdUnique(referenceId)) {
			int suffix = 1;
			while(!isReferenceIdUnique(referenceId + "-" + suffix))
				suffix++;
			referenceId += "-" + suffix;
		}
		save();
	}

	public static boolean isReferenceIdUnique(String referenceId) {
		if (referenceId == null || referenceId.isEmpty())
			return true;
		
		Reference search = find.where().eq("referenceId", referenceId).findUnique();
		if (search == null)
			return true;
		else 
			return false;
	}
	
	public static boolean isUpdatedReferenceIdUnique(Long id, String referenceId) {
		Reference updated = Reference.find.byId(id);

		if (updated.referenceId.equals(referenceId))
			return true;
		
		return isReferenceIdUnique(referenceId);
	}
	
	// TODO change to use validation like this (required fields AND referenceID)
	public Map<String,List<ValidationError>> validate() {
//		List<ValidationError> list = new ArrayList<ValidationError>();
//		list.add(new ValidationError("author", "wrong author", null));
//		LinkedHashMap<String, List<ValidationError>> map = new LinkedHashMap<String, List<ValidationError>>();
//		map.put("author", list);
//		return map;
		return null;
	}
}
