package fili.alex.newsradiot.controls;

public class FilterItem extends ControlItem {
    @Filter
    private int filter;

    public FilterItem(String name, @Filter int filter) {
        super(name, Control.FILTER);

        this.filter = filter;
    }

    @Filter
    public int getFilter() {
        return filter;
    }
}
