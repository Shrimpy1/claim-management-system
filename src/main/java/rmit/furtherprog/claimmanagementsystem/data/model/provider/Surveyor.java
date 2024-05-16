/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.data.model.provider;

import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;

import java.util.List;
import java.util.Objects;

public class Surveyor {
    private int id;
    private String name;
    private List<Claim> proposedClaim;

    public Surveyor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Surveyor(int id, String name, List<Claim> proposedClaim) {
        this.id = id;
        this.name = name;
        this.proposedClaim = proposedClaim;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Claim> getProposedClaim() {
        return proposedClaim;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setProposedClaim(List<Claim> proposedClaim) {
        this.proposedClaim = proposedClaim;
    }

    public void proposeClaim(Claim claim){
        this.proposedClaim.add(claim);
    }

    public void removeProposedClaim(Claim claim){
        this.proposedClaim.remove(claim);
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
