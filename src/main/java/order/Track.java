package order;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Track {
    private  String track;

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
