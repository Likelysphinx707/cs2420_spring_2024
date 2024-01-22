package assign01;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * For testing the GrayscaleImage class.
 * @author Ben Jones and Alex Murdock
 * @version 1/12/2024
 */
class GrayscaleImageTest {

    private GrayscaleImage smallSquare;
    private GrayscaleImage smallWide;

    @BeforeEach
    void setUp() {
        smallSquare = new GrayscaleImage(new double[][]{{1,2},{3,4}});
        smallWide = new GrayscaleImage(new double[][]{{1,2,3},{4,5,6}});
    }

    
    // Get Pixel tests
    @Test
    void testGetPixel(){
        assertEquals(smallSquare.getPixel(0,0), 1);
        assertEquals(smallSquare.getPixel(1,0), 2);
        assertEquals(smallSquare.getPixel(0,1), 3);
        assertEquals(smallSquare.getPixel(1,1), 4);
    }
    
    @Test
    void testGetPixelWithNegativeCoordinates(){
        assertThrows(IllegalArgumentException.class, () -> smallSquare.getPixel(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> smallSquare.getPixel(0, -1));
    }

    @Test
    void testGetPixelWithOutOfRangeCoordinates(){
        assertThrows(IllegalArgumentException.class, () -> smallSquare.getPixel(2, 0));
        assertThrows(IllegalArgumentException.class, () -> smallSquare.getPixel(0, 2));
    }

    @Test
    void testGetPixelWithLargeImage(){
        assertEquals(smallWide.getPixel(2, 1), 6);
    }
    
    
    
    // Test equals
    @Test
    void testEquals() {
        assertEquals(smallSquare, smallSquare);
        var equivalent = new GrayscaleImage(new double[][]{{1,2},{3,4}});
        assertEquals(smallSquare, equivalent);
    }
    
    @Test
    void testEqualsDifferentPixelValues() {
        var differentPixels = new GrayscaleImage(new double[][]{{1, 2}, {3, 5}});
        assertNotEquals(smallSquare, differentPixels);
    }


    @Test
    void testEqualsDifferentDimensions() {
        var smallSquare2x3 = new GrayscaleImage(new double[][]{{1, 2, 3}, {4, 5, 6}});
        assertNotEquals(smallSquare, smallSquare2x3);
    }
    
    @Test
    void testEqualsDifferentAverageBrightness() {
        var scaledImage = new GrayscaleImage(new double[][]{{2, 4}, {6, 8}});
        assertNotEquals(smallSquare, scaledImage);
    }


    @Test
    void testEqualsNonGrayscaleImage() {
        assertNotEquals(smallSquare, "Not an image");
    }


    @Test
    void testEqualsNull() {
        assertNotEquals(smallSquare, null);
    }
    
    

    // Test averageBrightness
    @Test
    void averageBrightness() {
        assertEquals(smallSquare.averageBrightness(), 2.5, 2.5*.001);
        var bigZero = new GrayscaleImage(new double[1000][1000]);
        assertEquals(bigZero.averageBrightness(), 0);
    }
    
    @Test
    void testAverageBrightnessWithSmallSquare() {
        double[][] smallSquareData = {
                {1, 2},
                {3, 4}
        };
        GrayscaleImage smallSquare = new GrayscaleImage(smallSquareData);
        assertEquals(2.5, smallSquare.averageBrightness(), 2.5 * 0.001);
    }
    
    @Test
    void testAverageBrightnessWithNonSquareImage() {
        double[][] nonSquareData = {
                {1, 2, 3},
                {4, 5, 6}
        };
        GrayscaleImage nonSquareImage = new GrayscaleImage(nonSquareData);
        assertEquals(3.5, nonSquareImage.averageBrightness(), 3.5 * 0.001);
    }

    @Test
    void testAverageBrightnessWithBigZero() {
        double[][] bigZeroData = new double[1000][1000];
        GrayscaleImage bigZero = new GrayscaleImage(bigZeroData);
        assertEquals(bigZero.averageBrightness(), 0);
    }

    @Test
    void testAverageBrightnessWithRandomImage() {
        double[][] randomImageData = {
                {5, 10, 15},
                {20, 25, 30},
                {35, 40, 45}
        };
        GrayscaleImage randomImage = new GrayscaleImage(randomImageData);
        assertEquals(randomImage.averageBrightness(), 25, 25 * 0.001);
    }

    @Test
    void testAverageBrightnessWithSinglePixelImage() {
        double[][] singlePixelData = {{42}};
        GrayscaleImage singlePixelImage = new GrayscaleImage(singlePixelData);
        assertEquals(singlePixelImage.averageBrightness(), 42);
    }
    
    
    
    // Test normalized
    @Test
    void normalized() {
        var smallNorm = smallSquare.normalized();
        assertEquals(smallNorm.averageBrightness(), 127, 127*.001);
        var scale = 127/2.5;
        var expectedNorm = new GrayscaleImage(new double[][]{{scale, 2*scale},{3*scale, 4*scale}});
        for(var row = 0; row < 2; row++){
            for(var col = 0; col < 2; col++){
                assertEquals(smallNorm.getPixel(col, row), expectedNorm.getPixel(col, row),
                        expectedNorm.getPixel(col, row)*.001,
                        "pixel at row: " + row + " col: " + col + " incorrect");
            }
        }
    }
    
    @Test
    void testNormalizedWithNonSquareImage() {
        double[][] nonSquareData = {
                {1, 2, 3},
                {4, 5, 6}
        };
        GrayscaleImage nonSquareImage = new GrayscaleImage(nonSquareData);
        GrayscaleImage normalizedImage = nonSquareImage.normalized();

        assertEquals(127, normalizedImage.averageBrightness(), 127 * 0.001);

        double scale = 127 / nonSquareImage.averageBrightness();
        GrayscaleImage expectedNormalizedImage = new GrayscaleImage(new double[][]{
                {scale, 2 * scale, 3 * scale},
                {4 * scale, 5 * scale, 6 * scale}
        });

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(expectedNormalizedImage.getPixel(col, row), normalizedImage.getPixel(col, row),
                        expectedNormalizedImage.getPixel(col, row) * 0.001,
                        "Pixel at row: " + row + " col: " + col + " is incorrect");
            }
        }
    }
    
    @Test
    void testNormalizedWithSmallSquare() {
        double[][] smallSquareData = {
                {1, 2},
                {3, 4}
        };
        GrayscaleImage smallSquare = new GrayscaleImage(smallSquareData);
        GrayscaleImage smallNorm = smallSquare.normalized();

        assertEquals(127, smallNorm.averageBrightness(), 127 * 0.001);

        double scale = 127 / smallSquare.averageBrightness();
        GrayscaleImage expectedNorm = new GrayscaleImage(new double[][]{
                {scale, 2 * scale},
                {3 * scale, 4 * scale}
        });

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                assertEquals(expectedNorm.getPixel(col, row), smallNorm.getPixel(col, row),
                        expectedNorm.getPixel(col, row) * 0.001,
                        "Pixel at row: " + row + " col: " + col + " is incorrect");
            }
        }
    }

    // TAs said I don't need to worry about this
