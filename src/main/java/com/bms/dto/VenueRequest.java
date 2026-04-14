package com.bms.dto;

import com.bms.entity_enums.VenueLayoutType;
import java.util.List;

public class VenueRequest {

    private String name;
    private String location;
    private Integer rows;
    private Integer seatsPerRow;
    private VenueLayoutType layoutType;
    private List<SectionRequest> sections; // For stadium/arena with named sections

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getRows() { return rows; }
    public void setRows(Integer rows) { this.rows = rows; }

    public Integer getSeatsPerRow() { return seatsPerRow; }
    public void setSeatsPerRow(Integer seatsPerRow) { this.seatsPerRow = seatsPerRow; }

    public VenueLayoutType getLayoutType() { return layoutType; }
    public void setLayoutType(VenueLayoutType layoutType) { this.layoutType = layoutType; }

    public List<SectionRequest> getSections() { return sections; }
    public void setSections(List<SectionRequest> sections) { this.sections = sections; }

    public static class SectionRequest {
        private String name;
        private int rows;
        private int seatsPerRow;
        private com.bms.entity_enums.SeatType seatType;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getRows() { return rows; }
        public void setRows(int rows) { this.rows = rows; }

        public int getSeatsPerRow() { return seatsPerRow; }
        public void setSeatsPerRow(int seatsPerRow) { this.seatsPerRow = seatsPerRow; }

        public com.bms.entity_enums.SeatType getSeatType() { return seatType; }
        public void setSeatType(com.bms.entity_enums.SeatType seatType) { this.seatType = seatType; }
    }
}