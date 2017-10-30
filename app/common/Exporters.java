package common;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import java.io.ByteArrayOutputStream;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Column;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import play.i18n.Lang;
import play.mvc.Controller;
import play.mvc.Util;

/**
 * @author marco
 *
 */
public class Exporters extends Controller {

  public interface ITableRows {
    void setHeaders(String ...headers);
    Row add();
  }

  public interface ISpreadsheetExporter {
    void export(ITableRows rows);
  }

  @Util
  public static void exportToOds(String name, ISpreadsheetExporter exporter) {

    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(exporter);

    try {
      final SpreadsheetDocument document = SpreadsheetDocument
          .newSpreadsheetDocument();
      final Table table = document.getSheetByIndex(0);
      // XXX: why?
      document.setLocale(Lang.getLocale());

      table.setTableName(name);
      exporter.export(new ITableRows() {

        @Override
        public void setHeaders(String ...headers) {
          final Row row = table.getRowByIndex(0);
          int i = 0;
          for (String header : headers) {
            row.getCellByIndex(i).setStringValue(header);
            i++;
          }
          for (Column column : table.getColumnList()) {
            column.setUseOptimalWidth(true);
          }
        }

        @Override
        public Row add() {
          return table.appendRow();
        }
      });
      final ByteArrayOutputStream data = new ByteArrayOutputStream();
      document.save(data);
      renderBinary(ByteSource.wrap(data.toByteArray()).openStream(),
          name + ".ods",
          "application/vnd.oasis.opendocument.spreadsheet", false);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