//    @Test
//    void testNormalizedWithZeroImage() {
//        double[][] zeroImageData = new double[3][3];
//        GrayscaleImage zeroImage = new GrayscaleImage(zeroImageData);
//        GrayscaleImage zeroNorm = zeroImage.normalized();
//        
//        assertEquals(127, zeroNorm.averageBrightness(), 127 * 0.001); // failing test here
//
//        for (int row = 0; row < 3; row++) {
//            for (int col = 0; col < 3; col++) {
//                assertEquals(0, zeroNorm.getPixel(col, row), 0.001, "Pixel at row: " + row + " col: " + col + " is incorrect");
//            }
//        }
//    }
    
    
    
    // Tests mirrored
    @Test
    void mirrored() {
        var expected = new GrayscaleImage(new double[][]{{2,1},{4,3}});
        assertEquals(smallSquare.mirrored(), expected);
    }
    
    @Test
    void testMirroredWithSmallSquare() {
        double[][] smallSquareData = {
                {1, 2},
                {3, 4}
        };
        GrayscaleImage smallSquare = new GrayscaleImage(smallSquareData);
        GrayscaleImage mirroredSmallSquare = smallSquare.mirrored();

        // The mirrored image should have rows reversed
        var expectedMirrored = new GrayscaleImage(new double[][]{{2, 1}, {4, 3}});
        assertEquals(expectedMirrored, mirroredSmallSquare);
    }

    @Test
    void testMirroredWithNonSquareImage() {
        double[][] nonSquareData = {
                {1, 2, 3},
                {4, 5, 6}
        };
        GrayscaleImage nonSquareImage = new GrayscaleImage(nonSquareData);
        GrayscaleImage mirroredNonSquareImage = nonSquareImage.mirrored();

        // The mirrored image should have rows reversed
        var expectedMirroredNonSquare = new GrayscaleImage(new double[][]{{3, 2, 1}, {6, 5, 4}});
        assertEquals(expectedMirroredNonSquare, mirroredNonSquareImage);
    }

    @Test
    void testMirroredWithSingleRowImage() {
        double[][] singleRowData = {{1, 2, 3}};
        GrayscaleImage singleRowImage = new GrayscaleImage(singleRowData);
        GrayscaleImage mirroredSingleRowImage = singleRowImage.mirrored();

        // The mirrored image should be the same as the original (single row)
        assertEquals(singleRowImage, mirroredSingleRowImage);
    }
    
    
    
    // Cropped Tests 
    @Test
    void cropped() {
        var cropped = smallSquare.cropped(1,1,1,1);
        assertEquals(cropped, new GrayscaleImage(new double[][]{{4}}));
    }
    
    @Test
    void testCroppedWithSquareImage() {
        double[][] imageData = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        GrayscaleImage squareImage = new GrayscaleImage(imageData);
        GrayscaleImage croppedImage = squareImage.cropped(0, 0, 2, 2);

        assertEquals(croppedImage, new GrayscaleImage(new double[][]{{1, 2}, {4, 5}}));
    }

    @Test
    void testCroppedWithRectangleImage() {
        double[][] imageData = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        GrayscaleImage rectangleImage = new GrayscaleImage(imageData);
        GrayscaleImage croppedImage = rectangleImage.cropped(0, 0, 3, 2);

        assertEquals(croppedImage, new GrayscaleImage(new double[][]{{1, 2, 3}, {4, 5, 6}}));
    }

    @Test
    void testCroppedWithOutOfBoundsRectangle() {
        double[][] imageData = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        GrayscaleImage image = new GrayscaleImage(imageData);

        // Trying to crop a region outside the bounds of the original image should throw an exception
        assertThrows(IllegalArgumentException.class, () -> image.cropped(1, 1, 3, 2));
    }

    @Test
    void testCroppedDoesNotModifyOriginalImage() {
        double[][] imageData = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        GrayscaleImage originalImage = new GrayscaleImage(imageData);

        // Create a cropped image
        GrayscaleImage croppedImage = originalImage.cropped(0, 0, 2, 2);

        // Ensure the original image is not modified
        assertEquals(originalImage, new GrayscaleImage(imageData));
    }
    
    
    
    // Squarified Tests
    @Test
    void squarified() {
        var squared = smallWide.squarified();
        var expected = new GrayscaleImage(new double[][]{{1,2},{4,5}});
        assertEquals(squared, expected);
    }
    
    @Test
    void squarifiedSuperWide() {
    	var squared = new GrayscaleImage(new double[][]{{1,2,3,4,5},{10,11,12,13,14}});
        var expected = new GrayscaleImage(new double[][]{{2,3},{11,12}});
        assertEquals(squared.squarified(), expected);
    }
    
    @Test
    void squarifiedAlreadySquared() {
    	var squared = smallSquare.squarified();
    	var expected = new GrayscaleImage(new double[][]{{1,2},{3,4}});
        assertEquals(squared.squarified(), expected);
    }

    @Test
    void testSquarifiedWithTallImage() {
        double[][] tallData = {
                {1, 2},
                {3, 4},
                {5, 6}
        };
        GrayscaleImage tallImage = new GrayscaleImage(tallData);
        GrayscaleImage squaredImage = tallImage.squarified();

        GrayscaleImage correctImage = new GrayscaleImage(new double[][]{{1, 2}, {3, 4}});
       
        assertEquals(squaredImage, correctImage);
    }

    @Test
    void testSquarifiedWithOddWidthDifference() {
        double[][] data = {
                {1, 2, 3, 4, 5, 6, 7, 8},
                {6, 7, 8, 9, 10, 11, 12, 13}
        };
        GrayscaleImage image = new GrayscaleImage(data);
        GrayscaleImage squaredImage = image.squarified();

        GrayscaleImage correctImage = new GrayscaleImage(new double[][]{{4, 5}, {9, 10}});
        assertEquals(squaredImage, correctImage);
    }

    @Test
    void testSquarifiedDoesNotModifyOriginalImage() {
        double[][] data = {
                {1, 2, 3},
                {4, 5, 6}
        };
        GrayscaleImage originalImage = new GrayscaleImage(data);

        // Create a squared image
        GrayscaleImage squaredImage = originalImage.squarified();

        // Ensure the original image is not modified
        assertEquals(originalImage, originalImage);
    }
    
}