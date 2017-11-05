package com.kadiryaka.kalorimerkezi.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kadiryaka.kalorimerkezi.R;
import com.kadiryaka.kalorimerkezi.adapter.ExcersizeListAdapter;
import com.kadiryaka.kalorimerkezi.adapter.SubReadOnlyAdapter;
import com.kadiryaka.kalorimerkezi.dto.DateDTO;
import com.kadiryaka.kalorimerkezi.dto.DoDTO;
import com.kadiryaka.kalorimerkezi.dto.ExcersizeListDTO;
import com.kadiryaka.kalorimerkezi.dto.SaveExcersizeDTO;
import com.kadiryaka.kalorimerkezi.dto.SubExcersizeDTO;
import com.kadiryaka.kalorimerkezi.dto.SubExcersizeListDTO;
import com.kadiryaka.kalorimerkezi.resource.UserResource;
import com.kadiryaka.kalorimerkezi.util.Constants;
import com.kadiryaka.kalorimerkezi.util.SliderMenu;
import com.kadiryaka.kalorimerkezi.util.Utils;
import java.util.Calendar;
import java.util.Locale;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ExcersizeActivity extends ActionBarActivity {
    ActionBarDrawerToggle slider;

    String token;

    private ListView excersizeListView;

    UserResource userResource = new UserResource();
    AlertDialog levelDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_exercise);

        SliderMenu sliderMenu = new SliderMenu(this);
        slider = sliderMenu.doMenu();

        token = Utils.getUserToken(this);

        if (token == null || token.trim().equalsIgnoreCase(Constants.empty)) {
            goLogin();
        }

        excersizeListView = (ListView) findViewById(R.id.listExcersize);

        fetchExercises();
    }

    private void fetchExercises() {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        userResource.getExcersizeListByUser(token, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                final ExcersizeListDTO excersizeListDTO = (ExcersizeListDTO) o;

                if (excersizeListDTO.excersizeList.size() == 0) {
                    RelativeLayout notFoundError = (RelativeLayout) findViewById(R.id.notFoundError);
                    LinearLayout contentArea = (LinearLayout) findViewById(R.id.contentArea);

                    notFoundError.setVisibility(View.VISIBLE);
                    contentArea.setVisibility(View.GONE);
                } else {
                    ExcersizeListAdapter excersizeListAdapter = new ExcersizeListAdapter(ExcersizeActivity.this, excersizeListDTO.excersizeList, excersizeListDTO.currentExcersize);
                    excersizeListView.setAdapter(excersizeListAdapter);

                    excersizeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if(excersizeListDTO.currentExcersize == 0 && i == 0) {
                                //doSubExcersize(excersizeListDTO.excersizeList.get(i).temp_id);
                                showDayList(true,excersizeListDTO.excersizeList.get(i).temp_id);
                            } else {
                                showDayList(false,excersizeListDTO.excersizeList.get(i).temp_id);
                                //doSubExcersizeReadOnly(excersizeListDTO.excersizeList.get(i).temp_id);
                            }
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                Utils.showRetrofitError(ExcersizeActivity.this, error);
            }
        });
    }

    private void doSubExcersizeReadOnly(final int temp_id, final int dayId) {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        String day = String.valueOf(dayId);
        userResource.getExcersizeByExcersizeTemplateAndDayId(token, String.valueOf(temp_id),day, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                final SubExcersizeListDTO subExcersizeListDTO = (SubExcersizeListDTO) o;

                LayoutInflater inflater = (LayoutInflater) ExcersizeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customTitle = inflater.inflate(R.layout.custom_title_sub_excersize, null);
                TextView subDurum = (TextView) customTitle.findViewById(R.id.subDurum);
                subDurum.setVisibility(View.GONE);

                if (subExcersizeListDTO.excersizeList.size() == 0) {
                    Toast.makeText(ExcersizeActivity.this, "Kayıt bulunamadı", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(new ContextThemeWrapper(ExcersizeActivity.this, R.style.AlertDialogTheme));
                    builderSingle.setCustomTitle(customTitle);
                    final SubReadOnlyAdapter arrayAdapter = new SubReadOnlyAdapter(ExcersizeActivity.this, android.R.layout.select_dialog_item);
                    for(SubExcersizeDTO i : subExcersizeListDTO.excersizeList) {
                        arrayAdapter.add("(" + i.egz_ad + ") " + i.makina_no + "'da " + i.agirlik + " kg ile " + i.adet + " kere.");
                    }
                    builderSingle.setAdapter(arrayAdapter, null);
                    builderSingle.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                Utils.showRetrofitError(ExcersizeActivity.this, error);
            }
        });
    }

    private void doSubExcersize(final int temp_id, final int dayId) {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);
        String day = String.valueOf(dayId);
        userResource.getExcersizeByExcersizeTemplateAndDayId(token, String.valueOf(temp_id),day, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);

                final SubExcersizeListDTO subExcersizeListDTO = (SubExcersizeListDTO) o;

                LayoutInflater inflater = (LayoutInflater) ExcersizeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customTitle = inflater.inflate(R.layout.custom_title_sub_excersize, null);

                if (subExcersizeListDTO.excersizeList.size() == 0) {
                    Toast.makeText(ExcersizeActivity.this, "Kayıt bulunamadı", Toast.LENGTH_LONG).show();
                } else {
                    List<String> listItems = new ArrayList<String>();
                    for(SubExcersizeDTO i : subExcersizeListDTO.excersizeList) {
                        listItems.add("(" + i.egz_ad + ") " + i.makina_no + "'da " + i.agirlik + " kg ile " + i.adet + " kere.");
                    }

                    final boolean[] checkedValues = new boolean[listItems.size()];
                    for(SubExcersizeDTO j : subExcersizeListDTO.excersizeList) {
                        for(DoDTO i : subExcersizeListDTO.doList) {
                            if(i.egz_id == j.egz_id && i.adet.equalsIgnoreCase(j.adet)) {
                                checkedValues[subExcersizeListDTO.excersizeList.indexOf(j)] = true;
                            }
                        }
                    }

                    final boolean[] oldCheckedValues = checkedValues.clone();
                    final List<Integer> deletedIDs = new ArrayList<Integer>();
                    final List<SubExcersizeDTO> addedObjS = new ArrayList<SubExcersizeDTO>();

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ExcersizeActivity.this, R.style.AlertDialogTheme));
                    builder.setCustomTitle(customTitle);
                    builder.setMultiChoiceItems(listItems.toArray(new CharSequence[listItems.size()]), checkedValues,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                }
                            });
                    builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            for(int x = 0; x < checkedValues.length; x++) {
                                if(oldCheckedValues[x] == true && checkedValues[x] == false) {
                                    for(DoDTO i : subExcersizeListDTO.doList) {
                                        if(i.egz_id == subExcersizeListDTO.excersizeList.get(x).egz_id && i.adet.equalsIgnoreCase(subExcersizeListDTO.excersizeList.get(x).adet)) {
                                            deletedIDs.add(i.id);
                                        }
                                    }
                                }
                            }

                            for(int i = 0; i < checkedValues.length; i++) {
                                if(oldCheckedValues[i] == false && checkedValues[i] == true) {
                                    addedObjS.add(subExcersizeListDTO.excersizeList.get(i));
                                }
                            }

                            doSave(deletedIDs, addedObjS);
                            Toast.makeText(ExcersizeActivity.this, "Bugün için egzersiz kaydedildi.", Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                Utils.showRetrofitError(ExcersizeActivity.this, error);
            }
        });
    }

    private void doSave(List<Integer> deletedIDs, List<SubExcersizeDTO> addedObjS) {
        setSupportProgressBarIndeterminateVisibility(Boolean.TRUE);

        SaveExcersizeDTO saveExcersizeDTO = new SaveExcersizeDTO();
        saveExcersizeDTO.doAdd = addedObjS;
        saveExcersizeDTO.doDelete = deletedIDs;

        userResource.saveExcercize(token, saveExcersizeDTO, new Callback() {
            @Override
            public void success(Object o, Response response) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
            }

            @Override
            public void failure(RetrofitError error) {
                setSupportProgressBarIndeterminateVisibility(Boolean.FALSE);
                Utils.showRetrofitError(ExcersizeActivity.this, error);
            }
        });
    }

    private void goLogin() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void showDayList(final Boolean control, final Integer temp_id) {
        final CharSequence[] items = {"Pazartesi","Salı","Çarşamba","Perşembe", "Cuma", "Cumartesi", "Pazar"};
        final int selectedDayIndex = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Egzersiz Günü Seçiniz");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                levelDialog.dismiss();
                Locale trlocale= new Locale("tr-TR");
                Calendar sCalendar = Calendar.getInstance();
                String dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, trlocale);
                if (control) {
                    item++;
                    doSubExcersize(temp_id,item);
                } else {
                    item++;
                    doSubExcersizeReadOnly(temp_id,item);
                }
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (slider.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        slider.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        slider.onConfigurationChanged(newConfig);
    }
}
