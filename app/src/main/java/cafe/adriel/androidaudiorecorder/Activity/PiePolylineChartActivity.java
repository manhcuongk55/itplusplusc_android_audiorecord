
package cafe.adriel.androidaudiorecorder.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cafe.adriel.androidaudiorecorder.common.Const;
import cafe.adriel.androidaudiorecorder.model.Day;
import cafe.adriel.androidaudiorecorder.model.StaticReponse;
import cafe.adriel.androidaudiorecorder.rest.ApiClient;
import cafe.adriel.androidaudiorecorder.rest.ApiInterface;
import cafe.adriel.androidaudiorecorder.storage.StorageManager;
import cafe.adriel.androidaudiorecorder.utils.MyAxisValueFormatter;
import cafe.adriel.androidaudiorecorder.utils.MyValueFormatter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PiePolylineChartActivity extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private BarChart mChart, mChart2;
    private PieChart mChart3;

    private Typeface tf;
    String TAG = "Chart";

    List<Day> days;

    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    private TextView mDateDisplay;

    public static final int[] MATERIAL_COLORS = {
            rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")
    };

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_piechart);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart2 = (BarChart) findViewById(R.id.chart2);


        mChart.setOnChartValueSelectedListener(this);

        mChart.getDescription().setEnabled(false);

        mChart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setDrawTopYLabelEntry(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);


        // chart 2

        mChart2.setOnChartValueSelectedListener(this);

        mChart2.getDescription().setEnabled(false);

        mChart2.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        mChart2.setPinchZoom(false);

        mChart2.setDrawGridBackground(false);
        mChart2.setDrawBarShadow(false);

        mChart2.setDrawValueAboveBar(false);
        mChart2.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis2 = mChart2.getAxisLeft();
        leftAxis2.setValueFormatter(new MyAxisValueFormatter());
        leftAxis2.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart2.getAxisRight().setEnabled(false);

        XAxis xLabels2 = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);


        Legend l2 = mChart.getLegend();
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l2.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l2.setDrawInside(false);
        l2.setFormSize(8f);
        l2.setFormToTextSpace(4f);
        l2.setXEntrySpace(6f);

        //

        //hinh tron

        mChart3 = (PieChart) findViewById(R.id.chart3);
        mChart3.setUsePercentValues(true);
        mChart3.getDescription().setEnabled(false);
        mChart3.setExtraOffsets(5, 10, 5, 5);

        mChart3.setDragDecelerationFrictionCoef(0.95f);

        mChart3.setCenterTextTypeface(mTfLight);
        mChart3.setCenterText(generateCenterSpannableText());

        mChart3.setDrawHoleEnabled(true);
        mChart3.setHoleColor(Color.WHITE);

        mChart3.setTransparentCircleColor(Color.WHITE);
        mChart3.setTransparentCircleAlpha(110);

        mChart3.setHoleRadius(58f);
        mChart3.setTransparentCircleRadius(61f);

        mChart3.setDrawCenterText(true);

        mChart3.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart3.setRotationEnabled(true);
        mChart3.setHighlightPerTapEnabled(true);

        //pick time

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
        updateDisplay();

        //        map.put("startDate", "1516406400000");
//        map.put("endDate", "1516406400999");
//        map.put("type", "1");
        Timestamp timestamp = new Timestamp(c.getTimeInMillis());
        getData(timestamp.getTime() + "", timestamp.getTime() + "", 1);

    }

    private void drawStackedBar() {

        if (days == null || days.size() == 0)
            return;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        for (int i = 1; i <= 24; i++) {

            for (int j = 0; j < days.size(); j++) {

                if (i == days.get(j).getTime()) {
                    float val1 = days.get(j).getReviewed();
                    float val2 = days.get(j).getAllRecordered();
                    yVals1.add(new BarEntry(
                            i,
                            new float[]{val1, val2},
                            getResources().getDrawable(R.drawable.aar_ic_check)));

                    float fal1 = days.get(j).getTimeReviewed();
                    float fal2 = days.get(j).getTimeRecorder();
                    yVals2.add(new BarEntry(
                            i,
                            new float[]{fal1, fal2},
                            getResources().getDrawable(R.drawable.aar_ic_check)));

                }
            }

        }


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "24h");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Reviewed", "Not Reviewed"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            //   data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);
        }

        mChart.setFitBars(true);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);


        BarDataSet set2;

        if (mChart2.getData() != null &&
                mChart2.getData().getDataSetCount() > 0) {
            set2 = (BarDataSet) mChart2.getData().getDataSetByIndex(0);
            set2.setValues(yVals1);
            mChart2.getData().notifyDataChanged();
            mChart2.notifyDataSetChanged();
        } else {
            set2 = new BarDataSet(yVals2, "times");
            set2.setDrawIcons(false);
            set2.setColors(getColors());
            set2.setStackLabels(new String[]{"TimeReviewed", "Not TimeReviewed"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.WHITE);

            mChart2.setData(data);
        }

        mChart2.setFitBars(true);
        mChart2.animateY(1400, Easing.EasingOption.EaseInOutQuart);


        // draw circle


        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        int totalTimeReview = 0, totalTimeRecord = 0;
        for (int i = 0; i < days.size(); i++) {
            totalTimeReview += days.get(i).getTimeReviewed();
            totalTimeRecord += days.get(i).getTimeRecorder();
        }

        entries.add(new PieEntry(totalTimeReview));
        entries.add(new PieEntry(totalTimeRecord - totalTimeReview));

        PieDataSet dataSet = new PieDataSet(entries, "Time not review");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mChart3.setData(data);

        // undo all highlights
        mChart3.highlightValues(null);

        mChart3.invalidate();
    }


    private void getData(String startDate, String endDate, int type) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
//        map.put("startDate", "1516406400000");
//        map.put("endDate", "1516406400999");
//        map.put("type", "1");
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("type", type + "");


        String token = "Bearer " + StorageManager.getStringValue(getApplicationContext(), Const.TOKEN, "");

        Call<StaticReponse> call2 = apiService.getStatistic(map, token);

        call2.enqueue(new Callback<StaticReponse>() {
            @Override
            public void onResponse(Call<StaticReponse> call, Response<StaticReponse> response1) {
                int statusCode = response1.code();

                if (statusCode == 200) {
                    String mesa = response1.body().getMessange();
                    Toast.makeText(getApplicationContext(), "result :" + mesa, Toast.LENGTH_LONG).show();
                    Log.e(TAG, statusCode + "");

                    days = response1.body().getData();
                    drawStackedBar();
                } else {
                    Toast.makeText(getApplicationContext(), "error 1 :" + statusCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StaticReponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());

    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    private int[] getColors() {

        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("TotalTime Chart\n Developed by duypq3");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aar_static, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.static_day:
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.static_month:
                break;
            case R.id.static_year:
                break;
            default:
                break;
        }
        return true;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    private void updateDisplay() {
        mDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));
    }

}
