package Model.DAO;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.fields.TextRange;
import com.spire.doc.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileConversionUtils {

	private static final int MAX_PAGES_PER_FILE = 3;

	public static String pdfToWord(String inputFilePath) {
		String outputFilePath = null;
		PdfDocument pdf = null;

		try {
			pdf = new PdfDocument();
			pdf.loadFromFile(inputFilePath);
			int totalPages = pdf.getPages().getCount();

			if (totalPages <= MAX_PAGES_PER_FILE) {
				outputFilePath = inputFilePath.replace(".pdf", "_converted.docx");
				pdf.saveToFile(outputFilePath, com.spire.pdf.FileFormat.DOCX);
			} else {
				List<String> tempFiles = splitAndConvertPdf(pdf, totalPages, inputFilePath);
				if (tempFiles != null) {
					outputFilePath = mergeWordDocuments(tempFiles, inputFilePath);
					cleanupTempFiles(tempFiles);
				}
			}
		} catch (Exception e) {
			System.err.println("Lỗi khi chuyển đổi PDF sang Word: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (pdf != null) {
				pdf.close();
			}
		}

		return outputFilePath;
	}

	private static List<String> splitAndConvertPdf(PdfDocument pdf, int totalPages, String inputFilePath) {
		List<String> tempFiles = new ArrayList<>();

		File inputFile = new File(inputFilePath);
		String originalFileName = inputFile.getName();
		String fileNameWithoutExtension = originalFileName.contains(".")
				? originalFileName.substring(0, originalFileName.lastIndexOf("."))
				: originalFileName;
		String inputFileName = inputFile.getParent() + File.separator + fileNameWithoutExtension;

		try {
			for (int i = 0; i < Math.ceil((double) totalPages / MAX_PAGES_PER_FILE); i++) {
				PdfDocument subPdf = new PdfDocument();
				int startPage = i * MAX_PAGES_PER_FILE;
				int endPage = Math.min(startPage + MAX_PAGES_PER_FILE, totalPages);

				for (int pageIndex = startPage; pageIndex < endPage; pageIndex++) {
					PdfPageBase page = subPdf.getPages().add();
					pdf.getPages().get(pageIndex).createTemplate().draw(page, new java.awt.geom.Point2D.Float(0, 0));
				}

				String tempFilePath = inputFileName + "_temp_part" + (i + 1) + ".docx";
				subPdf.saveToFile(tempFilePath, com.spire.pdf.FileFormat.DOCX);
				subPdf.close();
				tempFiles.add(tempFilePath);
			}
		} catch (Exception e) {
			System.err.println("Lỗi khi tách và chuyển đổi PDF: " + e.getMessage());
			e.printStackTrace();
			cleanupTempFiles(tempFiles);
			return null;
		}

		return tempFiles;
	}

	private static String mergeWordDocuments(List<String> tempFiles, String inputFilePath) {
		String outputFilePath = null;
		Document finalDoc = null;
		Document tempDoc = null;

		try {
			finalDoc = new Document();

			for (String tempFilePath : tempFiles) {
				try {
					tempDoc = new Document();
					tempDoc.loadFromFile(tempFilePath);

					for (Object sectionObj : tempDoc.getSections()) {
						if (sectionObj instanceof Section) {
							Section section = (Section) sectionObj;
							finalDoc.getSections().add((Section) section.deepClone());
						} else {
							System.err.println("Phần tử không phải là Section: " + sectionObj.getClass().getName());
						}
					}
				} catch (Exception e) {
					System.err.println("Lỗi khi gộp file " + tempFilePath + ": " + e.getMessage());
					e.printStackTrace();
					return null;
				} finally {
					if (tempDoc != null) {
						tempDoc.dispose();
						tempDoc = null;
					}
				}
			}

			for (Object sectionObj : finalDoc.getSections()) {
				if (sectionObj instanceof Section) {
					Section section = (Section) sectionObj;
					for (Object bodyChild : section.getBody().getChildObjects()) {
						if (bodyChild instanceof Paragraph) {
							Paragraph para = (Paragraph) bodyChild;
							for (Object childObj : para.getChildObjects()) {
								if (childObj instanceof TextRange) {
									TextRange textRange = (TextRange) childObj;
									textRange.getCharacterFormat().setFontName("Times New Roman");
								}
							}
						} else if (bodyChild instanceof Table) {
							Table table = (Table) bodyChild;
							for (int i = 0; i < table.getRows().getCount(); i++) {
								TableRow row = table.getRows().get(i);
								for (int j = 0; j < row.getCells().getCount(); j++) {
									TableCell cell = row.getCells().get(j);
									for (Object cellChild : cell.getChildObjects()) {
										if (cellChild instanceof Paragraph) {
											Paragraph para = (Paragraph) cellChild;
											for (Object paragraphChild : para.getChildObjects()) {
												if (paragraphChild instanceof TextRange) {
													TextRange textRange = (TextRange) paragraphChild;
													textRange.getCharacterFormat().setFontName("Times New Roman");
												}
											}
										}
									}
								}
							}
						}
					}
				} else {
					System.err.println("Phần tử không phải là Section: " + sectionObj.getClass().getName());
				}
			}

			File inputFile = new File(inputFilePath);
			String originalFileName = inputFile.getName();
			String fileNameWithoutExtension = originalFileName.contains(".")
					? originalFileName.substring(0, originalFileName.lastIndexOf("."))
					: originalFileName;

			outputFilePath = inputFile.getParent() + File.separator + fileNameWithoutExtension + "_converted.docx";

			finalDoc.saveToFile(outputFilePath, FileFormat.Docx_2013);
		} catch (Exception e) {
			System.err.println("Lỗi khi lưu file Word đã gộp: " + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			if (finalDoc != null) {
				finalDoc.dispose();
			}
		}

		return outputFilePath;
	}

	private static void cleanupTempFiles(List<String> tempFiles) {
		for (String tempFilePath : tempFiles) {
			File tempFile = new File(tempFilePath);
			if (tempFile.exists() && !tempFile.delete()) {
				System.err.println("Không thể xóa file tạm: " + tempFilePath);
			}
		}
	}

}