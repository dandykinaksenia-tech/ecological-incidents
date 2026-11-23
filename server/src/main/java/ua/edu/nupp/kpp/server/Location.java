package ua.edu.nupp.kpp.server;

public class Location {
    private Long id;
    private String name;
    private String region;
    private String address;
    private Double latitude;
    private Double longitude;
    private String description;

    public Location() {}
    public Location(Long id, String name, String region, String address,
                    Double latitude, Double longitude, String description) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}



