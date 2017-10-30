package common.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;


/**
 * @author marco
 *
 */
public class PdfReportGenerator {
	
	private PdfReportGenerator() { }
	
	public static InputStream toPDF(String content) throws Exception {
	  final W3CDom wc = new W3CDom();
	  final Document doc = wc.fromJsoup(Jsoup.parse(content));
      final ITextRenderer renderer = new ITextRenderer();
      renderer.setDocument(doc, "file:///pdf");
      renderer.layout();

      final ByteArrayOutputStream os = new ByteArrayOutputStream();
      renderer.createPDF(os);
      return new ByteArrayInputStream(os.toByteArray());
	}
}
