package application;

import java.util.Objects;

public class CountryCode {
    private String country;
    private Integer code;

    public CountryCode(String country, Integer code) {
        this.country = country;
        this.code = code;
    }

    public CountryCode() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryCode that = (CountryCode) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
