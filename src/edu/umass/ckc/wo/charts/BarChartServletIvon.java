package edu.umass.ckc.wo.charts;

import org.jCharts.axisChart.AxisChart;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelPosition;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelRenderer;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.interfaces.IAxisDataSeries;
import org.jCharts.encoders.ServletEncoderHelper;
import org.jCharts.properties.*;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.types.ChartType;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.awt.*;
import java.io.IOException;


public class BarChartServletIvon extends HttpServlet
{
	//---all of my charts serviced by this Servlet will have the same properties.
	private BarChartProperties barChartProperties;

	//---all of my charts serviced by this Servlet will have the same properties.
	protected LegendProperties legendProperties;
	protected AxisProperties axisProperties;
	protected ChartProperties chartProperties;

	protected int width = 600;
	protected int height = 500;


	/**********************************************************************************************
	 *
	 **********************************************************************************************/
	public void init()
	{
		this.legendProperties = new LegendProperties();
		this.chartProperties = new ChartProperties();
		this.axisProperties = new AxisProperties( false );
		ChartFont axisScaleFont = new ChartFont( new Font( "Georgia Negreta cursiva", Font.PLAIN, 13 ), Color.black );
		axisProperties.getXAxisProperties().setScaleChartFont( axisScaleFont );
		axisProperties.getYAxisProperties().setScaleChartFont( axisScaleFont );

		ChartFont axisTitleFont = new ChartFont( new Font( "Arial Narrow", Font.PLAIN, 14 ), Color.black );
		axisProperties.getXAxisProperties().setTitleChartFont( axisTitleFont );
		axisProperties.getYAxisProperties().setTitleChartFont( axisTitleFont );

//		DataAxisProperties dataAxisProperties = (DataAxisProperties) axisProperties.getYAxisProperties();

//		try
//		{
//			dataAxisProperties.setUserDefinedScale( 0, 30 );
//			dataAxisProperties.setNumItems(15);
//		}
//		catch( PropertyException propertyException )
//		{
//			propertyException.printStackTrace();
//		}
//        dataAxisProperties.setUsePercentSigns(true);
		//dataAxisProperties.setRoundToNearest( 2 );

		ChartFont titleFont = new ChartFont( new Font( "Georgia Negreta cursiva", Font.PLAIN, 14 ), Color.black );
		this.chartProperties.setTitleFont( titleFont );

		this.barChartProperties = new BarChartProperties();

		//ValueLabelRenderer valueLabelRenderer = new ValueLabelRenderer( false, false, true, -1 );
		//valueLabelRenderer.setValueLabelPosition( ValueLabelPosition.ON_TOP );
		//valueLabelRenderer.useVerticalLabels( false );
		//barChartProperties.addPostRenderEventListener( valueLabelRenderer );

	}


	/**********************************************************************************************
	 *
	 **********************************************************************************************/
	public void service( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
	{
		try
		{
            HttpSession sess = req.getSession();
            IAxisDataSeries dataSeries = (IAxisDataSeries) sess.getAttribute("dataSeries");
            double[][] data = (double[][]) sess.getAttribute("data");
            String[] legendLabels = (String[]) sess.getAttribute("legendLabels");
			Paint[] paints = new Paint[]{Color.yellow};
            
            dataSeries.addIAxisPlotDataSet( new AxisChartDataSet( data, legendLabels, paints, ChartType.BAR, this.barChartProperties ) );

			AxisChart axisChart = new AxisChart( dataSeries, this.chartProperties, this.axisProperties, this.legendProperties, this.width, this.height );
			ServletEncoderHelper.encodeJPEG13( axisChart, 1.0f, resp );


        }
		catch( Throwable throwable )
		{
			//HACK do your error handling here...
			throwable.printStackTrace();
		}
	}
}