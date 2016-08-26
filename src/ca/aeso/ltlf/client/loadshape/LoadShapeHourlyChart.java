package ca.aeso.ltlf.client.loadshape;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.model.util.Constants;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LoadListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * LoadShapeHourlyChart
 * This composite is responsible displaying the Hourly Shape chart
 * 
 * @author mbodor
 */
public class LoadShapeHourlyChart extends LtlfComposite {

	Image loadingImage;
	Image chartImage;

	private LoadListener loadListener = new LoadListener() {
		public void onLoad(Widget sender) {
			chartImage.setVisible(true);
			loadingImage.setVisible(false);
		}
		public void onError(Widget sender) {
		}
		
	};
	
	public LoadShapeHourlyChart() {

		VerticalPanel vp = new VerticalPanel();
		vp.setHeight(Integer.toString(Constants.LOAD_SHAPE_HOURLY_CHART_HEIGHT));
		vp.setWidth(Integer.toString(Constants.LOAD_SHAPE_HOURLY_CHART_WIDTH));

		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		chartImage = new Image();
		chartImage.setVisible(false);
		chartImage.addLoadListener(loadListener);

		loadingImage = new Image();

		vp.add(loadingImage);
		vp.add(chartImage);
		
		initWidget(vp);
	}

	public void setChartUrl(String imageUrl) {
		chartImage.setUrl(imageUrl);
	}
	
	public void setLoadingUrl(String imageUrl) {
		chartImage.setVisible(false);
		loadingImage.setUrl(imageUrl);
		loadingImage.setVisible(true);
	}
}