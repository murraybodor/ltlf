package ca.aeso.ltlf.client.loadshape;

import ca.aeso.ltlf.client.common.LtlfComposite;
import ca.aeso.ltlf.model.util.Constants;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * LoadHistoryChart
 * This composite is responsible for graphing the MP load history values
 * 
 * @author mbodor
 */
public class LoadHistoryChart extends LtlfComposite {

	Image chartImage;

	public LoadHistoryChart() {

		VerticalPanel vp = new VerticalPanel();
		vp.setHeight(Integer.toString(Constants.ANALYSIS_CHART_HEIGHT));
		vp.setWidth(Integer.toString(Constants.ANALYSIS_CHART_WIDTH));

		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		chartImage = new Image();

		vp.add(chartImage);
		
		initWidget(vp);
	}

	public void setImageUrl(String imageUrl) {
		chartImage.setUrl(imageUrl);
	}
}