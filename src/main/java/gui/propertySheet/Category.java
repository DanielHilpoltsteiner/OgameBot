package gui.propertySheet;

/**
 *
 */
class Category implements Comparable<Category> {
    private String name;
    private boolean invisible;

    Category(String name, boolean invisible) {
        this.name = name;
        this.invisible = invisible;
    }

    String getName() {
        return name;
    }

    boolean isInvisible() {
        return invisible;
    }

    public void setInvisible() {
        this.invisible = true;
    }

    @Override
    public int compareTo(Category o) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (isInvisible() != category.isInvisible()) return false;
        return getName() != null ? getName().equals(category.getName()) : category.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (isInvisible() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", invisible=" + invisible +
                '}';
    }
}
