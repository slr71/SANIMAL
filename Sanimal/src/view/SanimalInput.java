package view;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;

import model.Location;
import model.Species;
import model.UTMCoord;
import model.analysis.SanimalAnalysisUtils;

/**
 * A class of utility methods that perform user input
 * 
 * @author David Slovikosky
 */
public class SanimalInput
{
	private static final Character[] INVALID_UTM_LETTERS = new Character[]
	{ 'A', 'B', 'I', 'O', 'Y', 'Z' };

	/**
	 * Asks the user to input a new species.
	 * 
	 * @return The species entered or "null" if nothing was entered
	 */
	public static Species askUserForNewSpecies()
	{
		String name = "";
		while (name.isEmpty())
		{
			name = JOptionPane.showInputDialog("Enter the name of the new species");
			if (name == null)
				return null;
		}
		return new Species(name);
	}

	/**
	 * Asks the user to input a new location.
	 * 
	 * @return The new location or "null" if nothing was entered
	 */
	public static Location askUserForNewLocation()
	{
		// Input the name
		String name = "";
		while (name.isEmpty())
		{
			name = JOptionPane.showInputDialog("Enter the name of the new location");
			if (name == null)
				return null;
		}

		// Input lat/lng or UTM
		Integer result = JOptionPane.showOptionDialog(null, "Are the coordinates in UTM or Latitude/Longitude?", "Coordinate System Type", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]
		{ "Latitude/Longitude", "UTM" }, "Latitude/Longitude");
		if (result == JOptionPane.CLOSED_OPTION)
			return null;
		System.out.println(result);
		Boolean useLatLng = result == JOptionPane.YES_OPTION;

		Double latitude = Double.MAX_VALUE;
		Double longitude = Double.MAX_VALUE;

		// If we're using lat/lng, just input the coordinates
		if (useLatLng)
		{
			// Input that latitude
			while (latitude > 85 || latitude < -85)
			{
				try
				{
					String latitudeString = JOptionPane.showInputDialog("Enter the latitude (+/- 85) of location '" + name + "'");
					if (latitudeString == null)
						return null;
					latitude = Double.parseDouble(latitudeString);
				}
				catch (NumberFormatException exception)
				{
				}
			}
			// Input the longitude
			while (longitude > 180 || longitude < -180)
			{
				try
				{
					String longitudeString = JOptionPane.showInputDialog("Enter the longitude (+/- 180) of location '" + name + "'");
					if (longitudeString == null)
						return null;
					longitude = Double.parseDouble(longitudeString);
				}
				catch (NumberFormatException exception)
				{
				}
			}
		}
		// If we're using UTM, input the letter, zone, Northing, and Easting
		else
		{
			// Input the letter
			Character letter = Character.MAX_VALUE;
			while (!Character.isLetter(letter) || ArrayUtils.contains(INVALID_UTM_LETTERS, Character.toUpperCase(letter)))
			{
				String letterString = JOptionPane.showInputDialog("Enter UTM Letter (C to X excluding I and O) of location '" + name + "'");
				if (letterString == null)
					return null;
				if (letterString.length() == 1)
					letter = letterString.charAt(0);
			}

			// Input the zone
			Integer zone = Integer.MAX_VALUE;
			while (zone < 1 || zone > 60)
			{
				try
				{
					String zoneString = JOptionPane.showInputDialog("Enter the UTM zone (1 to 60) of location '" + name + "'");
					if (zoneString == null)
						return null;
					zone = Integer.parseInt(zoneString);
				}
				catch (NumberFormatException exception)
				{
				}
			}

			// Input the easting value
			Double easting = Double.MAX_VALUE;
			while (easting > 1000000 || easting < 0)
			{
				try
				{
					String eastingString = JOptionPane.showInputDialog("Enter the UTM easting (0m to 1000000m) of location '" + name + "'");
					if (eastingString == null)
						return null;
					easting = Double.parseDouble(eastingString);
				}
				catch (NumberFormatException exception)
				{
				}
			}

			// Input the northing value
			Double northing = Double.MAX_VALUE;
			while (northing > 10000000 || northing < 0)
			{
				try
				{
					String northingString = JOptionPane.showInputDialog("Enter the UTM northing (0m to 10000000m) of location '" + name + "'");
					if (northingString == null)
						return null;
					northing = Double.parseDouble(northingString);
				}
				catch (NumberFormatException exception)
				{
				}
			}

			UTMCoord utmCoord = new UTMCoord(easting, northing, zone, letter);
			Double[] latLng = SanimalAnalysisUtils.UTM2Deg(utmCoord);
			latitude = latLng[0];
			longitude = latLng[1];
		}

		// Input the elevation
		Double elevation = Double.MAX_VALUE;
		while (elevation == Double.MAX_VALUE)
		{
			try
			{
				String elevationString = JOptionPane.showInputDialog("Enter the elevation (in feet) of location '" + name + "'");
				if (elevationString == null)
					return null;
				elevation = Double.parseDouble(elevationString);
			}
			catch (NumberFormatException exception)
			{
			}
		}
		// Return the location
		return new Location(name, latitude, longitude, elevation);
	}
}
