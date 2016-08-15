package fili.alex.newsradiot.model;


public class SortingItem extends ControlItem {
    @Sorting
    private int sorting;

    public SortingItem(String name,@Sorting  int sorting) {
        super(name, Control.SORTING);

        this.sorting = sorting;
    }

    @Sorting
    public int getSorting() {
        return sorting;
    }
}
