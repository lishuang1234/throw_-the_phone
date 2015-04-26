package cn.ls.view;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.ls.activity.R;

public class LineChartShow {
	private Activity context;
	private XYMultipleSeriesRenderer mRender = new XYMultipleSeriesRenderer();
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYSeries currentXY;
	private XYSeriesRenderer currentXYRender;
	private GraphicalView graView;

	public LineChartShow(Activity x) {
		this.context = x;
		setUpRenderer();
	}

	public void setUpRenderer() {
		mRender.setApplyBackgroundColor(true);
		mRender.setBackgroundColor(Color.WHITE);
		mRender.setAxisTitleTextSize(20);// 设置坐标轴标题字体大小
		mRender.setChartTitleTextSize(20);// 設置图标标题字体大小
		mRender.setLabelsTextSize(20);// 标签
		mRender.setLegendTextSize(20);// 图例
		// mRender.setMargins(new int[] { 20, 30, 15, 0 });// 设置边缘
		mRender.setFitLegend(true);// 调整合适位置
		mRender.setPanLimits(new double[] { 1, 31, 0, 8 }); // 设置拖动时X轴Y轴允许的最大值最小值.
		mRender.setZoomLimits(new double[] { 1, 31, 0, 8 });// 设置放大缩小时X轴Y轴允许的最大最小值.
		// x、y轴上刻度颜色
		mRender.setMarginsColor(Color.argb(0, 0xF3, 0xF3, 0xF3)); // 图标与四边的颜色
		mRender.setXLabelsColor(context.getResources().getColor(
				R.color.green_deep));
		mRender.setYLabelsColor(0,
				context.getResources().getColor(R.color.green_deep));
		// 轴上数字的数量
		mRender.setXLabels(10);
		mRender.setYLabels(5);
		mRender.setShowGrid(true); // 是否显示网格
	 mRender.setShowGridY(true);                              //是否显示Y网格    
	 mRender.setShowGridX(true);   
		mRender.setXTitle("日期");
		mRender.setAxesColor(context.getResources()
				.getColor(R.color.green_deep));
		mRender.setYTitle("时长");
//		 mRender.setGridColor(Color.GRAY); 

		//
		// mRender.addXTextLabel(1, "1号");
		// mRender.addXTextLabel(2, "2号");
		// mRender.addXTextLabel(3, "3号");
		// mRender.addXTextLabel(4, "4号");
		// mRender.addXTextLabel(5, "5号");
		// mRender.addXTextLabel(6, "6号");
		// mRender.addXTextLabel(7, "7号");
		// mRender.addXTextLabel(8, "8号");
		// mRender.setXLabelsAlign(Align.RIGHT); //
		// ////////////////////////////??
		// mRender.setYLabelsAlign(Align.RIGHT);
		// mRender.setPanEnabled(true); // 图表是否可以移动
		// mRender.setZoomEnabled(false); // 图表是否可以缩放
		mRender.setLegendHeight(70); // 图标文字距离底边的高度
		// mRender.setZoomEnabled(true, false); // 拖动的时候 Y轴不动
		mRender.setPanEnabled(true, false); // 拖动的时候 X轴不动

		currentXYRender = new XYSeriesRenderer();
		mRender.addSeriesRenderer(currentXYRender);
		currentXYRender.setPointStyle(PointStyle.CIRCLE);// 设置节点分格
		currentXYRender.setFillPoints(true);
		currentXYRender.setDisplayChartValues(true);// 显示节点值
		currentXYRender.setDisplayChartValuesDistance(15);
		currentXYRender.setChartValuesTextSize(15);
		currentXYRender.setLineWidth(2);
		currentXYRender.setColor(context.getResources().getColor(
				R.color.green_deep));// 设置线条的颜色

		/**
		 * currentXY.add(1.0, 7.2); currentXY.add(2.0, 8.6); currentXY.add(3.0,
		 * 5.9); graView.repaint();
		 **/

	}

	public void setUpData(ArrayList<double[]> arrayList) {
		currentXY = new XYSeries("使用时长");
		mDataset.addSeries(currentXY);
		if (graView == null) {
			LinearLayout layout = (LinearLayout) context
					.findViewById(R.id.myinforAc_ll_chartshow);
			graView = ChartFactory.getLineChartView(context, mDataset, mRender);
			graView.setBackgroundColor(Color.WHITE);

			layout.addView(graView, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			graView.repaint();
		}
		double[] d;
		for (int i = 0; i < arrayList.size(); i++) {
			d = arrayList.get(i);
			currentXY.add(d[0], d[1]);
			System.out.println("Double[]" + d[0] + d[1]);
		}
		graView.repaint();
	}

}
