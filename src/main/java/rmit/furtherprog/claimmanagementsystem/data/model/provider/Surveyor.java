/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.data.model.provider;

import java.util.Objects;

public class Surveyor {
    private int id;
    private String name;

    public Surveyor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Surveyor)) {
            return false;
        }
        return Objects.equals(this.getId(), ((Surveyor) o).getId());
    }
}
