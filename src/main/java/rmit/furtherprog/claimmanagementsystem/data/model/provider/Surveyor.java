package rmit.furtherprog.claimmanagementsystem.data.model.provider;

import rmit.furtherprog.claimmanagementsystem.data.model.customer.Customer;

import java.util.Objects;

public class Surveyor {
    private String id;
    private String name;

    public Surveyor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public String getId() {
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
