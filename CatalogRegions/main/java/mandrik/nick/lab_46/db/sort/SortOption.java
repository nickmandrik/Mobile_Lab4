package mandrik.nick.lab_46.db.sort;

/**
 * @author Ihar Zharykau
 */
public enum SortOption {
    ASC, DESC;

    public SortOption revert(){
        switch (this){
            case ASC:
                return DESC;
            case DESC:
                return ASC;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this){
            case ASC:
                return "ASC";
            case DESC:
                return "DESC";
            default:
                return "";
        }
    }
}
