package mandrik.nick.lab_46.db.data;

public interface DBConstants {
    // Common DB
    String DATABASE_NAME = "catalog.db";
    //  Region table constants
    String REGION_TBL = "REGIONS";
    String REGION_REG_NAME = "REGION_NAME";
    String REGION_ID = "REGION_ID";

    //  District table constants
    String DISTRICT_TBL = "DISTRICTS";
    String DISTRICT_ID = "DISTRICT_ID";
    String DISTRICT_D_NAME = "DISTRICT_NAME";
    String DISTRICT_D_POPULATION = "DISTRICT_POPULATION";
    String DISTRICT_REGION_ID = "DISTRICT_REGION_ID";
    String DISTRICT_CODE = "DISTRICT_CODE";
}
