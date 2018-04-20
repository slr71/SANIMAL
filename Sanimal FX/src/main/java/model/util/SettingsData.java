package model.util;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.controlsfx.control.PropertySheet;
import org.irods.jargon.core.pub.domain.Zone;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Class containing sanimal settings
 */
public class SettingsData
{
	// A list of settings SANIMAL uses
	private transient ObservableList<CustomPropertyItem<?>> settingList = FXCollections.observableArrayList(item -> new Observable[] { item.getObservableValue().get() });

	// The current setting value
	private ObjectProperty<DateFormat> dateFormat = new SimpleObjectProperty<>(DateFormat.MonthDayYear);
	private ObjectProperty<TimeFormat> timeFormat = new SimpleObjectProperty<>(TimeFormat.Time24Hour);
	private ObjectProperty<LocationFormat> locationFormat = new SimpleObjectProperty<>(LocationFormat.LatLong);
	private ObjectProperty<DistanceUnits> distanceUnits = new SimpleObjectProperty<>(DistanceUnits.Meters);
	private BooleanProperty drSandersonCompatibility = new SimpleBooleanProperty(false);
	private BooleanProperty automaticNextImage = new SimpleBooleanProperty(false);

	/**
	 * Constructor adds all settings SANIMAL will use to the dictionary
	 */
	public SettingsData()
	{
		this.setupPropertyPageItems();
	}

	public void loadFromOther(SettingsData otherSettings)
	{
		this.dateFormat.setValue(otherSettings.getDateFormat());
		this.timeFormat.setValue(otherSettings.getTimeFormat());
		this.locationFormat.setValue(otherSettings.getLocationFormat());
		this.distanceUnits.setValue(otherSettings.getDistanceUnits());
		this.drSandersonCompatibility.setValue(otherSettings.getDrSandersonCompatibility());
		this.automaticNextImage.setValue(otherSettings.getAutomaticNextImage());
	}

	private void setupPropertyPageItems()
	{
		settingList.add(new CustomPropertyItem<>("Date Format: ", "DateTime", "The date format to be used when displaying dates", dateFormat, DateFormat.class));
		settingList.add(new CustomPropertyItem<>("Time Format: ", "DateTime", "The time format to be used when displaying dates", timeFormat, TimeFormat.class));
		settingList.add(new CustomPropertyItem<>("Location Format: ", "Location", "The location format to be used when displaying positional information", locationFormat, LocationFormat.class));
		settingList.add(new CustomPropertyItem<>("Distance Units: ", "Units", "The units to be used by the program", distanceUnits, DistanceUnits.class));
		settingList.add(new CustomPropertyItem<>("Dr. Sanderson's Format Compatibility: ", "Options", "Gives the option to read a directory in Dr. Jim Sanderson's format and automatically tag it", drSandersonCompatibility, Boolean.class));
		settingList.add(new CustomPropertyItem<>("Automatically Select Next Image: ", "Options", "Automatically select the next image after tagging one with species", automaticNextImage, Boolean.class));
	}

	/**
	 * Utility method used to format a date with the proper format
	 *
	 * @param date The date to format
	 * @return A string representing the date in the proper format
	 */
	public String formatDate(LocalDate date)
	{
		return this.dateFormat.getValue().format(date);
	}

	/**
	 * Utility method used to format a time with the proper format
	 *
	 * @param time The time to format
	 * @return A string representing the time in the proper format
	 */
	public String formatTime(LocalTime time)
	{
		return this.timeFormat.getValue().format(time);
	}

	/**
	 * Utility method used to format a date & time with the proper format
	 *
	 * @param dateTime The date & time to format
	 * @param delimeter An optional delimeter put between date and time
	 * @return A string representing the date & time in the proper format
	 */
	public String formatDateTime(LocalDateTime dateTime, String delimeter)
	{
		return this.dateFormat.getValue().format(dateTime.toLocalDate()) + delimeter + this.timeFormat.getValue().format(dateTime.toLocalTime());
	}

