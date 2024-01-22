package assign01;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a grayscale (black and white) image as a 2D array of "pixel"
 * brightnesses 255 is "white" 127 is "gray" 0 is "black" with intermediate
 * values in between
 * 
 * @author Ben Jones and Alex Murdock
 * @version 1/12/2024
 */
public class GrayscaleImage {
	private double[][] imageData; // the actual image data

	/**
	 * Initialize an image from a 2D array of doubles This constructor creates a
	 * copy of the input array
	 * 
	 * @param data initial pixel values
	 * @throws IllegalArgumentException if the input array is empty or "jagged"
	 *                                  meaning not all rows are the same length
	 */
	public GrayscaleImage(double[][] data) {
		if (data.length == 0 || data[0].length == 0) {
			throw new IllegalArgumentException("Image is empty");
		}

		imageData = new double[data.length][data[0].length];
		for (var row = 0; row < imageData.length; row++) {
			if (data[row].length != imageData[row].length) {
				throw new IllegalArgumentException("All rows must have the same length");
			}
			for (var col = 0; col < imageData[row].length; col++) {
				imageData[row][col] = data[row][col];
			}
		}
	}

	/**
	 * Fetches an image from the specified URL and converts it to grayscale Uses the
	 * AWT Graphics2D class to do the conversion, so it may add an item to your
	 * dock/menu bar as if you're loading a GUI program
	 * 
	 * @param url where to download the image
	 * @throws IOException if the image can't be downloaded for some reason
	 */
	public GrayscaleImage(URL url) throws IOException {
		var inputImage = ImageIO.read(url);
		// convert input image to grayscale based on
		// (https://stackoverflow.com/questions/6881578/how-to-convert-between-color-models)
		var grayImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2d = grayImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, null);
		g2d.dispose();
		imageData = new double[grayImage.getHeight()][grayImage.getWidth()];

