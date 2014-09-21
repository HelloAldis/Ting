
package com.kindazrael.tingweather.model;

public class WeatherLocation {

    public String areaId;
    public String province;
    public String city;
    public String district;
    public String spell;

    public int sortOrder;

    @ Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((areaId == null) ? 0 : areaId.hashCode());
        return result;
    }

    @ Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WeatherLocation other = (WeatherLocation) obj;
        if (areaId == null) {
            if (other.areaId != null)
                return false;
        } else if (!areaId.equals(other.areaId))
            return false;
        return true;
    }

}
