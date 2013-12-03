package com.yeetou.xinyongkaguanjia.util;

import java.util.Iterator;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;

public class AchartUtil {

	// 获取饼状图
	public static GraphicalView getPieGraphicalView(Context mContext,Map<String, Float> map) {
		
		
		
		CategorySeries mSeries = new CategorySeries("");
		DefaultRenderer mRenderer = new DefaultRenderer();
		GraphicalView mChartView;
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setZoomEnabled(false);
		mRenderer.setStartAngle(-90);
		mRenderer.setDisplayValues(false);
		mRenderer.setShowLegend(false);
		mRenderer.setShowLabels(false);
		mRenderer.setPanEnabled(false);
		mRenderer.setClickEnabled(false);

		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		float density = dm.density;
		int screenWidth = dm.widthPixels;

		mChartView = ChartFactory.getPieChartView(mContext, mSeries, mRenderer);
		mChartView.setClickable(false);

		double value = 0;

		Iterator ite = map.keySet().iterator();
		int i = 0;
		while (ite.hasNext()) {
	
				value = map.get(ite.next());
				mSeries.add("Series " + (mSeries.getItemCount() + 1),
						value);
				SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
				renderer.setColor(mContext.getResources().getColor(AppConstant.cart_color[i]));
				mRenderer.addSeriesRenderer(renderer);
				i++;
			} 
			mChartView.repaint();
			
			
			//	GraphicalView mChartView = ChartFactory.getPieChartView(mContext,buildCategoryDataset("1", map),buildCategoryRenderer(mContext, map));
				return mChartView;
	}

	// 设置饼状图的Renderer
	protected static DefaultRenderer buildCategoryRenderer(Context mContext,Map<String, Float> map) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setZoomEnabled(false);
		renderer.setPanEnabled(false);
		// 周围的标注
		renderer.setShowLabels(false);
		// 设置是否显示下面的各颜色的含义
		renderer.setShowLegend(false);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		Iterator iterator = map.keySet().iterator();
		int i = 0;
		for (String key : map.keySet()) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(mContext.getResources().getColor(AppConstant.cart_color[i]));
			renderer.addSeriesRenderer(r);
			i++;
		}

		return renderer;
	}

	// 设置饼状图的Series
	protected static CategorySeries buildCategoryDataset(String title,
			Map<String, Float> map) {
		CategorySeries series = new CategorySeries("123");
		int k = 0;

		for (String key : map.keySet()) {
			series.add("Project " + ++k,Double.parseDouble(map.get(key).toString()));
		}
		return series;
	}

	/*
	 * // 柱状�? public static GraphicalView getXYMultipleGraphicalView(Context
	 * mContext) { GraphicalView gview = ChartFactory.getBarChartView(mContext,
	 * getBarDemoDataset(), getBarDemoRenderer(), Type.DEFAULT); return gview; }
	 * 
	 * 
	 * //获取首页柱状�? public static GraphicalView getXYMultipleGraphicalView(Context
	 * mContext, List<MonthPayments> payments){
	 * 
	 * XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset(); final
	 * int nr = 10; Random r = new Random(); for (int i = 0; i < 3; i++) {
	 * CategorySeries series = new CategorySeries("Demo series " + (i + 1)); for
	 * (int k = 0; k < nr; k++) { series.add(100 + r.nextInt() % 100); }
	 * dataset.addSeries(series.toXYSeries()); }
	 * 
	 * 
	 * XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	 * SimpleSeriesRenderer r1 = new SimpleSeriesRenderer();
	 * r1.setColor(Color.BLUE); renderer.addSeriesRenderer(r1); r1 = new
	 * SimpleSeriesRenderer(); r1.setColor(Color.GREEN);
	 * renderer.addSeriesRenderer(r1); r1 = new SimpleSeriesRenderer();
	 * r1.setColor(Color.WHITE); renderer.addSeriesRenderer(r1);
	 * setChartSettings(renderer);
	 * 
	 * 
	 * GraphicalView gview = ChartFactory.getBarChartView(mContext, dataset,
	 * renderer, Type.DEFAULT); return gview; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * // 设置柱状图的Series private static XYMultipleSeriesDataset
	 * getBarDemoDataset() { XYMultipleSeriesDataset dataset = new
	 * XYMultipleSeriesDataset(); final int nr = 10; Random r = new Random();
	 * for (int i = 0; i < 3; i++) { CategorySeries series = new
	 * CategorySeries("Demo series " + (i + 1)); for (int k = 0; k < nr; k++) {
	 * series.add(100 + r.nextInt() % 100); }
	 * dataset.addSeries(series.toXYSeries()); } return dataset; }
	 * 
	 * // 设置柱状图的Renderer public static XYMultipleSeriesRenderer
	 * getBarDemoRenderer() { XYMultipleSeriesRenderer renderer = new
	 * XYMultipleSeriesRenderer(); SimpleSeriesRenderer r = new
	 * SimpleSeriesRenderer(); r.setColor(Color.BLUE);
	 * renderer.addSeriesRenderer(r); r = new SimpleSeriesRenderer();
	 * r.setColor(Color.GREEN); renderer.addSeriesRenderer(r); r = new
	 * SimpleSeriesRenderer(); r.setColor(Color.WHITE);
	 * renderer.addSeriesRenderer(r); setChartSettings(renderer); return
	 * renderer; }
	 * 
	 * // 设置柱状图Renderer的属�? private static void
	 * setChartSettings(XYMultipleSeriesRenderer renderer) { //
	 * renderer.setChartTitle("Chart demo"); renderer.setXTitle("x values");
	 * renderer.setYTitle("y values"); renderer.setXAxisMin(0.5);//
	 * 设置X轴的�?��值为0.5 renderer.setXAxisMax(10.5);// 设置X轴的�?��值为10.5
	 * renderer.setYAxisMin(0);// 设置Y轴的�?��值为0 renderer.setYAxisMax(210);//
	 * 设置Y轴最大�?�?10 renderer.setDisplayChartValues(true); // 设置是否在柱体上方显示�?
	 * renderer.setShowGrid(true);// 设置是否在图表中显示网格 renderer.setXLabels(0);//
	 * 设置X轴显示的刻度标签的个�? renderer.setMarginsColor(Color.WHITE); // 边框背景颜色
	 * renderer.setClickEnabled(true);// 设置可监点击 renderer.setPanEnabled(true,
	 * false);// 设置XY轴是否可惜滑�? for (int i = 1; i < 11; i++) {
	 * renderer.addTextLabel(i, "" + i); } }
	 */
}
