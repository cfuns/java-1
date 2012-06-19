package de.benjaminborbe.lunch.bean;

import java.util.Date;

import de.benjaminborbe.lunch.api.Lunch;

public class LunchBean implements Lunch {

	private Date date;

	private String name;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
