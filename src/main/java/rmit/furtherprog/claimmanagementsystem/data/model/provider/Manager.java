package rmit.furtherprog.claimmanagementsystem.data.model.provider;

import java.util.HashSet;
import java.util.Set;

public class Manager {
    private String id;
    private String name;
    private Set<Surveyor> surveyors;

    public Manager(String id, String name) {
        this.id = id;
        this.name = name;
        this.surveyors = new HashSet<Surveyor>();
    }

    public Manager(String id, String name, Set<Surveyor> surveyors) {
        this.id = id;
        this.name = name;
        this.surveyors = surveyors;
    }
    
    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Surveyor> getSurveyors() {
        return surveyors;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSurveyors(Set<Surveyor> surveyors) {
        this.surveyors = surveyors;
    }
    
    public void addSurveyor(Surveyor surveyor) {
        this.surveyors.add(surveyor);
    }
    
    public void removeSurveyor(Surveyor surveyor) {
        this.surveyors.remove(surveyor);
    }
}
