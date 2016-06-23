package fili.alex.newsradiot.controls;

public class ControlItem {
    private String name;

    @Control
    private int type;

    public ControlItem(String name, @Control int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @Control
    public int getType() {
        return type;
    }
}
