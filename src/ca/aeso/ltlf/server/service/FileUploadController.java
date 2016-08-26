package ca.aeso.ltlf.server.service;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

import ca.aeso.ltlf.model.Calendar;
import ca.aeso.ltlf.server.dao.CalendarDao;

public class FileUploadController extends AbstractCommandController {

	private CalendarDao _calDao;

	public CalendarDao getCalendarDao() {
		return _calDao;
	}

	public void setCalendarDao(CalendarDao aValue) {
		_calDao = aValue;
	}
	    
	protected ModelAndView handle(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, BindException arg3)
	throws Exception {

		try {
			FileUploadBean fileUploadBean = (FileUploadBean) arg2;
			byte[] file = fileUploadBean.getFile();

			if (file != null) {
				ArrayList  calList = new ArrayList();
				ByteArrayInputStream fileStream = new ByteArrayInputStream(file);
				
				POIFSFileSystem fs = new POIFSFileSystem(fileStream);
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);
				Iterator rowIterator = sheet.rowIterator();
				while (rowIterator.hasNext()) {
					HSSFRow row = (HSSFRow)rowIterator.next();
					Calendar newCal = new Calendar();
					Iterator cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						HSSFCell cell = (HSSFCell) cellIterator.next();
						if (cell.getCellNum() > 4) {
							break;
						}
						try {
							switch (cell.getCellNum()){
							case 0:
								newCal.setTimeDwhKey(new Double(cell.getNumericCellValue()).longValue());
								break;
							case 1: Date _date = cell.getDateCellValue();
								newCal.setForecastDate(_date);
								break;
							case 2: 
								newCal.setBaseDay(new Double(cell.getNumericCellValue()).intValue());
								break;
							case 3:
								newCal.setBaseYear(new Double(cell.getNumericCellValue()).intValue());
								break;
							}
						}
						catch (NumberFormatException ex){
							System.out.println(ex);
						}
					}
					calList.add(newCal);
				}
				_calDao.deleteAllCalendars();
				_calDao.saveCalendars(calList);
			}
		}
		catch (Exception e) {
			arg1.getWriter().println("Error Response");
			System.out.println(e);
			return null;
		}

		arg1.getWriter().println("Success Response");
		return null;
	}

	// this method is overriding, and specify how spring convert multipart into a byte array that binds to our command class
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		super.initBinder(request, binder);
	}

}
