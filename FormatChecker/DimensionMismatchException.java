import java.io.IOException;
/**
 * 
 * @author TrevorSmith
 *
 * Exception for if the actual dimensions of an input 2d array does not
 * match the said dimensions declared by the first two integers (row, col)
 */
@SuppressWarnings("serial")
public class DimensionMismatchException extends IOException {
	
	public DimensionMismatchException (String message) {
		super(message);
	}
}