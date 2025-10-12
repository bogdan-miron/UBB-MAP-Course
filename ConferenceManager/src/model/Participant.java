package model;

public abstract class Participant {
    protected int id;
    protected String name;
    protected String title; // Student at UBB, Researcher at Bitdefender etc.
    protected Boolean hasPresented;

    public Participant(int id, String name, String title, Boolean hasPresented) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.hasPresented = hasPresented;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getHasPresented() {
        return hasPresented;
    }

    public void setHasPresented(Boolean hasPresented) {
        this.hasPresented = hasPresented;
    }

    public abstract void displayInfo();
}
