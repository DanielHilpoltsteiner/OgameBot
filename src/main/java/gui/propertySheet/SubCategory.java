package gui.propertySheet;

import tools.Cache;

/**
 *
 */
public class SubCategory implements Comparable<SubCategory> {
    private static Cache<String, SubCategory> cache = new Cache<>();
    private String subCategory;
    private Category category;
    private boolean invisible;
    private int counter = 0;

    private SubCategory(String subCategory, Category category) {
        this.subCategory = subCategory;
        this.category = category;
        this.invisible = subCategory == null || subCategory.isEmpty();
        counter++;
    }

    public static SubCategory createSubCategory(String subCategory, String category) {
        return cache.checkCache(new SubCategory(subCategory, new Category(category, false)), SubCategory::getSubCategory);
    }

    String getSubCategory() {
        return subCategory;
    }

    Category getCategory() {
        return category;
    }

    void setCategory(Category category) {
        this.category = category;
    }

    boolean isInvisible() {
        return invisible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubCategory that = (SubCategory) o;

        if (getSubCategory() != null ? !getSubCategory().equals(that.getSubCategory()) : that.getSubCategory() != null)
            return false;
        return getCategory() != null ? getCategory().equals(that.getCategory()) : that.getCategory() == null;
    }

    @Override
    public int hashCode() {
        int result = getSubCategory() != null ? getSubCategory().hashCode() : 0;
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(SubCategory o) {
        if (o == null) {
            return -1;
        } else if (o == this) {
            return 0;
        }

        int compared = getCategory().compareTo(o.getCategory());
        if (compared == 0) {
            compared = getSubCategory().compareTo(o.subCategory);
        }
        return compared;
    }

    @Override
    public String toString() {
        return "SubCategory{" +
                "subCategory='" + subCategory + '\'' +
                ", category=" + category +
                ", invisible=" + invisible +
                ", counter=" + counter +
                '}';
    }
}
