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
 * Model for references. A reference has various datafields and a reference type
 * that knows which fields are required and which optional.
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

	public static Finder<Long, Reference> find = new Finder<Long, Reference>(
			Long.class, Reference.class);

	public static List<Reference> findSortedAndOrdered(String sortByField,
			String order, String searchField, String searchString) {
		return Reference.find.fetch("referenceType").where()
				.ilike(searchField, "%" + searchString + "%")
				.orderBy(sortByField + " " + order).findList();
	}

	public Map<String, String> getFields() {
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

	// Custom form validation
	public Map<String, List<ValidationError>> validate() {
		LinkedHashMap<String, List<ValidationError>> errors = new LinkedHashMap<String, List<ValidationError>>();
		
		if (referenceType == null) {
			errors.put("referenceType", errorEntry("referenceType", "Type can't be empty."));
			return errors;
		}

		// errors of reference id
		errors.putAll(checkReferenceId());

		// errors if any of the required fields missing
		errors.putAll(checkRequiredFields());

		if (errors.isEmpty()) {
			return null;
		}
		return errors;
	}

	private List<ValidationError> errorEntry(String errorField, String errorText) {
		List<ValidationError> list = new ArrayList<ValidationError>();
		list.add(new ValidationError(errorField, errorText, null));
		return list;
	}

	private Map<String, List<ValidationError>> checkReferenceId() {
		LinkedHashMap<String, List<ValidationError>> errors = new LinkedHashMap<String, List<ValidationError>>();

		// empty is valid because it means user wants to generate id
		if (referenceId == null || referenceId.isEmpty()) {
			return errors;
		}

		if (!isReferenceIdUnique()) {
			errors.put(
					"referenceId",
					errorEntry("referenceId",
							"ID already in use. Leave the field empty to generate a random ID."));
			return errors;
		}

		// don't allow only numbers
		if (referenceId.matches("[0-9]+")) {
			errors.put(
					"referenceId",
					errorEntry(
							"referenceId",
							"ID can't consist only of numbers. Leave the field empty to generate a random ID."));
			return errors;
		}

		return errors;
	}

	private boolean isReferenceIdUnique() {
		Reference search = find.where().eq("referenceId", referenceId)
				.findUnique();
		if (search != null && search.id != id) {
			return false;
		}
		return true;
	}

	private Map<String, List<ValidationError>> checkRequiredFields() {
		LinkedHashMap<String, List<ValidationError>> errors = new LinkedHashMap<String, List<ValidationError>>();

		Map<String, String> allFields = getFields();
		for (FieldType fieldType : referenceType.requiredFields) {
			String field = allFields.get(fieldType.fieldName);
			if (field == null || field.isEmpty()) {
				errors.put(fieldType.fieldName,
						errorEntry(fieldType.fieldName, "Field required."));
			}
		}
		return errors;
	}

	@Override
	public void save() {
		super.save();
		if (referenceId == null || referenceId.isEmpty()) {
			generateReferenceId();
			super.save();
		}
	}

	private void generateReferenceId() {
		// If author and year are defined, generate ID from them using the
		// first letter of surname and last two digits of the year
		if (author != null && year != null && !author.isEmpty()
				&& !year.isEmpty()) {
			if (year.length() >= 4)
				referenceId = author.charAt(0) + year.substring(0, 4);
			else
				referenceId = author.charAt(0) + year;
		}
		// Otherwise use the database generated id
		else {
			referenceId = Long.toString(this.id);
		}

		// If the generated id isn't unique, add a numeric suffix to make it
		// unique
		if (!isReferenceIdUnique()) {
			int suffix = 1;
			String originalId = referenceId;
			while (!isReferenceIdUnique()) {
				referenceId = originalId + "-" + suffix;
				suffix++;
			}
		}
	}
}
