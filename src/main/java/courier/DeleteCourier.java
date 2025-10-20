package courier;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeleteCourier {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