	public enum DateFormat
	{
		MonthDayYear("Month Day, Year -- January 3, 2011",        "MMMM dd',' yyyy"),
		ShortMonthDayYear("Short Month Day, Year -- Jan 3, 2011", "MMM dd',' yyyy"),
		NumericMonthDayYear("Numeric Month/Day/Year -- 1/3/2011", "M'/'d'/'yyyy"),
		DayMonthYear("Day Month, Year -- 3. January 2011",        "dd'.' MMMM yyyy"),
		ShortDayMonthYear("Day Short Month -- 3. Jan 2011",       "dd'.' MMM yyyy"),
		NumericDayMonthYear("Numeric Day/Month/Year -- 3/1/2011", "d'/'M'/'yyyy"),
		ISO("ISO Local Date -- 2011-1-3",                         DateTimeFormatter.ISO_LOCAL_DATE);

		private String stringValue;
		private DateTimeFormatter formatter;

		DateFormat(String stringValue, String format)
		{
			this.stringValue = stringValue;
			this.formatter = DateTimeFormatter.ofPattern(format);
		}

		DateFormat(String stringValue, DateTimeFormatter formater)
		{
			this.stringValue = stringValue;
			this.formatter = formater;
		}

		@Override
		public String toString()
		{
			return this.stringValue;
		}

		public String format(LocalDate date)
		{
			return date.format(formatter);
		}

		public DateTimeFormatter getFormatter() { return this.formatter; }
	}

	public enum TimeFormat
	{
		Time24Hour("24 hour -- 14:36",                                        "H:mm"),
		Time24HourSeconds("24 hour with seconds -- 14:36:52",                 "H:mm:ss"),
		Time12HourAMPM("12 hour with AM/PM -- 2:36 pm",                       "h:mm a"),
		Time12HourSecondsAMPM("12 hour with AM/PM and seconds -- 2:36:52 pm", "h:mm:ss a");

		private String stringValue;
		private DateTimeFormatter formatter;

		TimeFormat(String stringValue, String format)
		{
			this.stringValue = stringValue;
			this.formatter = DateTimeFormatter.ofPattern(format);
		}

		@Override
		public String toString()
		{
			return this.stringValue;
		}

		public String format(LocalTime time)
		{
			return time.format(formatter);
		}

		public DateTimeFormatter getFormatter() { return this.formatter; }
	}

	public enum LocationFormat
	{
		LatLong("Latitude & Longitude"),
		UTM("UTM");

		private String stringValue;

		LocationFormat(String stringValue)
		{
			this.stringValue = stringValue;
		}

		@Override
		public String toString()
		{
			return this.stringValue;
		}
	}

	public enum DistanceUnits
	{
		Feet,
		Meters;
	}

	public ObservableList<CustomPropertyItem<?>> getSettingList()
	{
		return this.settingList;
	}

	public void setDateFormat(DateFormat dateFormat)
	{
		this.dateFormat.set(dateFormat);
	}

	public DateFormat getDateFormat()
	{
		return dateFormat.get();
	}

	public ObjectProperty<DateFormat> dateFormatProperty()
	{
		return dateFormat;
	}

	public void setTimeFormat(TimeFormat timeFormat)
	{
		this.timeFormat.set(timeFormat);
	}

	public TimeFormat getTimeFormat()
	{
		return timeFormat.get();
	}

	public ObjectProperty<TimeFormat> timeFormatProperty()
	{
		return timeFormat;
	}

	public void setLocationFormat(LocationFormat locationFormat)
	{
		this.locationFormat.set(locationFormat);
	}

	public LocationFormat getLocationFormat()
	{
		return locationFormat.get();
	}

	public ObjectProperty<LocationFormat> locationFormatProperty()
	{
		return locationFormat;
	}

	public void setDistanceUnits(DistanceUnits distanceUnits)
	{
		this.distanceUnits.set(distanceUnits);
	}

	public DistanceUnits getDistanceUnits()
	{
		return distanceUnits.get();
	}

	public ObjectProperty<DistanceUnits> distanceUnitsProperty()
	{
		return distanceUnits;
	}

	public void setDrSandersonCompatibility(boolean drSandersonCompatibility)
	{
		this.drSandersonCompatibility.set(drSandersonCompatibility);
	}

	public boolean getDrSandersonCompatibility()
	{
		return this.drSandersonCompatibility.get();
	}

	public BooleanProperty drSandersonCompatibilityProperty()
	{
		return drSandersonCompatibility;
	}

	public void setAutomaticNextImage(boolean automaticNextImage)
	{
		this.automaticNextImage.set(automaticNextImage);
	}

	public boolean getAutomaticNextImage()
	{
		return automaticNextImage.get();
	}

	public BooleanProperty automaticNextImageProperty()
	{
		return automaticNextImage;
	}
}