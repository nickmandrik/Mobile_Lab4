package mandrik.nick.lab_46;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import mandrik.nick.lab_46.db.DBHelper;
import mandrik.nick.lab_46.db.data.InitialData;

/**
 * @author Ihar Zharykau
 */
public class FilterActivity extends AppCompatActivity {
    private Spinner spinner;
    private DBHelper dbHelper;
    private EditText minPopulationEditText;
    private TableLayout tbl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_layout);
        dbHelper = new DBHelper(this);

        spinner = (Spinner) findViewById(R.id.region_chooser);
        tbl = (TableLayout) findViewById(R.id.db_view);
        minPopulationEditText = (EditText) findViewById(R.id.min_pop_edit_text);
        List<String> source = dbHelper.allRegions();
        source.add(getResources().getString(R.string.filter_region_none));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, source);
        spinner.setAdapter(adapter);
    }

    public void filter(View v) {
        List<InitialData.District> list = new ArrayList<>();
        String populationString = minPopulationEditText.getText().toString();
        try {
            String regionName = spinner.getSelectedItem().toString();
            if ( regionName == null){
                regionName = "";
            }
            //  search by region and population
            if (populationString.length() > 0 && regionName.length() != 0 && !regionName.equals(getResources().getString(R.string.filter_region_none))) {
                Integer minPopulation = Integer.parseInt(populationString);
                Integer regionId = dbHelper.idByRegionName(regionName);
                list = dbHelper.findMoreThatMinPopulationAndRegion(minPopulation, regionId);
            //  search by population only
            } else if (populationString.length() > 0 && regionName.equals(getResources().getString(R.string.filter_region_none))) {
                Integer minPopulation = Integer.parseInt(populationString);
                list = dbHelper.findMoreThatMinPopulation(minPopulation);
            //  search by region only
            } else if ( !regionName.equals(getResources().getString(R.string.filter_region_none))){
                Integer regionId = dbHelper.idByRegionName(regionName);
                list = dbHelper.findWhereDistrict(regionId);
            }
            //  show all
            else {
                list = dbHelper.readAllDistricts();
            }
        } catch (NumberFormatException e) {
            Log.e("ERROR", "ERROR", e);
        }
        initTableView(list);
    }

    private void emptyTable() {
        int cnt = tbl.getChildCount();
        for (int i = cnt - 1; i > 0; i--) {
            tbl.removeViewAt(i);
        }
    }

    private void initTableView(List<InitialData.District> districts) {
        emptyTable();
        for (InitialData.District district : districts) {
            addToTable(district);
        }
    }

    private void addToTable(InitialData.District district) {
        TableRow row = new TableRow(this);
        row.addView(withText(district.name));
        row.addView(withText(district.population));
        row.addView(withText(dbHelper.regionNameById(district.regionId)));
        row.addView(withText(district.code));
        tbl.addView(row);
    }

    private TextView withText(Object obj) {
        TextView tw = new TextView(this);
        tw.setText(obj.toString());
        return tw;
    }
}

