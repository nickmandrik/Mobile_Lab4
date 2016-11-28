package mandrik.nick.lab_46.db.data;


public interface InitialData extends DBConstants {
    String SQL_CREATE_REGIONS =
            "CREATE TABLE IF NOT EXISTS " + REGION_TBL + "( " + REGION_ID + " integer primary key autoincrement, " +
                    REGION_REG_NAME + " string);";
    String SQL_CREATE_DISTRICT =

                    "CREATE TABLE IF NOT EXISTS " + DISTRICT_TBL + "(" +
                    DISTRICT_ID + " integer primary key autoincrement, " + DISTRICT_D_NAME + " string" +
                    ", " + DISTRICT_D_POPULATION + " integer, " + DISTRICT_REGION_ID + " integer, " + DISTRICT_CODE +
                    " integer, FOREIGN KEY (" + DISTRICT_REGION_ID +
                    " ) references " + REGION_TBL + " ( " + REGION_ID + " ));";
    String SQL_DELETE_DISTRICTS =
            "DROP TABLE " + DISTRICT_TBL + " ;";

    String SQL_DELETE_REGION = "DROP TABLE " + REGION_TBL + ";";


    class District{
        public String name;
        public int population;
        public int regionId;
        public int code;

        public District(String name, int population, int regionId, int code) {
            this.name = name;
            this.population = population;
            this.regionId = regionId;
            this.code = code;
        }

        @Override
        public String toString() {
            return "Район :" +
                    " " + name  +
                    "\nНаселение : " + population +
                    "\nКод области : " + regionId +
                    "\nТелефонный код : " + code;
        }
    }

}