		// raster is basically a width x height x 1 3-dimensional array
		var grayRaster = grayImage.getRaster();
		for (var row = 0; row < imageData.length; row++) {
			for (var col = 0; col < imageData[0].length; col++) {
				// getSample parameters are x (our column) and y (our row), so they're
				// "backwards"
				imageData[row][col] = grayRaster.getSampleDouble(col, row, 0);
			}
		}
	}

	/**
	 * Saves the image as a PNG file.
	 * 
	 * @param filename
	 * @throws IOException if the file can't be written
	 */
	public void savePNG(File filename) throws IOException {
		var outputImage = new BufferedImage(imageData[0].length, imageData.length, BufferedImage.TYPE_BYTE_GRAY);
		var raster = outputImage.getRaster();
		for (var row = 0; row < imageData.length; row++) {
			for (var col = 0; col < imageData[0].length; col++) {
				raster.setSample(col, row, 0, imageData[row][col]);
			}
		}
		ImageIO.write(outputImage, "png", filename);
	}

	/**
	 * Get the pixel brightness value at the specified coordinates (0,0) is the top
	 * left corner of the image, (width -1, height -1) is the bottom right corner
	 * 
	 * @param x horizontal position, increases left to right
	 * @param y vertical position, **increases top to bottom**
	 * @return the brightness value at the specified coordinates
	 * @throws IllegalArgumentException if x, y are not within the image width/height
	 */
	public double getPixel(int x, int y) {
		// will check to see if passed points are valid points in the image
		if (x < 0 || x >= imageData[0].length || y < 0 || y >= imageData.length) {
			throw new IllegalArgumentException("Coordinates (x, y) are not within the image width/height");
		}

		// return the brightness value at the give point
		return imageData[y][x];
	}

	/**
	 * Two images are equal if they have the same size and each corresponding pixel
	 * in the two images is exactly equal
	 * 
	 * @param other
	 * @return
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof GrayscaleImage)) {
			return false;
		}

		GrayscaleImage otherImage = (GrayscaleImage) other;
		
		// first will check to see if they are even the same dimensions if they are we will check each pixel. Other wise we will return false
		if (this.imageData.length != otherImage.imageData.length
				|| this.imageData[0].length != otherImage.imageData[0].length) {
			return false;
		} else {
			int imageOneMaxY = this.imageData.length;
			int imageOneMaxX = this.imageData[0].length;

			// now will loop through each coordinate in the image to make sure the brightness is the same on both images.
			for (int y = 0; y < imageOneMaxY; y++) {
				for (int x = 0; x < imageOneMaxX; x++) {
					if (this.imageData[y][x] != otherImage.imageData[y][x]) {
						return false; // if any of the brightness values aren't equal then the images aren't equal
					}
				}
			}

			// it has passed all checks so the images most be the same
			return true;
		}
	}

	/**
	 * Computes the average of all values in image data
	 * 
	 * @return the average of the imageData array
	 */
	public double averageBrightness() {
		// arrayList that we will store all our values in
		ArrayList<Double> allCordBrightness = new ArrayList<>();
		int numOfCords = 0;

		int imageOneMaxY = this.imageData.length;
		int imageOneMaxX = this.imageData[0].length;

		for (int y = 0; y < imageOneMaxY; y++) {
			for (int x = 0; x < imageOneMaxX; x++) {
				numOfCords++;
				// here we will get the brightness for the given cord then add it to the ArrayList
				double pixelBrightness = getPixel(x, y);
				allCordBrightness.add(pixelBrightness);
			}
		}

		// now we will add up all of the values in the ArrayList and divide them by the number of cords in the image and that way we will get the average brightness.
		double sumOfBrightness = 0;
		for (double brightness : allCordBrightness) {
			sumOfBrightness += brightness;
		}

		double result = sumOfBrightness / numOfCords;

		// this will yield are average brightness
		return result;
	}

	/**
	 * Return a new GrayScale image where the average new average brightness is 127
	 * To do this, uniformly scale each pixel (ie, multiply each imageData entry by
	 * the same value) Due to rounding, the new average brightness will not be 127
	 * exactly, but should be very close The original image should not be modified
	 * 
	 * @return a GrayScale image with pixel data uniformly rescaled so that its
	 *         averageBrightness() is 127
	 */
	public GrayscaleImage normalized() {
		// calculate the scaling factor and check for divison by 0
		double currentAverageBrightness = averageBrightness();
		double scalingFactor = (currentAverageBrightness != 0) ? 127 / currentAverageBrightness : 0;

		// get height and width of image
		int height = imageData.length;
		int width = imageData[0].length;

		double[][] normalizedData = new double[height][width];

		if (Double.isNaN(currentAverageBrightness)) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					normalizedData[y][x] = 1;
				}
			}
			return new GrayscaleImage(normalizedData);
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				normalizedData[y][x] = imageData[y][x] * scalingFactor;
			}
		}

		return new GrayscaleImage(normalizedData);
	}

	/**
	 * Returns a new grayscale image that has been "mirrored" across the y-axis In
	 * other words, each row of the image should be reversed The original image
	 * should be unchanged
	 * 
	 * @return a new GrayscaleImage that is a mirrored version of the this
	 */
	public GrayscaleImage mirrored() {
		int height = imageData.length;
		int width = imageData[0].length;

		// we will first check to see if its single row image because this can screw things up
		if (height == 1) {
			// if it does only have a height = 1 then we just return the same thing
			return new GrayscaleImage(imageData);
		}

		// now will create a new 2d array to store the mirrored data in
		double[][] mirroredData = new double[height][width];

		// then we will populate the mirroredData array by reversing each row in the current array
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				mirroredData[y][x] = imageData[y][width - 1 - x];
			}
		}

		return new GrayscaleImage(mirroredData);
	}

	/**
	 * Returns a new GrayscaleImage of size width x height, containing the part of
	 * `this` from startRow -> startRow + height, startCol -> startCol + width The
	 * original image should be unmodified
	 * 
	 * @param startRow
	 * @param startCol
	 * @param width
	 * @param height
	 * @return A new GrayscaleImage containing the sub-image in the specified
	 *         rectangle
	 * @throws IllegalArgumentException if the specified rectangle goes outside the
	 *                                  bounds of the original image
	 */
	public GrayscaleImage cropped(int startRow, int startCol, int width, int height) {
		int originalHeight = imageData.length;
		int originalWidth = imageData[0].length;

		// now will check to see if the specified shape goes out of the bounds of the original image
		if (startRow < 0 || startCol < 0 || startRow + height > originalHeight || startCol + width > originalWidth) {
			throw new IllegalArgumentException("Specified rectangle is outside the bounds of the original image");
		}

		// next we create a new 2d array to store our new cropped data
		double[][] croppedData = new double[height][width];

		// now we will populate the new croppedData array with data from the specified shape
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				croppedData[y][x] = imageData[startRow + y][startCol + x];
			}
		}

		// this will return our new GrayscaleImage with the cropped data
		return new GrayscaleImage(croppedData);
	}

	/**
	 * Returns a new "centered" square image (new width == new height) For example,
	 * if the width is 20 pixels greater than the height, this should return a
	 * height x height image, with 10 pixels removed from the left and right edges
	 * of the image If the number of pixels to be removed is odd, remove 1 fewer
	 * pixel from the left or top part (note this convention should be
	 * SIMPLER/EASIER to implement than the alternative) The original image should
	 * not be changed
	 * 
	 * @return a new, square, GrayscaleImage
	 */
	public GrayscaleImage squarified() {
		int originalHeight = imageData.length;
		int originalWidth = imageData[0].length;

		// calculate the number of pixels to be removed from left and right
		int pixelsToRemove = Math.abs(originalWidth - originalHeight) / 2;

		// create a new 2D array for the squared image
		double[][] squaredData;

		// here we account for the two variations of oddly formated images
		if (originalWidth > originalHeight) {
			// if original image is wider, remove pixels from left and right
			squaredData = new double[originalHeight][originalHeight];

			// check if the number of pixels is odd this will loop through each pixel and remove the ones that need to be removed
			for (int y = 0; y < originalHeight; y++) {
				for (int x = 0; x < originalHeight; x++) {
					squaredData[y][x] = imageData[y][pixelsToRemove + x];
				}
			}

		} else if (originalWidth < originalHeight) {
			// if original image is taller, remove pixels from top and bottom
			squaredData = new double[originalWidth][originalWidth];

			// this will loop through each pixel and remove the ones that need to be removed
			for (int y = 0; y < originalWidth; y++) {
				for (int x = 0; x < originalWidth; x++) {
					squaredData[y][x] = imageData[pixelsToRemove + y][x];
				}
			}
			
		} else {
			// if already a square image, return a copy
			squaredData = Arrays.stream(imageData).map(double[]::clone).toArray(double[][]::new);
		}

		return new GrayscaleImage(squaredData);
	}
}
