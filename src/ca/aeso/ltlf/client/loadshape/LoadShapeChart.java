package ca.aeso.ltlf.client.loadshape;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.model.CodesTable;
import ca.aeso.ltlf.model.util.Constants;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * LoadShapeChart
 * This composite is responsible for displaying the Load Shape chart
 * 
 * @author mbodor
 */
public class LoadShapeChart extends LtlfComposite {

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
	
	public LoadShapeChart() {

		VerticalPanel vp = new VerticalPanel();
		vp.setHeight(Integer.toString(Constants.LOAD_SHAPE_CHART_HEIGHT));
		vp.setWidth(Integer.toString(Constants.LOAD_SHAPE_CHART_WIDTH));

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