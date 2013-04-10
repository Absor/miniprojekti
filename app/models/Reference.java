package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
	public ReferenceType referenceType;

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

	public static Finder<Long, Reference> find = new Finder<Long, Reference>(Long.class, Reference.class);

	/*
	 * BibTeX-format string.
	 */
	@Override
	public String toString() {
		return Bibtex.generate(referenceType.name, id, this.getMap());
	}
	
	public Map<String, String> getMap() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
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
		Map <String, String> fields = this.getMap();
		for (FieldType fieldType : referenceType.requiredFields) {
			String field = fields.get(fieldType.fieldName);
			if (field == null || field.isEmpty()) {
				errors.add(fieldType.fieldName);
			}
		}
		return errors;
	}
}
