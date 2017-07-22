package com.boberto.el.van.stallone;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.AllTagConstants;

public class ImageFileRewriter {

	public static void main(String args[]) throws ImageReadException, IOException, ParseException, ImageWriteException {
		for (final Path path : getFileNames()) {
			// System.out.println(path);
			File file = path.toFile();
			final IImageMetadata metadata = Imaging.getMetadata(file);
			if (metadata instanceof JpegImageMetadata) {
				final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
				final TiffField field = jpegMetadata
						.findEXIFValueWithExactMatch(AllTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
				String dateOfCaptureString = field.getValueDescription();
				SimpleDateFormat sdf = new SimpleDateFormat("''yyyy:MM:dd HH:mm:ss''");
				Date dateOfCapture = sdf.parse(dateOfCaptureString);
				// dateOfCapture.setHours(dateOfCapture.getHours() + 1);
				// System.out.println(field.getValueDescription());

				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
				final String newPath = "E:\\Italy\\Originals - JavaPlay\\SDCamera2\\Italy "
						+ sdf2.format(dateOfCapture).replace(':', '-').replace("'", "") + ".jpg";
				System.out.println(newPath);
				try {
					Files.copy(path, path.resolveSibling(newPath));
				} catch (FileAlreadyExistsException e) {
					try {
						Files.copy(path,
								path.resolveSibling(newPath.replace(".jpg", "-x.jpg").replace(".JPG", "-x.JPG")));
					} catch (FileAlreadyExistsException e2) {
						try {
							Files.copy(path,
									path.resolveSibling(newPath.replace(".jpg", "-x2.jpg").replace(".JPG", "-x2.JPG")));
						} catch (FileAlreadyExistsException e3) {
							Files.copy(path,
									path.resolveSibling(newPath.replace(".jpg", "-x3.jpg").replace(".JPG", "-x3.JPG")));
						}
					}
				}

				// System.out.println(dateOfCapture);
				// final TiffImageMetadata exif = jpegMetadata.getExif();
				// TiffOutputSet outputSet = exif.getOutputSet();
				// final TiffOutputDirectory exifDirectory =
				// outputSet.getOrCreateExifDirectory();
				// exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
				// //exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL,
				// sdf.format(dateOfCapture));
				// final File destination = new File(
				// "E:\\Italy\\Originals - JavaPlay\\SDCamera2\\" +
				// file.getName());
				// destination.createNewFile();
				// try (FileOutputStream fos = new
				// FileOutputStream(destination);
				// OutputStream os = new BufferedOutputStream(fos);) {
				// new ExifRewriter().updateExifMetadataLossless(file, os,
				// outputSet);
				// }

			}
		}
		// for (final Path path : getFileNames("2013")) {
		// System.out.println(path);
		// File file = path.toFile();
		// final IImageMetadata metadata = Imaging.getMetadata(file);
		// if (metadata instanceof JpegImageMetadata) {
		// final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
		// TiffField tag =
		// jpegMetadata.findEXIFValueWithExactMatch(AllTagConstants.EXIF_TAG_XPKEYWORDS);
		// if (tag != null) {
		// // System.out.println(tag.getStringValue());
		// if (tag.getStringValue().contains("italy")) {
		// System.out.println(path);
		// final String fileName = path.toString();
		// String hour = fileName.substring(fileName.indexOf("2013") + 11,
		// fileName.indexOf("."));
		// String newHour = "" + (Integer.parseInt(hour) + 1);
		// String newPath = fileName.substring(0, fileName.indexOf("2013") + 11)
		// + (Integer.parseInt(newHour) < 10 ? "0" + newHour : newHour)
		// + fileName.substring(fileName.indexOf("."), fileName.length());
		// System.out.println(newPath);
		//
		// // final Path newP =
		// // path.getParent().resolveSibling(newPath);
		// // try {
		// // Files.move(path, path.resolveSibling(newPath));
		// // } catch (FileAlreadyExistsException e) {
		// // Files.move(path,
		// // path.resolveSibling(newPath.replace(".jpg",
		// // "-x.jpg").replace(".JPG", "-x.JPG")));
		// // }
		// }
		// }
		// }
		// }
	}

	private static List<Path> getFileNames() {
		final List<Path> names = new ArrayList<>();
		Path directory = Paths.get("E:\\Italy\\Originals - JavaPlay\\SDCamera\\");
		try (final DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.{jpg,JPG}")) {
			for (final Path entry : stream) {
				// if (entry.getFileName().toString().contains(filter)) {
				names.add(entry);
				// }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return names;
	}
}
