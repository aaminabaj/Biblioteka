package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PDFUtil {
    // Metoda za export
    public static <T> void exportTableToPDF(TableView<T> tableView, String title) {
        if (tableView.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Nema podataka za export.");
            alert.showAndWait();
            return;
        }

        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
                .append("table { width: 100%; border-collapse: collapse; }")
                .append("th, td { border: 1px solid black; padding: 5px; text-align: left; }")
                .append("th { background-color: #f2f2f2; }")
                .append("</style></head><body>");

        html.append("<h2>").append(title).append("</h2>");
        html.append("<table><tr>");

        // Header row
        for (TableColumn<T, ?> col : tableView.getColumns()) {
            String colText = col.getText();
            if (colText != null && !colText.isEmpty() &&
                    !colText.equalsIgnoreCase("Edit") &&
                    !colText.equalsIgnoreCase("Delete") &&
                    !colText.equalsIgnoreCase("Vrati")) {
                html.append("<th>").append(colText).append("</th>");
            }
        }
        html.append("</tr>");

        // Redovi
        for (T item : tableView.getItems()) {
            html.append("<tr>");
            for (TableColumn<T, ?> col : tableView.getColumns()) {
                String colText = col.getText();
                if (colText != null && !colText.isEmpty() &&
                        !colText.equalsIgnoreCase("Edit") &&
                        !colText.equalsIgnoreCase("Delete") &&
                        !colText.equalsIgnoreCase("Vrati")) {
                    Object cellData = col.getCellData(item);
                    html.append("<td>").append(cellData != null ? escapeHtml(cellData.toString()) : "").append("</td>");
                }
            }
            html.append("</tr>");
        }

        html.append("</table></body></html>");

        try {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Spremi PDF");
            fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            java.io.File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
            if (file != null) {
                com.openhtmltopdf.pdfboxout.PdfRendererBuilder builder = new com.openhtmltopdf.pdfboxout.PdfRendererBuilder();
                builder.withHtmlContent(html.toString(), null);
                builder.toStream(new java.io.FileOutputStream(file));
                builder.run();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText("PDF je uspješno generisan.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Greška pri generisanju PDF-a.");
            alert.showAndWait();
        }
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;")
                .replace("ć", "c")
                .replace("č", "c")
                .replace("đ", "dj")
                .replace("dž", "dz")
                .replace("ž", "z")
                .replace("š", "s");
    }


}
